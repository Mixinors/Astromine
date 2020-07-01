package com.github.chainmailstudios.astromine.common.volume.energy;

import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

public class EnergyVolume extends BaseVolume {
	private Runnable runnable;

	public EnergyVolume() {
		super();
	}

	public EnergyVolume(Fraction fraction) {
		this.fraction = fraction;
	}

	public EnergyVolume(Fraction fraction, Runnable runnable) {
		super(fraction);
		this.runnable = runnable;
	}

	public static EnergyVolume attached(SimpleEnergyInventoryComponent component) {
		return new EnergyVolume(Fraction.empty(), component::dispatchConsumers);
	}

	public static EnergyVolume empty() {
		return new EnergyVolume();
	}

	public static EnergyVolume of(Fraction fraction) {
		return new EnergyVolume(fraction);
	}

	@Override
	public void setFraction(Fraction fraction) {
		super.setFraction(fraction);
		if (runnable != null) runnable.run();
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
