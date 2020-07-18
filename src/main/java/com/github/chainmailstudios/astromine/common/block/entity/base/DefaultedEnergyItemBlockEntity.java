package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

public abstract class DefaultedEnergyItemBlockEntity extends DefaultedEnergyBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected final ItemInventoryComponent itemComponent = createItemComponent();

	protected abstract ItemInventoryComponent createItemComponent();

	public DefaultedEnergyItemBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, itemComponent);
	}
}
