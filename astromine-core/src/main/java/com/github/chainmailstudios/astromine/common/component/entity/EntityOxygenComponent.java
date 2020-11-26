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

package com.github.chainmailstudios.astromine.common.component.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Component} containing oxygen levels for an entity.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #writeToNbt(CompoundTag)} and {@link #readFromNbt(CompoundTag)}.
 */
public final class EntityOxygenComponent implements Component {
	int oxygen = 0;

	int minimumOxygen = -20;
	int maximumOxygen = 180;

	Entity entity;

	/** Instantiates an {@link EntityOxygenComponent}. */
	private EntityOxygenComponent(Entity entity) {
		this.entity = entity;
	}

	/** Returns the {@link EntityOxygenComponent} of the given {@link V}. */
	@Nullable
	public static <V> EntityOxygenComponent get(V v) {
		try {
			return AstromineComponents.ENTITY_OXYGEN_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	/** Instantiates an {@link EntityOxygenComponent}. */
	public static EntityOxygenComponent of(Entity entity) {
		return new EntityOxygenComponent(entity);
	}

	/** Simulate behavior, damaging the entity if out of oxygen. */
	public void simulate(boolean isBreathing) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;

			if (player.isCreative() || player.isSpectator()) {
				return;
			}
		}

		oxygen = nextOxygen(isBreathing, oxygen);

		if (oxygen == getMinimumOxygen()) {
			boolean isAK9 = false;

			if (entity instanceof PlayerEntity) {
				isAK9 = ((PlayerEntity) entity).getGameProfile().getId().toString().equals("38113444-0bc0-4502-9a4c-17903067907c");
			}

			if (!isAK9 || AstromineConfig.get().asphyxiateAK9) {
				entity.damage(DamageSource.DROWN, 1.0F);
			}
		}
	}

	/** Returns the next oxygen level for this component,
	 * adding if positive, subtracting if negative, based
	 * on the given amount. */
	private int nextOxygen(boolean isPositive, int oxygen) {
		return isPositive ? oxygen < getMaximumOxygen() ? oxygen + 1 : getMaximumOxygen() : oxygen > getMinimumOxygen() ? oxygen - 1 : getMinimumOxygen();
	}

	/** Returns this component's oxygen amount. */
	public int getOxygen() {
		return oxygen;
	}

	/** Sets this component's oxygen amount to the specified value. */
	public void setOxygen(int oxygen) {
		this.oxygen = oxygen;
	}

	/** Returns this component's minimum oxygen amount. */
	public int getMinimumOxygen() {
		return minimumOxygen;
	}

	/** Sets this component's minimum oxygen amount to the specified value. */
	public void setMinimumOxygen(int minimumOxygen) {
		this.minimumOxygen = minimumOxygen;
	}

	/** Returns this component's maximum oxygen amount. */
	public int getMaximumOxygen() {
		return maximumOxygen;
	}

	/** Sets this component's maximum oxygen amount to the specified value. */
	public void setMaximumOxygen(int maximumOxygen) {
		this.maximumOxygen = maximumOxygen;
	}

	/** Returns this componetn's entity. */
	public Entity getEntity() {
		return entity;
	}

	/** Sets this component's entity to the specified value. */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/** Serializes this {@link FluidComponent} to a {@link CompoundTag}. */
	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("oxygen", oxygen);
	}

	/** Deserializes this {@link FluidComponent} from a {@link CompoundTag}. */
	@Override
	public void readFromNbt(CompoundTag tag) {
		this.oxygen = tag.getInt("oxygen");
	}
}
