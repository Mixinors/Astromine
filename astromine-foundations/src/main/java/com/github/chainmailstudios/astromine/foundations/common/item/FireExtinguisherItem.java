/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.foundations.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsCriteria;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsSoundEvents;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;

public class FireExtinguisherItem extends Item {
	public FireExtinguisherItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		this.use(context.getWorld(), context.getPlayer(), context.getHand());

		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		Vec3d placeVec = user.getCameraPosVec(0);

		Vec3d thrustVec = new Vec3d(0.8, 0.8, 0.8);

		thrustVec = thrustVec.multiply(user.getRotationVector());

		for (int i = 0; i < world.random.nextInt(64); ++i) {
			float r = world.random.nextFloat();
			world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, placeVec.x + thrustVec.x, placeVec.y + thrustVec.y, placeVec.z + thrustVec.z, thrustVec.x * r, thrustVec.y * r, thrustVec.z * r);
		}

		thrustVec = thrustVec.multiply(-1);

		if (!user.isSneaking()) {
			user.addVelocity(thrustVec.x, thrustVec.y, thrustVec.z);
			if (user instanceof ServerPlayerEntity) {
				((ServerPlayerEntity) user).networkHandler.floatingTicks = 0;
				AstromineFoundationsCriteria.USE_FIRE_EXTINGUISHER.trigger((ServerPlayerEntity) user);
			}
			user.getItemCooldownManager().set(this, AstromineConfig.get().fireExtinguisherStandingDelay);
		} else {
			user.getItemCooldownManager().set(this, AstromineConfig.get().fireExtinguisherSneakingDelay);
		}

		BlockHitResult result = (BlockHitResult) user.raycast(6, 0, false);

		BlockPos.Mutable.stream(new Box(result.getBlockPos()).expand(2)).forEach(position -> {
			BlockState state = world.getBlockState(position);

			if (state.getBlock() instanceof FireBlock) {
				world.setBlockState(position, Blocks.AIR.getDefaultState());
			} else if (state.getBlock() instanceof CampfireBlock) {
				if (state.get(CampfireBlock.LIT))
					world.setBlockState(position, state.with(CampfireBlock.LIT, false));
			}
		});

		world.getOtherEntities(null, new Box(result.getBlockPos()).expand(3)).forEach(entity -> {
			if (entity.isOnFire()) {
				entity.setFireTicks(0);
				if (user instanceof ServerPlayerEntity) {
					AstromineFoundationsCriteria.PROPERLY_USE_FIRE_EXTINGUISHER.trigger((ServerPlayerEntity) user);
				}
			}
		});

		if (world.isClient) {
			world.playSound(user, user.getBlockPos(), AstromineFoundationsSoundEvents.FIRE_EXTINGUISHER_OPEN, SoundCategory.PLAYERS, 1f, 1f);
		}

		return super.use(world, user, hand);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		this.use(user.world, user, hand);

		return ActionResult.PASS;
	}
}
