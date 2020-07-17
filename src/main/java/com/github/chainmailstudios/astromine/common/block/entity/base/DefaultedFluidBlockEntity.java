package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

public abstract class DefaultedFluidBlockEntity extends DefaultedBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected final FluidInventoryComponent fluidComponent = createFluidComponent();

	protected abstract FluidInventoryComponent createFluidComponent();

	public DefaultedFluidBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);
		fluidComponent.dispatchConsumers();
	}
}
