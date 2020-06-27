package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

public abstract class DefaultedEnergyFluidBlockEntity extends DefaultedBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected SimpleEnergyInventoryComponent energyComponent = new SimpleEnergyInventoryComponent(1);
	protected SimpleFluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);

	public DefaultedEnergyFluidBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, energyComponent);
		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);
	}
}
