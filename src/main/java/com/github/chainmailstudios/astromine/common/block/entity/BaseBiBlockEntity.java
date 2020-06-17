package com.github.chainmailstudios.astromine.common.block.entity;

import java.util.Collections;
import java.util.List;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import com.github.chainmailstudios.astromine.common.volume.collection.AgnosticSidedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.collection.IndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.collection.SimpleIndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public abstract class BaseBiBlockEntity extends BlockEntity implements AgnosticSidedVolumeCollection {
	private SimpleIndexedVolumeCollection<FluidVolume> fluidVolumeCollection = new SimpleIndexedVolumeCollection();
	private SimpleIndexedVolumeCollection<EnergyVolume> energyVolumeCollection = new SimpleIndexedVolumeCollection();

	public BaseBiBlockEntity(BlockEntityType<?> type) {
		super(type);

		this.fluidVolumeCollection.addVolume(new FluidVolume());
		this.energyVolumeCollection.addVolume(new EnergyVolume());
	}

	@Override
	public boolean contains(Direction direction, int volumeType) {
		Direction facing = this.getCachedState().get(FacingBlock.FACING);

		return (volumeType == EnergyVolume.TYPE || volumeType == FluidVolume.TYPE) && direction != facing;
	}

	@Override
	public <U extends BaseVolume, T extends IndexedVolumeCollection<U>> T get(Direction direction, int volumeType) {
		Direction facing = this.getCachedState().get(FacingBlock.FACING);

		return direction != facing ? volumeType == EnergyVolume.TYPE ? (T) this.energyVolumeCollection : volumeType == FluidVolume.TYPE ? (T) this.fluidVolumeCollection : null : null;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		IndexedVolumeCollection<EnergyVolume> energyCollection = new SimpleIndexedVolumeCollection();

		List<String> energyKeys = Lists.newArrayList(tag.getCompound("energy").getKeys());

		Collections.sort(energyKeys);

		for (String key : energyKeys) {
			energyCollection.addVolume(EnergyVolume.fromTag(tag.getCompound(key)));
		}

		this.energyVolumeCollection = (SimpleIndexedVolumeCollection<EnergyVolume>) energyCollection;

		IndexedVolumeCollection<FluidVolume> fluidCollection = new SimpleIndexedVolumeCollection();

		List<String> fluidKeys = Lists.newArrayList(tag.getCompound("fluid").getKeys());

		Collections.sort(fluidKeys);

		for (String key : fluidKeys) {
			fluidCollection.addVolume(FluidVolume.fromTag(tag.getCompound(key)));
		}

		this.fluidVolumeCollection = (SimpleIndexedVolumeCollection<FluidVolume>) fluidCollection;

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("energy", this.energyVolumeCollection.toTag(new CompoundTag()));
		tag.put("fluid", this.fluidVolumeCollection.toTag(new CompoundTag()));

		return super.toTag(tag);
	}
}
