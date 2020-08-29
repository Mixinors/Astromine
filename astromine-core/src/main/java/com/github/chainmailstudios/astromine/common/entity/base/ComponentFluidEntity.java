package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.ExtendedInventoryProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public abstract class ComponentFluidEntity extends ComponentEntity {
	public abstract FluidInventoryComponent createFluidComponent();

	private final FluidInventoryComponent fluidComponent = createFluidComponent();

	public FluidInventoryComponent getFluidComponent() {
		return fluidComponent;
	}

	public ComponentFluidEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {

	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {

	}
}
