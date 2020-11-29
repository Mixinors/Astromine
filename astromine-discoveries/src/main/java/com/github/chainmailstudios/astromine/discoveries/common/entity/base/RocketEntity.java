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

import com.github.chainmailstudios.astromine.common.entity.base.ComponentFluidItemEntity;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesCriteria;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static java.lang.Math.min;

public abstract class RocketEntity extends ComponentFluidItemEntity {
	public static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);

	public RocketEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	protected abstract boolean isFuelMatching();

	protected abstract void consumeFuel();

	protected abstract Vector3d getAcceleration();

	protected abstract Vector3f getPassengerPosition();

	protected abstract Collection<ItemStack> getDroppedStacks();

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(IS_RUNNING, false);
	}

	@Override
	public void positionRider(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			Vector3f position = getPassengerPosition();
			passenger.setPos(getX() + position.x, getY() + position.y, getZ() + position.z);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getEntityData().get(IS_RUNNING)) {
			if (isFuelMatching()) {
				consumeFuel();

				Vector3d acceleration = getAcceleration();

				this.push(0, acceleration.y, 0);
				this.move(MoverType.SELF, this.getDeltaMovement());

				if (!this.level.isClientSide) {
					AABB box = getBoundingBox();

					double y = getY();

					for (double x = box.minX; x < box.maxX; x += 0.0625) {
						for (double z = box.minZ; z < box.maxZ; z += 0.0625) {
							((ServerLevel) level).sendParticles(AstromineDiscoveriesParticles.ROCKET_FLAME, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
						}
					}
				}

				if (BlockPos.MutableBlockPos.betweenClosedStream(getBoundingBox()).anyMatch(pos -> !level.getBlockState(pos).isAir()) && !level.isClientSide) {
					this.tryDisassemble(false);
				}
			} else if (!level.isClientSide) {
				this.push(0, -GravityRegistry.INSTANCE.get(level.dimension()), 0);
				this.move(MoverType.SELF, this.getDeltaMovement());

				if (getDeltaMovement().y < -GravityRegistry.INSTANCE.get(level.dimension())) {
					if (BlockPos.MutableBlockPos.betweenClosedStream(getBoundingBox().move(0, -1, 0)).anyMatch(pos -> !level.getBlockState(pos).isAir()) && !level.isClientSide) {
						this.tryDisassemble(false);
					}
				}
			}
		} else {
			setDeltaMovement(Vec3.ZERO);

			this.hasImpulse = true;
		}
	}

	public void tryDisassemble(boolean intentional) {
		this.tryExplode();

		this.getDroppedStacks().forEach(stack -> Containers.dropItemStack(level, getX(), getY(), getZ(), stack.copy()));

		Collection<Entity> passengers = this.getIndirectPassengers();

		for (Entity passenger : passengers) {
			if (passenger instanceof ServerPlayer) {
				AstromineDiscoveriesCriteria.DESTROY_ROCKET.trigger((ServerPlayer) passenger, intentional);
			}

			passenger.stopRiding();
		}

		this.remove();
	}

	private void tryExplode() {
		float[] strength = { 0 };

		getFluidComponent().forEach(volume -> strength[0] += volume.getAmount().floatValue());

		level.explode(this, getX(), getY(), getZ(), min(strength[0], 32F) + 3F, Explosion.BlockInteraction.BREAK);
	}

	public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
		Vec3 vec3d = getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.yRot + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
		return new Vec3(vec3d.x() + this.getX(), vec3d.y() + this.getY(), vec3d.z() + this.getZ());
	}

	public abstract void openInventory(Player player);

	public void tryLaunch(Player launcher) {
		if (this.getFluidComponent().getFirst().biggerThan(Fraction.EMPTY)) {
			this.getEntityData().set(RocketEntity.IS_RUNNING, true);
			if (launcher instanceof ServerPlayer) {
				AstromineDiscoveriesCriteria.LAUNCH_ROCKET.trigger((ServerPlayer) launcher);
			}
		}
	}
}
