package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleFluidInventoryComponent implements FluidInventoryComponent {
	private final Map<Integer, FluidVolume> contents = new HashMap<>();

	private final List<Runnable> listeners = new ArrayList<>();

	private final int size;

	public SimpleFluidInventoryComponent() {
		this(0);
	}

	public SimpleFluidInventoryComponent(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			contents.put(i, FluidVolume.empty());
		}
	}

	@Override
	public Map<Integer, FluidVolume> getContents() {
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

	@Override
	public SimpleFluidInventoryComponent copy() {
		SimpleFluidInventoryComponent component = new SimpleFluidInventoryComponent(getSize());
		component.fromTag(toTag(new CompoundTag()));
		return component;
	}
}
