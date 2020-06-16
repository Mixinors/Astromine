package com.github.chainmailstudios.astromine.common.fluid.collection;

import com.github.chainmailstudios.astromine.common.fluid.logic.Volume;

import java.util.ArrayList;
import java.util.List;

public class SimpleIndexedVolumeCollection implements IndexedVolumeCollection {
	private final List<Volume> volumes = new ArrayList<>();

	@Override
	public List<Volume> getVolumes() {
		return volumes;
	}
}
