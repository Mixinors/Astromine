package com.github.chainmailstudios.astromine.common.fluid.collection;

import com.github.chainmailstudios.astromine.common.fluid.logic.Volume;
import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IndexedVolumeCollection extends Iterable<Volume> {
	static IndexedVolumeCollection fromTag(CompoundTag tag) {
		IndexedVolumeCollection collection = new SimpleIndexedVolumeCollection();

		List<String> keys = Lists.newArrayList(tag.getKeys());

		Collections.sort(keys);

		for (String key : keys) {
			collection.addVolume(Volume.fromTag(tag.getCompound(key)));
		}

		return collection;
	}

	default void addVolume(Volume volume) {
		getVolumes().add(volume);
	}

	/**
	 * Position-based Volume gathering.
	 */
	default Volume getVolume(int position) {
		return getVolumes().get(position);
	}

	/**
	 * Fluid-predicate-based Volume gathering.
	 */
	default List<Volume> getVolume(Fluid fluid) {
		return this.getVolumes(volume -> volume.getFluid() == fluid);
	}

	/**
	 * Generic predicate-based Volume gathering.
	 */
	default List<Volume> getVolumes(Predicate<Volume> predicate) {
		return this.getVolumes().stream().filter(predicate).collect(Collectors.toList());
	}

	List<Volume> getVolumes();

	/**
	 * Serializes an IndexedVolumeCollection to a tag.
	 *
	 * @return a tag
	 */
	default CompoundTag toTag(CompoundTag tag) {
		for (int i = 0; i < getVolumes().size(); ++i) {
			tag.put(String.valueOf(i), getVolumes().get(i).toTag(new CompoundTag()));
		}

		return tag;
	}

	@Override
	default Iterator<Volume> iterator() {
		return getVolumes().iterator();
	}
}
