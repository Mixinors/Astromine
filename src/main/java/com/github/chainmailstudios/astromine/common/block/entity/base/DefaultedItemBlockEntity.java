package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public abstract class DefaultedItemBlockEntity extends DefaultedBlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected final SimpleItemInventoryComponent itemComponent = new SimpleItemInventoryComponent(1);

	public DefaultedItemBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public <T extends Component> Collection<T> getSidedComponents(Direction direction) {
		if (direction == null) {
			return (Collection<T>) Lists.newArrayList(itemComponent);
		} else if (getCachedState().getBlock() instanceof FacingBlock) {
			Direction facing = getCachedState().get(FacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(itemComponent);
		} else if (getCachedState().getBlock() instanceof HorizontalFacingBlock) {
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(itemComponent);
		} else {
			return (Collection<T>) Lists.newArrayList(itemComponent);
		}
	}

	@Override
	public boolean hasComponent(ComponentType<?> type) {
		return type == AstromineComponentTypes.ITEM_INVENTORY_COMPONENT ? true : false;
	}

	@Override
	public <C extends Component> C getComponent(ComponentType<C> type) {
		return type == AstromineComponentTypes.ITEM_INVENTORY_COMPONENT ? (C) itemComponent : null;
	}

	@Override
	public Set<ComponentType<?>> getComponentTypes() {
		return Sets.newHashSet(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("energy", itemComponent.write(itemComponent, Optional.empty(), Optional.empty()));

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		itemComponent.read(itemComponent, tag.getCompound("energy"), Optional.empty(), Optional.empty());

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
