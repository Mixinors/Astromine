package com.github.chainmailstudios.astromine.common.volume.energy;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.PropertyRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnergyVolume extends BaseVolume {
	public static final int TYPE = 1;

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static EnergyVolume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		EnergyVolume energyVolume = new EnergyVolume();

		if (!tag.contains("fraction")) {
			energyVolume.fraction = Fraction.EMPTY;
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
}
