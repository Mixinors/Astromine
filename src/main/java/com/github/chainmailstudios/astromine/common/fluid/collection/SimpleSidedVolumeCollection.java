package com.github.chainmailstudios.astromine.common.fluid.collection;

import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;

public class SimpleSidedVolumeCollection implements SidedVolumeCollection {
	private final Map<Direction, IndexedVolumeCollection> collections = new HashMap<>();

	public SimpleSidedVolumeCollection() {
		for (Direction direction : Direction.values()) {
			collections.put(direction, new SimpleIndexedVolumeCollection());
		}
	}

	@Override
	public Map<Direction, IndexedVolumeCollection> getCollections() {
		return collections;
	}
}
