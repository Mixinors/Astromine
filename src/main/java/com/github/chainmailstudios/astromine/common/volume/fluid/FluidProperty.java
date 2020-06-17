package com.github.chainmailstudios.astromine.common.volume.fluid;

import net.minecraft.nbt.CompoundTag;

/**
 * Interface for implementation of NBT data
 * in Volumes.
 */
public interface FluidProperty {
	/**
	 * Serializes this FluidProperty to a tag
	 * based on a Volume.
	 *
	 * @return a tag
	 */
	<T extends FluidVolume> CompoundTag toTag(T volume);

	/**
	 * Deserializes a FluidProperty from a tag.
	 *
	 * @return a FluidProperty
	 */
	<T extends FluidProperty> T fromTag(CompoundTag tag);
}