package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import team.reborn.energy.EnergyStorage;

public interface EnergyInventoryComponent extends NameableComponent {
	default Item getSymbol() {
		return AstromineItems.ENERGY;
	}

	default TranslatableText getName() {
		return new TranslatableText("text.astromine.energy");
	}

	default EnergyStorage getStorage() {
		return ((EnergyStorage) this);
	}

	@Override
	default ComponentType<?> getComponentType() {
		return AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT;
	}

	@Override
	default void fromTag(CompoundTag compoundTag) {
	}

	@Override
	default CompoundTag toTag(CompoundTag compoundTag) {
		return compoundTag;
	}
}