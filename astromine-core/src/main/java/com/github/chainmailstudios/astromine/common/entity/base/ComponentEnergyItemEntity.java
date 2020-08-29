package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.ExtendedInventoryProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class ComponentEnergyItemEntity extends ComponentEntity {
	public abstract ItemInventoryComponent createItemComponent();

	private final ItemInventoryComponent itemComponent = createItemComponent();

	public abstract EnergyInventoryComponent createEnergyComponent();

	private final EnergyInventoryComponent energyComponent = createEnergyComponent();

	public ItemInventoryComponent getItemComponent() {
		return itemComponent;
	}

	public EnergyInventoryComponent getEnergyComponent() {
		return energyComponent;
	}

	public ComponentEnergyItemEntity(EntityType<?> type, World world) {
		super(type, world);
	}
}
