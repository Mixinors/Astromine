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

package com.github.mixinors.astromine.common.component.entity;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.registry.common.AMComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public final class OxygenComponent implements AutoSyncedComponent {
	private static final UUID AK9_UUID = UUID.fromString("38113444-0bc0-4502-9a4c-17903067907c");
	
	public static final String OXYGEN_KEY = "Oxygen";
	
	private int oxygen = 0;
	
	private int minimumOxygen = -20;
	private int maximumOxygen = 180;
	
	private Entity entity;
	
	@Nullable
	public static <V> OxygenComponent get(V v) {
		try {
			return AMComponents.ENTITY_OXYGEN_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
	
	public OxygenComponent(Entity entity) {
		this.entity = entity;
	}
	
	public void tick(boolean breathing) {
		if (entity instanceof PlayerEntity player) {
			if (player.isCreative() || player.isSpectator()) {
				return;
			}
		}
		
		oxygen = nextOxygen(breathing, oxygen);
		
		if (oxygen == getMinimumOxygen()) {
			var isAK9 = false;
			
			if (entity instanceof PlayerEntity playerEntity) {
				isAK9 = playerEntity.getGameProfile().getId().equals(AK9_UUID);
			}
			
			if (!isAK9 || AMConfig.get().secret.asphyxiateAK9) {
				entity.damage(DamageSource.DROWN, 1.0F);
			}
		}
	}
	
	private int nextOxygen(boolean breathing, int oxygen) {
		return breathing ? oxygen < getMaximumOxygen() ? oxygen + 1 : getMaximumOxygen() : oxygen > getMinimumOxygen() ? oxygen - 1 : getMinimumOxygen();
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
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt(OXYGEN_KEY, oxygen);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		oxygen = nbt.getInt(OXYGEN_KEY);
	}
}
