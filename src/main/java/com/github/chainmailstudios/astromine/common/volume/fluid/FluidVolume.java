/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chainmailstudios.astromine.common.volume.fluid;

import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.google.common.base.Objects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidVolume extends BaseVolume {
	private Fluid fluid = Fluids.EMPTY;

	private byte signal = 0b0;

	private Runnable runnable;

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

	public FluidVolume(Fluid fluid, Fraction fraction, byte signal) {
		this.fluid = fluid;
		this.fraction = fraction;
		this.signal = signal;
	}

	public FluidVolume(Fluid fluid, Fraction fraction, Runnable runnable) {
		this.fluid = fluid;
		this.fraction = fraction;
		this.runnable = runnable;
	}

	public static FluidVolume empty() {
		return new FluidVolume();
	}

	public static FluidVolume oxygen() {
		return new FluidVolume(AstromineFluids.OXYGEN, Fraction.BUCKET, (byte) 0b1);
	}

	public static FluidVolume attached(SimpleFluidInventoryComponent component) {
		return new FluidVolume(Fluids.EMPTY, Fraction.empty(), component::dispatchConsumers);
	}

	public static FluidVolume of(Fluid fluid, Fraction fraction) {
		return new FluidVolume(fluid, fraction);
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
	public static FluidVolume fromTag(CompoundTag tag) {
		// TODO: Null checks.

		FluidVolume fluidVolume = new FluidVolume(Fluids.EMPTY);

		if (!tag.contains("fluid")) {
			fluidVolume.fluid = AstromineFluids.OXYGEN;
		} else {
			fluidVolume.fluid = Registry.FLUID.get(new Identifier(tag.getString("fluid")));
		}

		if (!tag.contains("fraction")) {
			fluidVolume.fraction = Fraction.empty();
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

	public <T extends BaseVolume> T insertVolume(FluidVolume volume) {
		return (T) insertVolume(volume.getFluid(), volume.getFraction());
	}

	public <T extends BaseVolume> T insertVolume(Fluid fluid, Fraction fraction) {
		if (this.fluid != Fluids.EMPTY && fluid != this.fluid) return (T) FluidVolume.empty();

		FluidVolume volume = new FluidVolume(fluid, super.insertVolume(fraction).getFraction());

		if (this.fluid == Fluids.EMPTY) this.fluid = fluid;

		return (T) volume;
	}

	public <T extends BaseVolume> T extractVolume(Fluid fluid, Fraction fraction) {
		if (fluid != this.fluid) return (T) FluidVolume.empty();

		FluidVolume volume = new FluidVolume(this.fluid, super.extractVolume(fraction).getFraction());

		if (this.fraction.equals(Fraction.empty())) this.fluid = Fluids.EMPTY;

		return (T) volume;
	}

	@Override
	public <T extends BaseVolume> T extractVolume(Fraction taken) {
		T t = (T) new FluidVolume(fluid, super.extractVolume(taken).getFraction());

		if (this.fraction.equals(Fraction.empty())) this.fluid = Fluids.EMPTY;

		return t;
	}

	@Override
	public <T extends BaseVolume> T insertVolume(Fraction fraction) {
		T t = (T) new FluidVolume(fluid, super.insertVolume(fraction).getFraction());

		return t;
	}

	public <T extends BaseVolume> void pullVolume(T target, Fraction pulled) {
		if (target instanceof FluidVolume && ((FluidVolume) target).getFluid() != fluid) {
			setFluid(((FluidVolume) target).getFluid());
		}

		if (target instanceof FluidVolume && target.getFraction().equals(Fraction.empty())) {
			if (target.getFraction().equals(Fraction.empty())) {
				((FluidVolume) target).setFluid(Fluids.EMPTY);
			}
		}

		super.pullVolume(target, pulled);
	}

	public <T extends BaseVolume> void pushVolume(T target, Fraction pushed) {
		if (target instanceof FluidVolume && ((FluidVolume) target).getFluid() != fluid) {
			((FluidVolume) target).setFluid(fluid);
		}

		if (this.fraction.equals(Fraction.empty())) this.fluid = Fluids.EMPTY;

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
		return Objects.hashCode(this.fluid, this.fraction, this.signal);
	}

	public FluidVolume copy() {
		return new FluidVolume(fluid, fraction);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof FluidVolume)) return false;

		FluidVolume volume = (FluidVolume) object;

		return Objects.equal(this.fluid, volume.fluid) && Objects.equal(this.fraction, volume.fraction) && this.signal == volume.signal;
	}

	@Override
	public String toString() {
		return "Volume{" + "fluid=" + this.fluid + ", fraction=" + this.fraction + '}';
	}

	public Identifier getFluidIdentifier() {
		return Registry.FLUID.getId(fluid);
	}
}
