package com.github.chainmailstudios.astromine.common.fluid.collection;

import com.github.chainmailstudios.astromine.common.fluid.logic.Volume;
import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IndexedVolumeCollection implements Iterable<Volume> {
	private final List<Volume> volumes;

	/**
	 * Instantiates an empty IndexedVolumeCollection instance.
	 */
	public IndexedVolumeCollection() {
		volumes = Lists.newArrayList();
	}

	/**
	 * Instantiates an empty-filled IndexedVolumeCollection instance.
	 */
	public IndexedVolumeCollection(int size) {
		this.volumes = Lists.newArrayList();
		for (int i = 0; i < size; ++i) {
			volumes.add(new Volume());
		}
	}

	/**
	 * Instantiates a IndexedVolumeCollection based on a pre-existing volume array.
	 */
	public IndexedVolumeCollection(Volume... volumes) {
		this.volumes = Arrays.asList(volumes);
	}

	/**
	 * Instantiates a IndexedVolumeCollection based on a pre-existing volume iterable.
	 */
	public IndexedVolumeCollection(Iterable<Volume> volumes) {
		this.volumes = Lists.newArrayList(volumes);
	}

	public void addVolume(Volume volume) {
		volumes.add(volume);
	}

	/**
	 * Position-based Volume gathering.
	 */
	public Volume getVolume(int position) {
		return volumes.get(position);
	}

	public List<Volume> getVolumes() {
		return volumes;
	}

	/**
	 * Generic predicate-based Volume gathering.
	 */
	public List<Volume> getVolumes(Predicate<Volume> predicate) {
		return getVolumes().stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * Fluid-predicate-based Volume gathering.
	 */
	public List<Volume> getVolume(Fluid fluid) {
		return getVolumes(volume -> volume.getFluid() == fluid);
	}

	/**
	 * Serializes an IndexedVolumeCollection to a tag.
	 *
	 * @return a tag
	 */
	public CompoundTag toTag(CompoundTag tag) {
		for (int i = 0; i < volumes.size(); ++i) {
			tag.put(String.valueOf(i), volumes.get(i).toTag(new CompoundTag()));
		}

		return tag;
	}

	/**
	 * Deserialize an IndexedVolumeCollection from a tag.
	 * Collection is sorted to preserve Volume order.
	 *
	 * @return an IndexedVolumeCollection
	 */
	public static IndexedVolumeCollection fromTag(CompoundTag tag) {
		IndexedVolumeCollection collection = new IndexedVolumeCollection();

		List<String> keys = Lists.newArrayList(tag.getKeys());

		Collections.sort(keys);

		for (String key : keys) {
			collection.addVolume(Volume.fromTag(tag.getCompound(key)));
		}

		return collection;
	}

	@Override
	public Iterator<Volume> iterator() {
		return volumes.iterator();
	}
}
