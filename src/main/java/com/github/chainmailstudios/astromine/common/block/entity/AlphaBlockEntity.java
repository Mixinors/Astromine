package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.component.Component;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.SimpleFluidInventoryComponent;
import com.google.common.collect.Lists;
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

public abstract class AlphaBlockEntity extends BlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	protected final SimpleEnergyInventoryComponent energyComponent = new SimpleEnergyInventoryComponent(1);
	protected final SimpleFluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);

	public AlphaBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public <T extends Component> Collection<T> getComponents(@Nullable Direction direction) {
		if (direction == null) {
			return (Collection<T>) Lists.newArrayList(energyComponent, fluidComponent);
		} else if (getCachedState().getBlock() instanceof FacingBlock) {
			Direction facing = getCachedState().get(FacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(energyComponent, fluidComponent);
		} else if (getCachedState().getBlock() instanceof HorizontalFacingBlock) {
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);
			return facing == direction ? Lists.newArrayList() : (Collection<T>) Lists.newArrayList(energyComponent, fluidComponent);
		} else {
			return (Collection<T>) Lists.newArrayList(energyComponent, fluidComponent);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("energy", energyComponent.write(energyComponent, Optional.empty(), Optional.empty()));
		tag.put("fluid", fluidComponent.write(fluidComponent, Optional.empty(), Optional.empty()));

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		energyComponent.read(energyComponent, tag.getCompound("energy"), Optional.empty(), Optional.empty());
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
