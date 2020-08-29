package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class ComponentEnergyFluidEntity extends ComponentEntity {
	public abstract FluidInventoryComponent createFluidComponent();

	private final FluidInventoryComponent fluidComponent = createFluidComponent();

	public abstract EnergyInventoryComponent createEnergyComponent();

	private EnergyInventoryComponent energyComponent = createEnergyComponent();

	public final FluidInventoryComponent getFluidComponent() {
		return fluidComponent;
	}

	public EnergyInventoryComponent getEnergyComponent() {
		return energyComponent;
	}

	public ComponentEnergyFluidEntity(EntityType<?> type, World world) {
		super(type, world);
	}
}
