package com.github.chainmailstudios.astromine.common.fluid.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class SidedVolumeCollection implements Iterable<Map.Entry<Direction, IndexedVolumeCollection>> {
	private final Map<Direction, IndexedVolumeCollection> collections;

	/**
	 * Instantiates an empty SidedIndexedVolumeCollection instance.
	 */
	public SidedVolumeCollection() {
		this.collections = Maps.newHashMap();

		for (Direction direction : Direction.values()) {
			this.collections.put(direction, new IndexedVolumeCollection());
		}
	}

	/**
	 * Deserializes a SidedVolumeCollection from a tag.
	 *
	 * @return a SidedVolumeCollection
	 */
	public static SidedVolumeCollection fromTag(CompoundTag tag) {
		SidedVolumeCollection collection = new SidedVolumeCollection();

		for (Direction direction : Direction.values()) {
			collection.collections.put(direction, IndexedVolumeCollection.fromTag(tag.getCompound(direction.asString())));
		}

		return collection;
	}

	public IndexedVolumeCollection getCollection(Direction direction) {
		return this.collections.get(direction);
	}

	public Collection<IndexedVolumeCollection> getCollections() {
		return this.collections.values();
	}

	/**
	 * Serializes a SidedVolumeCollection to a tag.
	 *
	 * @return a tag
	 */
	public CompoundTag toTag(CompoundTag tag) {
		for (Map.Entry<Direction, IndexedVolumeCollection> entry : this.collections.entrySet()) {
			tag.put(entry.getKey().asString(), entry.getValue().toTag(new CompoundTag()));
		}

		return tag;
	}

	@Override
	public Iterator<Map.Entry<Direction, IndexedVolumeCollection>> iterator() {
		return this.collections.entrySet().iterator();
	}
}
