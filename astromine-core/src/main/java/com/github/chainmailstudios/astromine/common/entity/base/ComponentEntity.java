package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class ComponentEntity extends Entity {
	public ComponentEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {

	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {

	}
}
