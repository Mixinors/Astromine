package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
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

public abstract class DefaultedFluidBlockEntity extends BlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected final SimpleFluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);

	public DefaultedFluidBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public <T extends Component> Collection<T> getSidedComponents(@Nullable Direction direction) {
		if (direction == null) {
			return (Collection<T>) Lists.newArrayList(fluidComponent);
		} else if (getCachedState().getBlock() instanceof FacingBlock) {
			Direction facing = getCachedState().get(FacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(fluidComponent);
		} else if (getCachedState().getBlock() instanceof HorizontalFacingBlock) {
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(fluidComponent);
		} else {
			return (Collection<T>) Lists.newArrayList(fluidComponent);
		}
	}

	@Override
	public boolean hasComponent(ComponentType<?> type) {
		return type == AstromineComponentTypes.FLUID_INVENTORY_COMPONENT ? true : false;
	}

	@Override
	public <C extends Component> C getComponent(ComponentType<C> type) {
		return type == AstromineComponentTypes.FLUID_INVENTORY_COMPONENT ? (C) fluidComponent : null;
	}

	@Override
	public Set<ComponentType<?>> getComponentTypes() {
		return Sets.newHashSet(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("fluid", fluidComponent.write(fluidComponent, Optional.empty(), Optional.empty()));

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		fluidComponent.read(fluidComponent, tag.getCompound("fluid"), Optional.empty(), Optional.empty());

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
