package com.github.chainmailstudios.astromine.discoveries.common.entity.base;

import com.github.chainmailstudios.astromine.common.entity.base.ComponentFluidEntity;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class RocketEntity extends ComponentFluidEntity {
	public static final TrackedData<Boolean> IS_RUNNING = DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected abstract Predicate<FluidVolume> createFuelPredicate();

	private final Predicate<FluidVolume> fuelPredicate = createFuelPredicate();

	protected abstract Function<RocketEntity, Fraction> createConsumptionFunction();

	private final Function<RocketEntity, Fraction> consumptionFunction = createConsumptionFunction();

	protected abstract Collection<ItemStack> createExplosionRemains();

	private final Collection<ItemStack> explosionRemains = createExplosionRemains();

	protected abstract Function<RocketEntity, Vector3d> createAccelerationFunction();

	private final Function<RocketEntity, Vector3d> accelerationFunction = createAccelerationFunction();

	protected abstract Supplier<Vector3f> createPassengerPosition();

	private final Supplier<Vector3f> passengerPosition = createPassengerPosition();

	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);
	}

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

			if (fuelPredicate.test(tank)) {
				tank.from(consumptionFunction.apply(this));

				if (tank.isEmpty()) {
					this.tryDisassemble();

					this.world.getPlayers().forEach(player -> player.sendMessage(new LiteralText("text.astromine.rocket.disassemble_empty_fuel"), false));
				} else {
					Vector3d acceleration = accelerationFunction.apply(this);

					this.addVelocity(acceleration.x, acceleration.y, acceleration.y);

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

				if (BlockPos.Mutable.method_29715(getBoundingBox()).anyMatch(pos -> !world.getBlockState(pos).isAir())) {
					this.tryDisassemble();

					this.world.getPlayers().forEach(player -> player.sendMessage(new LiteralText("text.astromine.rocket.disassemble_collision"), false));
				}
			} else {
				this.tryDisassemble();

				this.world.getPlayers().forEach(player -> player.sendMessage(new LiteralText("text.astromine.rocket.disassemble_invalid_fuel"), false));
			}
		}
	}

	private FluidVolume getTank() {
		return getFluidComponent().getVolume(0);
	}

	private void tryDisassemble() {
		this.tryExplode();
		this.explosionRemains.forEach(stack -> ItemScatterer.spawn(world, getX(), getY(), getZ(), stack));
		this.remove();
	}

	private void tryExplode() {
		world.createExplosion(this, getX(), getY(), getZ(), getTank().getAmount().floatValue(), Explosion.DestructionType.BREAK);
	}
}
