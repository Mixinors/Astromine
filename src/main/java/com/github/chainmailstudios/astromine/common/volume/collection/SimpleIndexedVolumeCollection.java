package com.github.chainmailstudios.astromine.common.volume.collection;

import java.util.ArrayList;
import java.util.List;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

public class SimpleIndexedVolumeCollection<T extends BaseVolume> implements IndexedVolumeCollection<T> {
	private final List<T> fluidVolumes = new ArrayList<>();

	@Override
	public List<T> getVolumes() {
		return this.fluidVolumes;
	}
}
