package com.github.chainmailstudios.astromine.common.volume.collection;

import java.util.HashMap;
import java.util.Map;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

import net.minecraft.util.math.Direction;

public class SimpleSidedVolumeCollection<T extends BaseVolume> implements SidedVolumeCollection<T> {
	private final Map<Direction, IndexedVolumeCollection<T>> collections = new HashMap<>();

	public SimpleSidedVolumeCollection() {
		for (Direction direction : Direction.values()) {
			this.collections.put(direction, new SimpleIndexedVolumeCollection());
		}
	}

	@Override
	public Map<Direction, IndexedVolumeCollection<T>> getCollections() {
		return this.collections;
	}
}
