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
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RocketEntity extends ExtendedEntity {
	private static final String ROCKET_UUID_KEY = "rocket";
	
	public static final TrackedData<Boolean> RUNNING = DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private Rocket rocket;
	
	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(RUNNING, false);
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		
		if (rocket != null) {
			nbt.putUuid(ROCKET_UUID_KEY, rocket.getUuid());
		}
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		
		if (nbt.contains(ROCKET_UUID_KEY)) {
			rocket = RocketManager.get(nbt.getUuid(ROCKET_UUID_KEY));
		}
	}
	
	public Vec3d getAcceleration() {
		return new Vec3d(0.0D, 0.000025 / (Math.abs(getY()) / 1024.0D), 0.0D);
	}
	
	@Override
	public boolean isCollidable() {
		return !this.isRemoved();
	}
	
	@Override
	public boolean canHit() {
		return !this.isRemoved();
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		return super.interact(player, hand);
	}
	
	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		if (player.getWorld().isClient()) {
			return ActionResult.CONSUME;
		}
		
		RocketManager.teleportToRocketInterior(player, getUuid());
		
		return super.interactAt(player, hitPos, hand);
	}
	
	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!getWorld().isClient()) {
			if (isRunning()) {
				var acceleration = getAcceleration();
				
				this.addVelocity(0, acceleration.y, 0);
				this.move(MovementType.SELF, this.getVelocity());
				
				var box = getBoundingBox();
				
				for (var x = box.minX; x < box.maxX; x += 0.0625) {
					for (var z = box.minZ; z < box.maxZ; z += 0.0625) {
						((ServerWorld) getWorld()).spawnParticles(AMParticles.ROCKET_FLAME.get(), x, getY(), z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
					}
				}
			} else {
				this.addVelocity(0, -GravityManager.get(getWorld().getRegistryKey()), 0);
				this.move(MovementType.SELF, this.getVelocity());
				
				velocityDirty = true;
			}
		}
	}
	
	public boolean isRunning() {
		return dataTracker.get(RUNNING);
	}
	
	public void setRunning(boolean running) {
		dataTracker.set(RUNNING, running);
	}
	
	public Rocket getRocket() {
		return rocket;
	}
	
	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}
}
