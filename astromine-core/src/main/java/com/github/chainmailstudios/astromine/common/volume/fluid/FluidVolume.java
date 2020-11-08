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

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.utilities.FractionUtilities;
import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;

import com.google.common.base.Objects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FluidVolume extends Volume<Identifier, Fraction> {
	public static final Identifier ID = AstromineCommon.identifier("fluid");

	private Fluid fluid;

	protected FluidVolume(Fraction amount, Fraction size, Fluid fluid) {
		super(ID, amount, size);
		this.fluid = fluid;
	}

	protected FluidVolume(Fraction amount, Fraction size, Fluid fluid, Runnable runnable) {
		super(ID, amount, size, runnable);
		this.fluid = fluid;
	}

	public static FluidVolume empty() {
		return FluidVolume.of(Fraction.EMPTY, Fluids.EMPTY);
	}

	public static FluidVolume oxygen() {
		return new FluidVolume(Fraction.BUCKET, Fraction.BUCKET, Registry.FLUID.get(AstromineCommon.identifier("oxygen")));
	}

	public static FluidVolume attached(SimpleFluidComponent component) {
		return new FluidVolume(Fraction.EMPTY, Fraction.BUCKET, Fluids.EMPTY, component::updateListeners);
	}

	public static FluidVolume of(Fraction amount, Fluid fluid) {
		return new FluidVolume(amount, Fraction.of(128L), fluid);
	}

	public static FluidVolume of(Fraction amount, Fraction size, Fluid fluid) {
		return new FluidVolume(amount, size, fluid);
	}

	public static FluidVolume of(Fraction amount, Fluid fluid, Runnable runnable) {
		return new FluidVolume(amount, Fraction.of(128L), fluid, runnable);
	}

	public static FluidVolume of(Fraction amount, Fraction size, Fluid fluid, Runnable runnable) {
		return new FluidVolume(amount, size, fluid, runnable);
	}

	public static boolean areFluidsEqual(FluidVolume volume1, FluidVolume volume2) {
		return Objects.equal(volume1.getFluid(), volume2.getFluid());
	}

	public static FluidVolume fromTag(CompoundTag tag) {
		return new FluidVolume(Fraction.fromTag(tag.getCompound("amount")), Fraction.fromTag(tag.getCompound("size")), Registry.FLUID.get(new Identifier(tag.getString("fluid"))));
	}

	public static FluidVolume fromJson(JsonElement jsonElement) {
		if (!(jsonElement instanceof JsonObject))
			return null;
		else {
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			if (!jsonObject.has("fluid"))
				return null;
			if (!jsonObject.has("amount")) {
				return FluidVolume.of(Fraction.BUCKET, Registry.FLUID.get(new Identifier(jsonObject.get("fluid").getAsString())));
			} else {
				return FluidVolume.of(FractionUtilities.fromJson(jsonObject.get("amount")), Registry.FLUID.get(new Identifier(jsonObject.get("fluid").getAsString())));
			}
		}
	}

	public static FluidVolume fromPacket(PacketByteBuf buffer) {
		if (!buffer.readBoolean()) {
			return empty();
		} else {
			int id = buffer.readVarInt();

			Fraction amount = FractionUtilities.fromPacket(buffer);
			Fraction size = FractionUtilities.fromPacket(buffer);

			return FluidVolume.of(amount, size, Registry.FLUID.get(id));
		}
	}

	public boolean fluidEquals(FluidVolume volume) {
		return Objects.equal(this.getFluid(), volume.getFluid());
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public Identifier getFluidId() {
		return Registry.FLUID.getId(getFluid());
	}

	public boolean test(Fluid fluid) {
		return (this.fluid == fluid && fluid != Fluids.EMPTY) || this.isEmpty();
	}

	public boolean test(FluidVolume volume) {
		return volume.test(getFluid()) && volume.hasAvailable(getAmount());
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() || getFluid() == Fluids.EMPTY;
	}

	@Override
	public <V extends Volume<Identifier, Fraction>> V add(V v, Fraction fraction) {
		if (!(v instanceof FluidVolume))
			return (V) this;

		if (((FluidVolume) v).getFluid() != getFluid()) {
			if (v.isEmpty()) {
				((FluidVolume) v).setFluid((this).getFluid());
			} else {
				return (V) this;
			}
		}

		Fraction amount = Fraction.minimum(v.getSize().subtract(v.getAmount()), Fraction.minimum(getAmount(), fraction));

		amount.ifBiggerThan(Fraction.EMPTY, () -> {
			v.setAmount(v.getAmount().add(amount));
			setAmount(getAmount().subtract(amount));
		});

		if (isEmpty()) {
			setFluid(Fluids.EMPTY);
			setAmount(Fraction.EMPTY);
		}

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Fraction>> V add(Fraction fraction) {
		Fraction amount = Fraction.minimum(getSize().subtract(getAmount()), fraction);

		setAmount(Fraction.add(getAmount(), amount));

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Fraction>> V minus(Fraction fraction) {
		Fraction amount = Fraction.minimum(getAmount(), fraction);

		setAmount(Fraction.subtract(getAmount(), amount));

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Fraction>> V moveFrom(V v, Fraction fraction) {
		if (!(v instanceof FluidVolume))
			return (V) this;

		if (((FluidVolume) v).getFluid() != getFluid()) {
			if (this.isEmpty()) {
				this.setFluid(((FluidVolume) v).getFluid());
			} else {
				return (V) this;
			}
		}

		v.add(this, fraction);

		return (V) this;
	}

	@Override
	public <V extends Volume<Identifier, Fraction>> V moveFrom(V v) {
		return moveFrom(v, v.getAmount());
	}

	@Override
	public <V extends Volume<Identifier, Fraction>> V copy() {
		return (V) of(getAmount().copy(), getSize().copy(), getFluid());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;

		if (!(object instanceof FluidVolume))
			return false;

		if (!super.equals(object))
			return false;

		FluidVolume volume = (FluidVolume) object;

		return FluidVolume.areFluidsEqual(this, volume);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), getAmount(), getSize(), fluid);
	}

	@Override
	public String toString() {
		return getAmount().toDecimalString() + " / " + getSize().toDecimalString() + " " + getFluidId();
	}

	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.put("amount", getAmount().toTag());
		tag.put("size", getSize().toTag());
		tag.putString("fluid", Registry.FLUID.getId(getFluid()).toString());
		return tag;
	}

	public PacketByteBuf toPacket(PacketByteBuf buffer) {
		if (this.isEmpty()) {
			buffer.writeBoolean(false);
		} else {
			buffer.writeBoolean(true);

			buffer.writeVarInt(Registry.FLUID.getRawId(getFluid()));

			FractionUtilities.toPacket(buffer, getAmount());
			FractionUtilities.toPacket(buffer, getSize());
		}

		return buffer;
	}
}
