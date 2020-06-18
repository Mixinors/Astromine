package com.github.chainmailstudios.astromine.common.volume.collection;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

import java.util.ArrayList;
import java.util.List;

public class SimpleIndexedVolumeCollection<T extends BaseVolume> implements IndexedVolumeCollection<T> {
	private final List<T> fluidVolumes = new ArrayList<>();

	@Override
	public List<T> getVolumes() {
		return this.fluidVolumes;
	}
}
