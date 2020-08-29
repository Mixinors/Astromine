package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.ExtendedInventoryProvider;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public abstract class ComponentFluidItemEntity extends ComponentEntity {
	public abstract ItemInventoryComponent createItemComponent();

	public abstract FluidInventoryComponent createFluidComponent();

	public ItemInventoryComponent getItemComponent() {
		return ComponentProvider.fromEntity(this).getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);
	}

	public FluidInventoryComponent getFluidComponent() {
		return ComponentProvider.fromEntity(this).getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);
	}

	public ComponentFluidItemEntity(EntityType<?> type, World world) {
		super(type, world);
	}
}
