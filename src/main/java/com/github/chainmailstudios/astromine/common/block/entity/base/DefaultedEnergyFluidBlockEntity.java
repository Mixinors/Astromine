package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntityType;

public abstract class DefaultedEnergyFluidBlockEntity extends DefaultedEnergyBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected SimpleFluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);

	public DefaultedEnergyFluidBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);

		fluidComponent.dispatchConsumers();
	}
}
