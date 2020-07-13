package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

public abstract class DefaultedEnergyBlockEntity extends DefaultedBlockEntity implements ComponentProvider {
	protected final SimpleEnergyInventoryComponent energyComponent = new SimpleEnergyInventoryComponent(1);

	public DefaultedEnergyBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, energyComponent);

		energyComponent.dispatchConsumers();
	}
}
