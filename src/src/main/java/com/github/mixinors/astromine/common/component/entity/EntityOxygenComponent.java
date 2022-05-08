package com.github.mixinors.astromine.common.component.entity;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.registry.common.AMComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;


public final class EntityOxygenComponent implements AutoSyncedComponent {
	private int oxygen = 0;
	
	private int minimumOxygen = -20;
	private int maximumOxygen = 180;
	
	private Entity entity;
	
	private EntityOxygenComponent(Entity entity) {
		this.entity = entity;
	}
	
	@Nullable
	public static <V> EntityOxygenComponent get(V v) {
		try {
			return AMComponents.ENTITY_OXYGEN_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
	
	public static EntityOxygenComponent of(Entity entity) {
		return new EntityOxygenComponent(entity);
	}
	
	public void simulate(boolean isBreathing) {
		if (entity instanceof PlayerEntity player) {
			
			if (player.isCreative() || player.isSpectator()) {
				return;
			}
		}
		
		oxygen = nextOxygen(isBreathing, oxygen);
		
		if (oxygen == getMinimumOxygen()) {
			var isAK9 = false;
			
			if (entity instanceof PlayerEntity) {
				isAK9 = ((PlayerEntity) entity).getGameProfile().getId().toString().equals("38113444-0bc0-4502-9a4c-17903067907c");
			}
			
			if (!isAK9 || AMConfig.get().secret.asphyxiateAK9) {
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
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("Oxygen", oxygen);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.oxygen = tag.getInt("Oxygen");
	}
}
