package com.github.chainmailstudios.astromine.common.fluid.collection;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public interface SidedVolumeCollection extends Iterable<Map.Entry<Direction, IndexedVolumeCollection>> {
	/**
	 * Deserializes a SidedVolumeCollection from a tag.
	 *
	 * @return a SidedVolumeCollection
	 */
	static SidedVolumeCollection fromTag(CompoundTag tag) {
		SidedVolumeCollection collection = new SimpleSidedVolumeCollection();

		for (Direction direction : Direction.values()) {
			collection.getCollections().put(direction, IndexedVolumeCollection.fromTag(tag.getCompound(direction.asString())));
		}

		return collection;
	}
	
	Map<Direction, IndexedVolumeCollection> getCollections();

	default IndexedVolumeCollection getCollection(Direction direction) {
		return getCollections().get(direction);
	}

	/**
	 * Serializes a SidedVolumeCollection to a tag.
	 *
	 * @return a tag
	 */
	default CompoundTag toTag(CompoundTag tag) {
		for (Map.Entry<Direction, IndexedVolumeCollection> entry : getCollections().entrySet()) {
			tag.put(entry.getKey().asString(), entry.getValue().toTag(new CompoundTag()));
		}

		return tag;
	}

	@Override
	default Iterator<Map.Entry<Direction, IndexedVolumeCollection>> iterator() {
		return getCollections().entrySet().iterator();
	}
}
