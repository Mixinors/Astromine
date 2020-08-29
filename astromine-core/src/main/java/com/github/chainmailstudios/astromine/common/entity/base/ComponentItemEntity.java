package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.ExtendedInventoryProvider;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public abstract class ComponentItemEntity extends ComponentEntity {
	public abstract ItemInventoryComponent createItemComponent();

	private final ItemInventoryComponent itemComponent = createItemComponent();

	public ItemInventoryComponent getItemComponent() {
		return itemComponent;
	}

	public ComponentItemEntity(EntityType<?> type, World world) {
		super(type, world);
	}
}
