package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class ComponentEnergyFluidEntity extends ComponentEntity {
	public abstract FluidInventoryComponent createFluidComponent();

	public abstract EnergyInventoryComponent createEnergyComponent();

	public FluidInventoryComponent getFluidComponent() {
		return ComponentProvider.fromEntity(this).getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);
	}

	public EnergyInventoryComponent getEnergyComponent() {
		return ComponentProvider.fromEntity(this).getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
	}

	public ComponentEnergyFluidEntity(EntityType<?> type, World world) {
		super(type, world);
	}
}
