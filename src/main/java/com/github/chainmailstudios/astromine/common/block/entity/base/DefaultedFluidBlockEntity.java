package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

public abstract class DefaultedFluidBlockEntity extends DefaultedBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected final SimpleFluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);

	public DefaultedFluidBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);

		fluidComponent.dispatchConsumers();
	}
}
