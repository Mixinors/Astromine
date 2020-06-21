package com.github.chainmailstudios.astromine.common.volume.fluid;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.google.common.base.Objects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidVolume extends BaseVolume {
	private Fluid fluid = Fluids.EMPTY;

	public static final FluidVolume EMPTY = new FluidVolume();

	public static final FluidVolume DEFAULT = new FluidVolume(AstromineFluids.OXYGEN, Fraction.BUCKET);

	/**
	 * Instantiates a Volume with an empty fraction and fluid.
	 */
	public FluidVolume() {
	}

	/**
	 * Instantiates a Volume with an empty fraction and specified fluid.
	 */
	public FluidVolume(Fluid fluid) {
		this.fluid = fluid;
	}

	/**
	 * Instantiates a Volume with an specified fraction and fluid.
	 */
	public FluidVolume(Fluid fluid, Fraction fraction) {
		this.fluid = fluid;
		this.fraction = fraction;
	}

	/**
	 * Deserializes a Volume from a tag.
	 *
	 * @return a Volume
	 */
	public static FluidVolume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		FluidVolume fluidVolume = new FluidVolume(Fluids.EMPTY);

		if (!tag.contains("fluid")) {
			fluidVolume.fluid = AstromineFluids.OXYGEN;
		} else {
			fluidVolume.fluid = Registry.FLUID.get(new Identifier(tag.getString("fluid")));
		}

		if (!tag.contains("fraction")) {
			fluidVolume.fraction = Fraction.EMPTY;
		} else {
			fluidVolume.fraction = Fraction.fromTag(tag.getCompound("fraction"));
		}

		if (!tag.contains("size")) {
			fluidVolume.size = Fraction.BUCKET;
		} else {
			fluidVolume.size = Fraction.fromTag(tag.getCompound("size"));
		}

		return fluidVolume;
	}

	/**
	 * Serializes this Volume and its properties
	 * into a tag.
	 *
	 * @return a tag
	 */
	public CompoundTag toTag(CompoundTag tag) {
		tag.putString("fluid", Registry.FLUID.getId(this.fluid).toString());
		tag.put("fraction", this.fraction.toTag(new CompoundTag()));
		tag.put("size", this.size.toTag(new CompoundTag()));

		return tag;
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public boolean canInsert(Fluid fluid, Fraction amount) {
		return (this.fluid == Fluids.EMPTY || fluid == this.fluid) && hasAvailable(amount);
	}

	public boolean canExtract(Fluid fluid, Fraction amount) {
		return (this.fluid != Fluids.EMPTY || fluid == this.fluid) && hasStored(amount);
	}

	public <T extends BaseVolume> T insertVolume(Fluid fluid, Fraction fraction) {
		if (this.fluid != Fluids.EMPTY && fluid != this.fluid) return (T) FluidVolume.EMPTY;

		FluidVolume volume = super.insertVolume(fraction);
		volume.setFluid(fluid);

		if (this.fluid == Fluids.EMPTY) this.fluid = fluid;

		return (T) volume;
	}

	public <T extends BaseVolume> T extractVolume(Fluid fluid, Fraction fraction) {
		if (fluid != this.fluid) return (T) FluidVolume.EMPTY;

		FluidVolume volume = super.extractVolume(fraction);
		volume.setFluid(this.fluid);

		return (T) volume;
	}

	@Override
	public <T extends BaseVolume> T takeVolume(Fraction taken) {
		return (T) new FluidVolume(fluid, super.takeVolume(taken).getFraction());
	}

	@Override
	public <T extends BaseVolume> T giveVolume(Fraction pushed) {
		return (T) new FluidVolume(fluid, super.giveVolume(pushed).getFraction());
	}

	public <T extends BaseVolume> void pullVolume(T target, Fraction pulled) {
		if (target instanceof FluidVolume && ((FluidVolume) target).getFluid() != fluid) setFluid(((FluidVolume) target).getFluid());
		super.pullVolume(target, pulled);
	}

	public <T extends BaseVolume> void pushVolume(T target, Fraction pushed) {
		if (target instanceof FluidVolume && ((FluidVolume) target).getFluid() != fluid) ((FluidVolume) target).setFluid(fluid);
		super.pushVolume(target, pushed);
	}

	@Override
	public boolean isFull() {
		return hasStored(size) && this.fluid != Fluids.EMPTY;
	}

	@Override
	public boolean isEmpty() {
		return hasAvailable(size) || this.fluid == Fluids.EMPTY;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.fluid, this.fraction);
	}

	public FluidVolume copy() {
		return new FluidVolume(fluid, fraction);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof FluidVolume)) return false;

		FluidVolume volume = (FluidVolume) object;

		return Objects.equal(this.fluid, volume.fluid) && Objects.equal(this.fraction, volume.fraction);
	}

	@Override
	public String toString() {
		return "Volume{" + "fluid=" + this.fluid + ", fraction=" + this.fraction + '}';
	}

	public String getFluidString() {
		return Registry.FLUID.getId(fluid).toString();
	}

	public String getFractionString() {
		return fraction.getNumerator() + ":" + fraction.getDenominator();
	}
}
