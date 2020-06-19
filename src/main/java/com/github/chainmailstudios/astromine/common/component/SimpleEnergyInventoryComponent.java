package com.github.chainmailstudios.astromine.common.component;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleEnergyInventoryComponent implements EnergyInventoryComponent {
	private final Map<Integer, EnergyVolume> contents = new HashMap<>();

	private final List<Runnable> listeners = new ArrayList<>();

	private final int size;

	public SimpleEnergyInventoryComponent(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			contents.put(i, EnergyVolume.EMPTY);
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
}
