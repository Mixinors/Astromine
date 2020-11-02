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

import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

public class EntityOxygenComponent implements Component {
	int oxygen = 0;

	int minimumOxygen = -20;
	int maximumOxygen = -180;

	Entity entity;

	public EntityOxygenComponent(Entity entity) {
		this.entity = entity;
	}

	public static EntityOxygenComponent defaulted(Entity entity) {
		return new EntityOxygenComponent(entity);
	}

	@Nullable
	public static <V> EntityOxygenComponent get(V v) {
		try {
			return AstromineComponents.ENTITY_OXYGEN_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		this.oxygen = tag.getInt("oxygen");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("oxygen", oxygen);
	}

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

	private int nextOxygen(boolean isPositive, int oxygen) {
		return isPositive ? oxygen < getMaximumOxygen() ? oxygen + 1 : getMaximumOxygen() : oxygen > getMinimumOxygen() ? oxygen - 1 : getMinimumOxygen();
	}

	public int getOxygen() {
		return oxygen;
	}

	public void setOxygen(int oxygen) {
		this.oxygen = oxygen;
	}

	public int getMinimumOxygen() {
		return minimumOxygen;
	}

	public void setMinimumOxygen(int minimumOxygen) {
		this.minimumOxygen = minimumOxygen;
	}

	public int getMaximumOxygen() {
		return maximumOxygen;
	}

	public void setMaximumOxygen(int maximumOxygen) {
		this.maximumOxygen = maximumOxygen;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
