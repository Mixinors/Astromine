package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public abstract class DefaultedEnergyItemBlockEntity extends DefaultedBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected SimpleEnergyInventoryComponent energyComponent = new SimpleEnergyInventoryComponent(1);
	protected SimpleItemInventoryComponent itemComponent = new SimpleItemInventoryComponent(1);

	public DefaultedEnergyItemBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, energyComponent);
		addComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, itemComponent);
	}
}
