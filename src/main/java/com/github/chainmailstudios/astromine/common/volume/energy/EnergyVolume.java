package com.github.chainmailstudios.astromine.common.volume.energy;

import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

public class EnergyVolume extends BaseVolume {
	public EnergyVolume() {
		super();
	}

	public EnergyVolume(Fraction fraction) {
		this.fraction = fraction;
	}

	public static EnergyVolume empty() {
		return new EnergyVolume();
	}

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static EnergyVolume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		EnergyVolume energyVolume = new EnergyVolume();

		if (!tag.contains("fraction")) {
			energyVolume.fraction = Fraction.empty();
		} else {
			energyVolume.fraction = Fraction.fromTag(tag.getCompound("fraction"));
		}

		if (!tag.contains("size")) {
			energyVolume.size = Fraction.BUCKET;
		} else {
			energyVolume.size = Fraction.fromTag(tag.getCompound("size"));
		}

		return energyVolume;
	}

	public EnergyVolume copy() {
		return new EnergyVolume(fraction);
	}
}
