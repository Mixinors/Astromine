package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntityType;

public abstract class DefaultedEnergyItemBlockEntity extends DefaultedEnergyBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected SimpleItemInventoryComponent itemComponent = new SimpleItemInventoryComponent(1);

	public DefaultedEnergyItemBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, itemComponent);
	}
}
