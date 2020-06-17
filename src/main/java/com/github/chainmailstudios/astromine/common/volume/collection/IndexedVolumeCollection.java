package com.github.chainmailstudios.astromine.common.volume.collection;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

import net.minecraft.nbt.CompoundTag;

public interface IndexedVolumeCollection<T extends BaseVolume> extends Iterable<T> {
	/**
	 * An example implementation of a fromTag method.
	 */
	//static IndexedVolumeCollection fromTag(CompoundTag tag) {
	//	IndexedVolumeCollection collection = new SimpleIndexedVolumeCollection();
	//
	//	List<String> keys = Lists.newArrayList(tag.getKeys());
	//
	//	Collections.sort(keys);
	//
	//	for (String key : keys) {
	//		collection.addVolume(BaseVolume.fromTag(tag.getCompound(key))); // Importantly, you must change
	//	}
	//
	//	return collection;
	//}
	default void addVolume(T fluidVolume) {
		this.getVolumes().add(fluidVolume);
	}

	List<T> getVolumes();

	/**
	 * Position-based Volume gathering.
	 */
	default T getVolume(int position) {
		return this.getVolumes().get(position);
	}

	/**
	 * Generic predicate-based Volume gathering.
	 */
	default List<T> getVolumes(Predicate<T> predicate) {
		return this.getVolumes().stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * Serializes an IndexedVolumeCollection to a tag.
	 *
	 * @return a tag
	 */
	default CompoundTag toTag(CompoundTag tag) {
		for (int i = 0; i < this.getVolumes().size(); ++i) {
			tag.put(String.valueOf(i), this.getVolumes().get(i).toTag(new CompoundTag()));
		}

		return tag;
	}

	@Override
	default Iterator<T> iterator() {
		return this.getVolumes().iterator();
	}
}
