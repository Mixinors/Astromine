/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.item.utility;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.registry.common.AMCriteria;
import com.github.mixinors.astromine.registry.common.AMSoundEvents;
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
		var placeVec = user.getCameraPosVec(0.0F);
		
		var thrustVec = new Vec3d(0.8D, 0.8D, 0.8D);
		
		thrustVec = thrustVec.multiply(user.getRotationVector());
		
		for (var i = 0; i < world.random.nextInt(64); ++i) {
			var randomOffset = world.random.nextFloat();
			
			world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, placeVec.x + thrustVec.x, placeVec.y + thrustVec.y, placeVec.z + thrustVec.z, thrustVec.x * randomOffset, thrustVec.y * randomOffset, thrustVec.z * randomOffset);
		}
		
		thrustVec = thrustVec.multiply(-1);
		
		if (!user.isSneaking()) {
			user.addVelocity(thrustVec.x, thrustVec.y, thrustVec.z);
			
			if (user instanceof ServerPlayerEntity serverUser) {
				serverUser.networkHandler.floatingTicks = 0;
				
				AMCriteria.USE_FIRE_EXTINGUISHER.trigger((ServerPlayerEntity) user);
			}
			
			user.getItemCooldownManager().set(this, AMConfig.get().items.fireExtinguisherStandingDelay);
		} else {
			user.getItemCooldownManager().set(this, AMConfig.get().items.fireExtinguisherSneakingDelay);
		}
		
		var result = (BlockHitResult) user.raycast(6.0D, 0.0F, false);
		
		BlockPos.Mutable.stream(new Box(result.getBlockPos()).expand(2)).forEach(position -> {
			var state = world.getBlockState(position);
			
			if (state.getBlock() instanceof FireBlock) {
				world.setBlockState(position, Blocks.AIR.getDefaultState());
			} else if (state.getBlock() instanceof CampfireBlock) {
				if (state.get(CampfireBlock.LIT)) {
					world.setBlockState(position, state.with(CampfireBlock.LIT, false));
				}
			}
		});
		
		world.getOtherEntities(null, new Box(result.getBlockPos()).expand(3)).forEach(entity -> {
			if (entity.isOnFire()) {
				entity.setFireTicks(0);
				
				if (user instanceof ServerPlayerEntity serverUser) {
					AMCriteria.PROPERLY_USE_FIRE_EXTINGUISHER.trigger(serverUser);
				}
			}
		});
		
		if (world.isClient) {
			world.playSound(user, user.getBlockPos(), AMSoundEvents.FIRE_EXTINGUISHER_OPEN.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		
		return super.use(world, user, hand);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		this.use(user.world, user, hand);
		
		return ActionResult.PASS;
	}
}
