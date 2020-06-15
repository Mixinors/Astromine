package com.github.chainmailstudios.astromine.common.fluid.logic;

import net.minecraft.nbt.CompoundTag;

/**
 * Interface for implementation of NBT data
 * in Volumes.
 */
public interface Property {
    /**
     * Serializes this FluidProperty to a tag
     * based on a Volume.
     *
     * @return a tag
     */
    <T extends Volume> CompoundTag toTag(T volume);

    /**
     * Deserializes a FluidProperty from a tag.
     *
     * @return a FluidProperty
     */
    <T extends Property> T fromTag(CompoundTag tag);
}