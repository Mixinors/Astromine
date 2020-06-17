package com.github.chainmailstudios.astromine.common.volume.collection;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;

public class SimpleSidedVolumeCollection<T extends BaseVolume> implements SidedVolumeCollection<T> {
	private final Map<Direction, IndexedVolumeCollection<T>> collections = new HashMap<>();

	public SimpleSidedVolumeCollection() {
		for (Direction direction : Direction.values()) {
			collections.put(direction, new SimpleIndexedVolumeCollection());
		}
	}

	@Override
	public Map<Direction, IndexedVolumeCollection<T>> getCollections() {
		return collections;
	}
}
