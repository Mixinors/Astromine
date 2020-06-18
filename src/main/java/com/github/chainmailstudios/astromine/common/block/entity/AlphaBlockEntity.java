package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import com.github.chainmailstudios.astromine.common.volume.collection.AgnosticSidedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public abstract class AlphaBlockEntity extends BlockEntity implements AgnosticSidedVolumeCollection {
	private FluidVolume fluidVolume = new FluidVolume();
	private EnergyVolume energyVolume = new EnergyVolume();

	public AlphaBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public boolean contains(Direction direction, int volumeType) {
		Direction facing = getCachedState().get(FacingBlock.FACING);

		return (volumeType == EnergyVolume.TYPE || volumeType == FluidVolume.TYPE) && direction != facing;
	}

	@Override
	public <T extends BaseVolume> T get(Direction direction, int volumeType) {
		Direction facing = getCachedState().get(FacingBlock.FACING);

		return direction != facing ? volumeType == EnergyVolume.TYPE ? (T) energyVolume : volumeType == FluidVolume.TYPE ? (T) fluidVolume : null : null;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("energy", energyVolume.toTag(new CompoundTag()));
		tag.put("fluid", fluidVolume.toTag(new CompoundTag()));

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		this.energyVolume = EnergyVolume.fromTag(tag.getCompound("energy"));
		this.fluidVolume = FluidVolume.fromTag(tag.getCompound("fluid"));;

		super.fromTag(state, tag);
	}
}
