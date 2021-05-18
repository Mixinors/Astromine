/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.item;

import com.github.mixinors.astromine.mixin.common.ServerPlayNetworkHandlerAccessor;
import com.github.mixinors.astromine.registry.common.AMCriteria;
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
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.github.mixinors.astromine.registry.common.AMSoundEvents;
import com.github.mixinors.astromine.registry.common.AMConfig;

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
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		var placeVec = player.getCameraPosVec(0.0F);

		var thrustVec = new Vec3d(0.8D, 0.8D, 0.8D);
		
		thrustVec = thrustVec.multiply(player.getRotationVector());

		var random = world.random;
		
		for (var i = 0; i < random.nextInt(64); ++i) {
			world.addParticle(
					ParticleTypes.CAMPFIRE_COSY_SMOKE,
					placeVec.x + thrustVec.x,
					placeVec.y + thrustVec.y,
					placeVec.z + thrustVec.z,
					thrustVec.x * random.nextFloat(),
					thrustVec.y * random.nextFloat(),
					thrustVec.z * random.nextFloat()
			);
		}

		thrustVec = thrustVec.multiply(-1.0F);

		var cooldownManager = player.getItemCooldownManager();
		
		if (!player.isSneaking()) {
			player.addVelocity(thrustVec.x, thrustVec.y, thrustVec.z);
			
			if (player instanceof ServerPlayerEntity serverPlayer && serverPlayer instanceof ServerPlayNetworkHandlerAccessor accessor) {
				accessor.setFloatingTicks(0);
					
				AMCriteria.USE_FIRE_EXTINGUISHER.trigger((ServerPlayerEntity) player);
			}
			
			cooldownManager.set(this, AMConfig.get().fireExtinguisherStandingDelay);
		} else {
			cooldownManager.set(this, AMConfig.get().fireExtinguisherSneakingDelay);
		}

		var resultPos = ((BlockHitResult) player.raycast(6, 0, false)).getBlockPos();

		BlockPos.Mutable.stream(
				new Box(resultPos).expand(2)
		).forEach(position -> {
			var state = world.getBlockState(position);
			
			var block = state.getBlock();

			if (block.isIn(BlockTags.FIRE)) {
				world.setBlockState(position, Blocks.AIR.getDefaultState());
			} else if (block.isIn(BlockTags.CAMPFIRES)) {
				if (state.get(CampfireBlock.LIT))
					world.setBlockState(position, state.with(CampfireBlock.LIT, false));
			}
		});

		world.getOtherEntities(
				null,
				new Box(resultPos).expand(3)
		).forEach(entity -> {
			if (entity.isOnFire()) {
				entity.setFireTicks(0);
				
				if (player instanceof ServerPlayerEntity serverPlayer) {
					AMCriteria.PROPERLY_USE_FIRE_EXTINGUISHER.trigger(serverPlayer);
				}
			}
		});

		if (world.isClient) {
			world.playSound(player, player.getBlockPos(), AMSoundEvents.FIRE_EXTINGUISHER_OPEN.get(), SoundCategory.PLAYERS, 1f, 1f);
		}

		return super.use(world, player, hand);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		this.use(user.world, user, hand);

		return ActionResult.PASS;
	}
}
