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

package com.github.chainmailstudios.astromine.discoveries.common.entity.base;

import com.github.chainmailstudios.astromine.common.utilities.VolumeUtilities;
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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import com.github.chainmailstudios.astromine.common.entity.base.ComponentFluidItemEntity;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesCriteria;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class RocketEntity extends ComponentFluidItemEntity {
	public static final TrackedData<Boolean> IS_RUNNING = DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private final Predicate<FluidVolume> fuelPredicate = createFuelPredicate();
	private final Function<RocketEntity, Fraction> consumptionFunction = createConsumptionFunction();
	private final Collection<ItemStack> explosionRemains = createExplosionRemains();
	private final Function<RocketEntity, Vector3d> accelerationFunction = createAccelerationFunction();
	private final Supplier<Vector3f> passengerPosition = createPassengerPosition();

	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	protected abstract Predicate<FluidVolume> createFuelPredicate();

	protected abstract Function<RocketEntity, Fraction> createConsumptionFunction();

	protected abstract Collection<ItemStack> createExplosionRemains();

	protected abstract Function<RocketEntity, Vector3d> createAccelerationFunction();

	protected abstract Supplier<Vector3f> createPassengerPosition();

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(IS_RUNNING, false);
	}

	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			Vector3f position = passengerPosition.get();
			passenger.updatePosition(getX() + position.x, getY() + position.y, getZ() + position.z);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getDataTracker().get(IS_RUNNING)) {
			FluidVolume tank = getTank();

			if (fuelPredicate.test(tank) || tank.isEmpty()) {
				tank.minus(consumptionFunction.apply(this));

				if (tank.isEmpty() && !world.isClient) {
					this.world.getPlayers().forEach(player -> player.sendMessage(new TranslatableText("text.astromine.rocket.disassemble_empty_fuel").formatted(Formatting.RED), false));

					this.tryDisassemble(false);
				} else {
					Vector3d acceleration = accelerationFunction.apply(this);

					this.addVelocity(0, acceleration.y, 0);
					this.move(MovementType.SELF, this.getVelocity());

					if (!this.world.isClient) {
						Box box = getBoundingBox();

						double y = getY();

						for (double x = box.minX; x < box.maxX; x += 0.0625) {
							for (double z = box.minZ; z < box.maxZ; z += 0.0625) {
								((ServerWorld) world).spawnParticles(AstromineDiscoveriesParticles.ROCKET_FLAME, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
							}
						}
					}
				}

				if (BlockPos.Mutable.stream(getBoundingBox()).anyMatch(pos -> world.getBlockState(pos).isFullCube(world, pos)) && !world.isClient) {
					this.world.getPlayers().forEach(player -> player.sendMessage(new TranslatableText("text.astromine.rocket.disassemble_collision").formatted(Formatting.RED), false));

					this.tryDisassemble(false);
				}
			} else if (!world.isClient) {
				this.world.getPlayers().forEach(player -> player.sendMessage(new TranslatableText("text.astromine.rocket.disassemble_invalid_fuel").formatted(Formatting.RED), false));

				this.tryDisassemble(false);
			}
		} else {
			setVelocity(Vec3d.ZERO);
			this.velocityDirty = true;
		}

		VolumeUtilities.transferBetweenFirstAndSecond(getFluidComponent(), getItemComponent());
	}

	private FluidVolume getTank() {
		return getFluidComponent().getFirst();
	}

	public void tryDisassemble(boolean intentional) {
		this.tryExplode();
		this.explosionRemains.forEach(stack -> ItemScatterer.spawn(world, getX(), getY(), getZ(), stack.copy()));
		Collection<Entity> passengers = this.getPassengersDeep();
		for (Entity passenger : passengers) {
			if (passenger instanceof ServerPlayerEntity) {
				AstromineDiscoveriesCriteria.DESTROY_ROCKET.trigger((ServerPlayerEntity) passenger, intentional);
			}
			passenger.stopRiding();
		}
		this.remove();
	}

	private void tryExplode() {
		world.createExplosion(this, getX(), getY(), getZ(), getTank().getAmount().floatValue() + 3f, Explosion.DestructionType.BREAK);
	}

	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		Vec3d vec3d = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.yaw + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
		return new Vec3d(vec3d.getX() + this.getX(), vec3d.getY() + this.getY(), vec3d.getZ() + this.getZ());
	}

	public abstract void openInventory(PlayerEntity player);

	public void tryLaunch(PlayerEntity launcher) {
		if (this.getFluidComponent().getFirst().biggerThan(Fraction.EMPTY)) {
			this.getDataTracker().set(RocketEntity.IS_RUNNING, true);
			if (launcher instanceof ServerPlayerEntity) {
				AstromineDiscoveriesCriteria.LAUNCH_ROCKET.trigger((ServerPlayerEntity) launcher);
			}
		}
	}
}
