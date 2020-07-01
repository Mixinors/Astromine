package com.github.chainmailstudios.astromine.common.component.entity;

import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public class EntityOxygenComponent implements Component {
	int oxygen;

	int minimumOxygen = -20;
	int maximumOxygen = -180;

	Entity entity;

	public EntityOxygenComponent(int oxygen, Entity entity) {
		this.oxygen = oxygen;
		this.entity = entity;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.oxygen = tag.getInt("oxygen");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("oxygen", oxygen);
		return tag;
	}

	public void simulate(FluidVolume atmosphereVolume) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;

			if (player.isCreative() || player.isSpectator()) {
				return;
			}
		}

		if (BreathableRegistry.INSTANCE.containsKey(entity.getType())) {
			if (!BreathableRegistry.INSTANCE.canBreathe(entity.getType(), atmosphereVolume.getFluid())) {
				oxygen = nextOxygen(false, oxygen);
			} else {
				oxygen = nextOxygen(true, oxygen);
			}

			if (oxygen == getMinimumOxygen()) {
				entity.damage(DamageSource.GENERIC, 1.0F);
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
