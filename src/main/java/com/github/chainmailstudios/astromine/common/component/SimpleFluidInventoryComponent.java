package com.github.chainmailstudios.astromine.common.component;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFluidInventoryComponent implements FluidInventoryComponent {
	private final Map<Integer, FluidVolume> contents = new HashMap<>();

	private final List<Runnable> listeners = new ArrayList<>();

	private final int size;

	public SimpleFluidInventoryComponent(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			contents.put(i, FluidVolume.EMPTY);
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
}
