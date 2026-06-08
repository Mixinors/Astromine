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

package com.github.mixinors.astromine.common.entity.rocket;

import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMParticles;
import com.github.mixinors.astromine.common.gravity.GravityManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RocketEntity extends ExtendedEntity {
	private static final String ROCKET_UUID_KEY = "rocket";
	
	public static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);
	
	private Rocket rocket;
	
	public RocketEntity(EntityType<?> type, Level world) {
		super(type, world);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(RUNNING, false);
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		
		if (rocket != null) {
			nbt.putUUID(ROCKET_UUID_KEY, rocket.getUuid());
		}
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		
		if (nbt.contains(ROCKET_UUID_KEY)) {
			rocket = RocketManager.get(level(), nbt.getUUID(ROCKET_UUID_KEY));
		}
	}
	
	public Vec3 getAcceleration() {
		return new Vec3(0.0D, 0.000025 / (Math.abs(getY()) / 1024.0D), 0.0D);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return !this.isRemoved();
	}
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return super.interact(player, hand);
	}
	
	@Override
	public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
		if (player.level().isClientSide()) {
			return InteractionResult.CONSUME;
		}
		
		var linkedRocket = getRocket();
		
		if (linkedRocket == null) {
			linkedRocket = RocketManager.get(level(), getUUID());
			
			if (linkedRocket != null) {
				setRocket(linkedRocket);
			}
		}
		
		if (linkedRocket == null) {
			return InteractionResult.FAIL;
		}
		
		return RocketManager.teleportToRocketInterior(player, linkedRocket.getUuid()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		var level = level();
		
		if (!level.isClientSide) {
			if (isRunning()) {
				var acceleration = getAcceleration();
				
				this.push(0, acceleration.y, 0);
				this.move(MoverType.SELF, this.getDeltaMovement());
				
				var box = getBoundingBox();
				
				for (var x = box.minX; x < box.maxX; x += 0.0625) {
					for (var z = box.minZ; z < box.maxZ; z += 0.0625) {
						((ServerLevel) level).sendParticles(AMParticles.ROCKET_FLAME.get(), x, getY(), z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
					}
				}
			} else {
				this.push(0, -GravityManager.get(level.dimension()), 0);
				this.move(MoverType.SELF, this.getDeltaMovement());
				
				hasImpulse = true;
			}
		}
	}
	
	public boolean isRunning() {
		return entityData.get(RUNNING);
	}
	
	public void setRunning(boolean running) {
		entityData.set(RUNNING, running);
	}
	
	public Rocket getRocket() {
		return rocket;
	}
	
	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}
}
