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
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class FireExtinguisherItem extends Item {
	public FireExtinguisherItem(Item.Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		this.use(context.getLevel(), context.getPlayer(), context.getHand());
		
		return InteractionResult.PASS;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		var placeVec = user.getEyePosition(0.0F);
		
		var thrustVec = new Vec3(0.8D, 0.8D, 0.8D);
		
		thrustVec = thrustVec.multiply(user.getLookAngle());
		
		for (var i = 0; i < world.random.nextInt(64); ++i) {
			var randomOffset = world.random.nextFloat();
			
			world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, placeVec.x + thrustVec.x, placeVec.y + thrustVec.y, placeVec.z + thrustVec.z, thrustVec.x * randomOffset, thrustVec.y * randomOffset, thrustVec.z * randomOffset);
		}
		
		thrustVec = thrustVec.scale(-1);
		
		if (!user.isShiftKeyDown()) {
			user.push(thrustVec.x, thrustVec.y, thrustVec.z);
			
			if (user instanceof ServerPlayer serverUser) {
				AMCriteria.USE_FIRE_EXTINGUISHER.trigger((ServerPlayer) user);
			}
			
			user.getCooldowns().addCooldown(this, AMConfig.get().items.fireExtinguisherStandingDelay);
		} else {
			user.getCooldowns().addCooldown(this, AMConfig.get().items.fireExtinguisherSneakingDelay);
		}
		
		var result = (BlockHitResult) user.pick(6.0D, 0.0F, false);
		
		BlockPos.MutableBlockPos.betweenClosedStream(new AABB(result.getBlockPos()).inflate(2)).forEach(position -> {
			var state = world.getBlockState(position);
			
			if (state.getBlock() instanceof FireBlock) {
				world.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState());
			} else if (state.getBlock() instanceof CampfireBlock) {
				if (state.getValue(CampfireBlock.LIT)) {
					world.setBlockAndUpdate(position, state.setValue(CampfireBlock.LIT, false));
				}
			}
		});
		
		world.getEntities(null, new AABB(result.getBlockPos()).inflate(3)).forEach(entity -> {
			if (entity.isOnFire()) {
				entity.setRemainingFireTicks(0);
				
				if (user instanceof ServerPlayer serverUser) {
					AMCriteria.PROPERLY_USE_FIRE_EXTINGUISHER.trigger(serverUser);
				}
			}
		});
		
		if (world.isClientSide) {
			world.playSound(user, user.blockPosition(), AMSoundEvents.FIRE_EXTINGUISHER_OPEN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		
		return super.use(world, user, hand);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		this.use(user.level(), user, hand);
		
		return InteractionResult.PASS;
	}
}
