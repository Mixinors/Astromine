package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.ExtendedInventoryProvider;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public abstract class ComponentFluidEntity extends ComponentEntity {
	public abstract FluidInventoryComponent createFluidComponent();

	public FluidInventoryComponent getFluidComponent() {
		return ComponentProvider.fromEntity(this).getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);
	}

	public ComponentFluidEntity(EntityType<?> type, World world) {
		super(type, world);
	}
}
