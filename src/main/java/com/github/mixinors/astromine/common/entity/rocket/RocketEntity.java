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

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMCriteria;
import com.github.mixinors.astromine.registry.common.AMParticles;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Collection;

import static java.lang.Math.min;

public abstract class RocketEntity extends ExtendedEntity {
	public static final int FLUID_INPUT_SLOT_1 = 0;
	public static final int FLUID_INPUT_SLOT_2 = 1;
	
	public static final int FLUID_OUTPUT_SLOT_1 = 0;
	public static final int FLUID_OUTPUT_SLOT_2 = 1;
	
	public static final int ITEM_INPUT_SLOT_1 = 0;
	public static final int ITEM_INPUT_SLOT_2 = 2;
	
	public static final int ITEM_BUFFER_SLOT_1 = 4;
	
	public static final int ITEM_OUTPUT_SLOT_1 = 1;
	public static final int ITEM_OUTPUT_SLOT_2 = 3;
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT_1, ITEM_INPUT_SLOT_2 };
	
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_BUFFER_SLOT_1, ITEM_OUTPUT_SLOT_1, ITEM_OUTPUT_SLOT_2 };
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { FLUID_INPUT_SLOT_1, FLUID_INPUT_SLOT_2 };
	
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { };
	
	public static final TrackedData<Boolean> IS_RUNNING = DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);
		
		itemStorage = new SimpleItemStorage(5).extractPredicate((variant, slot) ->
				slot == ITEM_BUFFER_SLOT_1 || slot == ITEM_OUTPUT_SLOT_1 || slot == ITEM_OUTPUT_SLOT_2
		).insertPredicate((variant, slot) ->
				slot == ITEM_INPUT_SLOT_1 || slot == ITEM_INPUT_SLOT_2
		).listener(() -> {
			syncData();
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		
		fluidStorage = new SimpleFluidStorage(2, getFluidStorageSize()).extractPredicate((variant, slot) ->
				false
		).insertPredicate((variant, slot) ->
				(slot == FLUID_INPUT_SLOT_1 && getFirstFuel().testVariant(variant)) || (slot == FLUID_INPUT_SLOT_2 && getSecondFuel().testVariant(variant))
		).listener(() -> {
			syncData();
		}).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
	}
	
	public abstract FluidIngredient getFirstFuel();
	
	public abstract FluidIngredient getSecondFuel();
	
	public boolean isFuelMatching() {
		return getFirstFuel().test(fluidStorage.getStorage(FLUID_INPUT_SLOT_1)) && getSecondFuel().test(fluidStorage.getStorage(FLUID_INPUT_SLOT_2));
	}
	
	protected void consumeFuel() {
		try (var transaction = Transaction.openOuter()) {
			var firstInputStorage = fluidStorage.getStorage(FLUID_INPUT_SLOT_1);
			var secondInputStorage = fluidStorage.getStorage(FLUID_INPUT_SLOT_2);
			
			if (getFirstFuel().test(fluidStorage.getStorage(FLUID_INPUT_SLOT_1)) && getSecondFuel().test(fluidStorage.getStorage(FLUID_INPUT_SLOT_2))) {
				firstInputStorage.extract(firstInputStorage.getResource(), getFirstFuel().getAmount(), transaction, true);
				secondInputStorage.extract(secondInputStorage.getResource(), getSecondFuel().getAmount(), transaction, true);
			}
			
			transaction.commit();
		}
	}
	
	protected abstract Vector3d getAcceleration();
	
	protected abstract Vec3f getPassengerPosition();
	
	protected abstract Collection<ItemStack> getDroppedStacks();
	
	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(IS_RUNNING, false);
	}
	
	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			var position = getPassengerPosition();
			
			passenger.setPosition(getX() + position.getX(), getY() + position.getY(), getZ() + position.getZ());
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!world.isClient) {
			if (dataTracker.get(IS_RUNNING)) {
				if (isFuelMatching()) {
					consumeFuel();
					
					var acceleration = getAcceleration();
					
					this.addVelocity(0, acceleration.y, 0);
					this.move(MovementType.SELF, this.getVelocity());
					
					var box = getBoundingBox();
					
					for (var x = box.minX; x < box.maxX; x += 0.0625) {
						for (var z = box.minZ; z < box.maxZ; z += 0.0625) {
							((ServerWorld) world).spawnParticles(AMParticles.ROCKET_FLAME.get(), x, getY(), z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
						}
					}
					
					if (BlockPos.Mutable.stream(getBoundingBox()).anyMatch(pos -> !world.getBlockState(pos).isAir()) && !world.isClient) {
						this.tryDisassemble(false);
					}
				} else if (!world.isClient) {
					this.addVelocity(0, -GravityManager.get(world.getRegistryKey()), 0);
					this.move(MovementType.SELF, this.getVelocity());
					
					if (getVelocity().y < -GravityManager.get(world.getRegistryKey())) {
						if (BlockPos.Mutable.stream(getBoundingBox().offset(0, -1, 0)).anyMatch(pos -> !world.getBlockState(pos).isAir()) && !world.isClient) {
							this.tryDisassemble(false);
						}
					}
					
					velocityDirty = true;
				}
			} else {
				this.addVelocity(0, -GravityManager.get(world.getRegistryKey()), 0);
				this.move(MovementType.SELF, this.getVelocity());
				
				if (getVelocity().y < -GravityManager.get(world.getRegistryKey())) {
					if (BlockPos.Mutable.stream(getBoundingBox().offset(0, -1, 0)).anyMatch(pos -> !world.getBlockState(pos).isAir()) && !world.isClient) {
						this.tryDisassemble(false);
					}
				}
				
				velocityDirty = true;
			}
		}
	}
	
	public void tryDisassemble(boolean intentional) {
		this.tryExplode();
		
		this.getDroppedStacks().forEach(stack -> ItemScatterer.spawn(world, getX(), getY(), getZ(), stack.copy()));
		
		var passengers = this.getPassengersDeep();
		
		for (var passenger : passengers) {
			if (passenger instanceof ServerPlayerEntity) {
				AMCriteria.DESTROY_ROCKET.trigger((ServerPlayerEntity) passenger, intentional);
			}
			
			passenger.stopRiding();
		}
		
		this.remove(RemovalReason.KILLED);
	}
	
	private void tryExplode() {
		var strength = fluidStorage.getStorage(FLUID_INPUT_SLOT_1).getAmount() * 0.25F + fluidStorage.getStorage(FLUID_INPUT_SLOT_2).getAmount() * 0.25F;
		
		world.createExplosion(this, getX(), getY(), getZ(), min(strength, 32.0F) + 3.0F, Explosion.DestructionType.BREAK);
	}
	
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		var vec3d = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
		return new Vec3d(vec3d.getX() + this.getX(), vec3d.getY() + this.getY(), vec3d.getZ() + this.getZ());
	}
	
	public abstract void openInventory(PlayerEntity player);
	
	public void tryLaunch(PlayerEntity launcher) {
		if (fluidStorage.getStorage(FLUID_INPUT_SLOT_1).getAmount() > 0 && fluidStorage.getStorage(FLUID_INPUT_SLOT_2).getAmount() > 0) {
			this.getDataTracker().set(RocketEntity.IS_RUNNING, true);
			
			if (launcher instanceof ServerPlayerEntity serverLauncher) {
				AMCriteria.LAUNCH_ROCKET.trigger(serverLauncher);
			}
		}
	}
	
	@Override
	public long getFluidStorageSize() {
		return AMConfig.get().entities.primitiveRocketFluid;
	}
}
