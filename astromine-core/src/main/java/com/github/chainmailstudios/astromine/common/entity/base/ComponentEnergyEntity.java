package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.ExtendedInventoryProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class ComponentEnergyEntity extends ComponentEntity {
	public abstract EnergyInventoryComponent createEnergyComponent();

	private final EnergyInventoryComponent energyComponent = createEnergyComponent();

	public EnergyInventoryComponent getEnergyComponent() {
		return energyComponent;
	}

	public ComponentEnergyEntity(EntityType<?> type, World world) {
		super(type, world);
	}
}
