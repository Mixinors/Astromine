package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public abstract class DefaultedEnergyItemBlockEntity extends BlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected SimpleEnergyInventoryComponent energyComponent = new SimpleEnergyInventoryComponent(1);
	protected SimpleItemInventoryComponent inventoryComponent = new SimpleItemInventoryComponent(1);

	public DefaultedEnergyItemBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public <T extends Component> Collection<T> getSidedComponents(@Nullable Direction direction) {
		if (direction == null) {
			return (Collection<T>) Lists.newArrayList(energyComponent, inventoryComponent);
		} else if (getCachedState().getBlock() instanceof FacingBlock) {
			Direction facing = getCachedState().get(FacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(energyComponent, inventoryComponent);
		} else if (getCachedState().getBlock() instanceof HorizontalFacingBlock) {
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(energyComponent, inventoryComponent);
		} else {
			return (Collection<T>) Lists.newArrayList(energyComponent, inventoryComponent);
		}
	}

	@Override
	public boolean hasComponent(ComponentType<?> type) {
		return type == AstromineComponentTypes.ITEM_INVENTORY_COMPONENT || type == AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT ? true : false;
	}

	@Override
	public <C extends Component> C getComponent(ComponentType<C> type) {
		return type == AstromineComponentTypes.ITEM_INVENTORY_COMPONENT ? (C) inventoryComponent : type == AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT ? (C) energyComponent : null;
	}

	@Override
	public Set<ComponentType<?>> getComponentTypes() {
		return Sets.newHashSet(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("energy", energyComponent.write(energyComponent, Optional.empty(), Optional.empty()));
		tag.put("item", inventoryComponent.write(inventoryComponent, Optional.empty(), Optional.empty()));

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		energyComponent.read(energyComponent, tag.getCompound("energy"), Optional.empty(), Optional.empty());
		inventoryComponent.read(inventoryComponent, tag.getCompound("item"), Optional.empty(), Optional.empty());

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(null, tag);
	}
}
