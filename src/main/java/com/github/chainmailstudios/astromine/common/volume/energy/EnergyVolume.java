package com.github.chainmailstudios.astromine.common.volume.energy;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

import net.minecraft.nbt.CompoundTag;

public class EnergyVolume extends BaseVolume {
	public static final int TYPE = 1;

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static EnergyVolume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		EnergyVolume volume = new EnergyVolume();

		volume.fraction = Fraction.fromTag(tag.getCompound("fraction"));

		return volume;
	}
}
