package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleEnergyInventoryComponent implements EnergyInventoryComponent {
	private final Map<Integer, EnergyVolume> contents = new HashMap<>();

	private final List<Runnable> listeners = new ArrayList<>();

	private final int size;

	public SimpleEnergyInventoryComponent(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			contents.put(i, EnergyVolume.empty());
		}
	}

	public Map<Integer, EnergyVolume> getContents() {
		return contents;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public List<Runnable> getListeners() {
		return listeners;
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {
		read(this, compoundTag, Optional.empty(), Optional.empty());
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		write(this, compoundTag, Optional.empty(), Optional.empty());
		return compoundTag;
	}
}
