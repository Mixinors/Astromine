package com.github.chainmailstudios.astromine.common.volume.collection;

import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

import java.util.HashMap;
import java.util.Map;

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
