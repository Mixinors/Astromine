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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;

import com.google.common.base.Objects;

import java.text.DecimalFormat;

/**
 * A class representing a {@link Volume} of {@link Fluid}s, whose amount
 * and size are represented by a {@link Fraction}
 *
 * It is not an inventory, thus it is recommended to use a {@link FluidComponent},
 * most commonly via its implementation, {@link SimpleFluidComponent}.
 *
 * A few utility methods for instantiation are provided, that being:
 *
 * - {@link #ofEmpty()}, returning an empty volume.
 *
 * - {@link #ofEmpty(Runnable)}, returning an empty volume, with an a listener.
 *
 * - {@link #of(Fraction, Fluid)}, returning a volume with the specified
 * amount as a {@link Fraction}, and the given {@link Fluid}.
 *
 * - {@link #of(Fraction, Fluid, Runnable)}, returning a volume with the
 * specified amount as a {@link Fraction}, the given {@link Fluid},
 * and a listener as a {@link Runnable}.
 *
 * - {@link #of(Fraction, Fraction, Fluid)}, returning a volume with the
 * specified amount as a {@link Fraction}, size as a {@link Fraction},
 * and the given {@link Fluid}.
 *
 *- {@link #of(Fraction, Fraction, Fluid, Runnable)}, returning a value with
 * the specified amount as a {@link Fraction}, size as a {@link Fraction},
 * the given {@link Fluid}, and a listener as a {@link Runnable}.
 *
 * It is recommended that you always set the volume's listener to
 * {@link FluidComponent#updateListeners()}, if using one.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag()} and {@link #fromTag(CompoundTag)}.
 * - {@link JsonElement} - through {@link #toJson()} and {@link #fromJson(JsonElement)}.
 * - {@link ByteBuf} - through {@link #toPacket(PacketByteBuf)} and {@link #fromPacket(PacketByteBuf)}.
 */
public class FluidVolume extends Volume<Fraction> {
	public static final DecimalFormat FORMAT = new DecimalFormat("#0.00");

	private Fluid fluid;

	/** Instantiates a volume with the given values. */
	protected FluidVolume(Fraction amount, Fraction size, Fluid fluid) {
		super(amount, size);
		this.fluid = fluid;
	}

	/** Instantiates a volume with the given values and a listener. */
	protected FluidVolume(Fraction amount, Fraction size, Fluid fluid, Runnable runnable) {
		super(amount, size, runnable);
		this.fluid = fluid;
	}

	/** Instantiates an empty volume. */
	public static FluidVolume ofEmpty() {
		return new FluidVolume(Fraction.EMPTY, Fraction.MAX_VALUE, Fluids.EMPTY);
	}

	/** Instantiates an empty volume with a listener. */
	public static FluidVolume ofEmpty(Runnable runnable) {
		return new FluidVolume(Fraction.EMPTY, Fraction.MAX_VALUE, Fluids.EMPTY, runnable);
	}

	/** Instantiates a volume with the given values. */
	public static FluidVolume of(Fraction amount, Fluid fluid) {
		return new FluidVolume(amount, Fraction.MAX_VALUE, fluid);
	}

	/** Instantiates a volume with the given values and a listener. */
	public static FluidVolume of(Fraction amount, Fluid fluid, Runnable runnable) {
		return new FluidVolume(amount, Fraction.MAX_VALUE, fluid, runnable);
	}

	/** Instantiates a volume with the given values. */
	public static FluidVolume of(Fraction amount, Fraction size, Fluid fluid) {
		return new FluidVolume(amount, size, fluid);
	}

	/** Instantiates a volume with the given values and a listener. */
	public static FluidVolume of(Fraction amount, Fraction size, Fluid fluid, Runnable runnable) {
		return new FluidVolume(amount, size, fluid, runnable);
	}

	/** Asserts the equality of the volume's fluids. */
	public static boolean fluidsEqual(FluidVolume first, FluidVolume second) {
		return Objects.equal(first.getFluid(), second.getFluid());
	}

	/** Asserts the equality of the volume's fluids. */
	public boolean fluidEquals(FluidVolume volume) {
		return Objects.equal(getFluid(), volume.getFluid());
	}

	/** Returns the volume's fluid. */
	public Fluid getFluid() {
		return this.fluid;
	}

	/** Sets the volume's fluid to the given one. */
	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	/** Returns the identifier of the volume's fluid. */
	public Identifier getFluidId() {
		return Registry.FLUID.getId(getFluid());
	}

	/** Asserts whether the fluid can be inserted into this volume or not. */
	public boolean test(Fluid fluid) {
		return (this.fluid == fluid && fluid != Fluids.EMPTY) || this.isEmpty();
	}

	/** Asserts whether this volume can be inserted into the given one or not. */
	public boolean test(FluidVolume volume) {
		return volume.test(getFluid()) && volume.hasAvailable(getAmount());
	}

	/** Asserts whether this volume is empty or not. */
	@Override
	public boolean isEmpty() {
		return super.isEmpty() || getFluid() == Fluids.EMPTY;
	}

	/**
	 * Attempts to give the given {@link V} volume a {@link Fraction} of this
	 * volume's content.
	 *
	 * If we both have the same fluid, the target receives as much as possible.
	 *
	 * If our fluids are not the same, but the theirs is {@link Fluids#EMPTY}
	 * empty, change their fluid to {@link #fluid} ours. They then receive
	 * as much as possible.
	 *
	 * If our fluids not the same, and theirs is not {@link Fluids#EMPTY},
	 * do nothing.
	 *
	 * The amount transferred is the {@link Fraction#minimum(Fraction, Fraction)} between
	 * the target's available space, our amount, and the specified amount.
	 */
	@Override
	public <V extends Volume<Fraction>> void give(V volume, Fraction fraction) {
		if (!(volume instanceof FluidVolume))
			return;

		if (((FluidVolume) volume).getFluid() != getFluid()) {
			if (volume.isEmpty()) {
				((FluidVolume) volume).setFluid((this).getFluid());
			} else {
				return;
			}
		}

		Fraction amount = Fraction.minimum(volume.getSize().subtract(volume.getAmount()), Fraction.minimum(getAmount(), fraction));

		if (amount.biggerThan(Fraction.EMPTY)) {
			volume.setAmount(volume.getAmount().add(amount));
			setAmount(getAmount().subtract(amount));
		}

		if (isEmpty()) {
			setFluid(Fluids.EMPTY);
			setAmount(Fraction.EMPTY);
		}
	}

	/** Gives this volume the minimum between the available amount and the
	 * specified amount. */
	@Override
	public void give(Fraction fraction) {
		Fraction amount = Fraction.minimum(getSize().subtract(getAmount()), fraction);

		setAmount(Fraction.add(getAmount(), amount));
	}

	/**
	 * Attempts to take the given {@link Fraction} from a {@link V}
	 * volume's content.
	 *
	 * If we both have the same fluid, we receive as much as possible.
	 *
	 * If our fluids are not the same, but the ours is {@link Fluids#EMPTY}
	 * empty, change our fluid to {@link #fluid} theirs. We then receive
	 * as much as possible.
	 *
	 * If our fluids not the same, and ours is not {@link Fluids#EMPTY},
	 * do nothing.
	 *
	 * The amount transferred is the {@link Fraction#minimum(Fraction, Fraction)} between
	 * the our available space, their amount, and the specified amount.
	 */
	@Override
	public <V extends Volume<Fraction>> void take(V volume, Fraction amount) {
		if (!(volume instanceof FluidVolume))
			return;

		if (((FluidVolume) volume).getFluid() != getFluid()) {
			if (this.isEmpty()) {
				this.setFluid(((FluidVolume) volume).getFluid());
			} else {
				return;
			}
		}

		volume.give(this, amount);
	}

	/** Takes the minimum between the stored amount and the
	 * specified amount from this volume. */
	@Override
	public void take(Fraction fraction) {
		Fraction amount = Fraction.minimum(getAmount(), fraction);

		setAmount(Fraction.subtract(getAmount(), amount));
	}

	/** Returns a copy of this volume. */
	@Override
	public <V extends Volume<Fraction>> V copy() {
		return (V) of(getAmount(), getSize(), getFluid());
	}

	/** Asserts the quality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;

		if (!(object instanceof FluidVolume))
			return false;

		if (!super.equals(object))
			return false;

		FluidVolume volume = (FluidVolume) object;

		return FluidVolume.fluidsEqual(this, volume);
	}

	/** Returns the hash for this volume. */
	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), getAmount(), getSize(), fluid);
	}

	/** Returns this volume's string representation.
	 * For example, it may be "minecraft:water, 16.50 of 32.00 Buckets" */
	@Override
	public String toString() {
		return getFluidId().toString() + ", " + FORMAT.format(getAmount().doubleValue()) + " of " + FORMAT.format(getSize().doubleValue() + " Buckets");
	}

	/** Deserializes a volume from a {@link CompoundTag}. */
	public static FluidVolume fromTag(CompoundTag tag) {
		return of(Fraction.fromTag(tag.getCompound("amount")), Fraction.fromTag(tag.getCompound("size")), Registry.FLUID.get(new Identifier(tag.getString("fluid"))));
	}

	/** Serializes this volume to a {@link CompoundTag}. */
	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.put("amount", getAmount().toTag());
		tag.put("size", getSize().toTag());
		tag.putString("fluid", Registry.FLUID.getId(getFluid()).toString());
		return tag;
	}

	/** Deserializes a volume from a {@link JsonElement}. */
	public static FluidVolume fromJson(JsonElement jsonElement) {
		if (!(jsonElement instanceof JsonObject)) {
			if (jsonElement.isJsonPrimitive()) {
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

				if (jsonPrimitive.isString()) {
					return of(Fraction.BUCKET, Registry.FLUID.get(new Identifier(jsonPrimitive.getAsString())));
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			if (!jsonObject.has("fluid")) return null;
			if (!jsonObject.has("amount")) {
				return of(Fraction.BUCKET, Registry.FLUID.get(new Identifier(jsonObject.get("fluid").getAsString())));
			} else {
				return of(Fraction.fromJson(jsonObject.get("amount")), Registry.FLUID.get(new Identifier(jsonObject.get("fluid").getAsString())));
			}
		}
	}

	/** Serializes this volume to a {@link JsonElement}. */
	public JsonElement toJson() {
		JsonObject object = new JsonObject();
		object.addProperty("fluid", Registry.FLUID.getId(getFluid()).toString());
		object.add("amount", getAmount().toJson());
		object.add("size", getSize().toJson());
		return object;
	}

	/** Deserializes a volume from a {@link ByteBuf}. */
	public static FluidVolume fromPacket(PacketByteBuf buffer) {
		if (!buffer.readBoolean()) {
			return ofEmpty();
		} else {
			int id = buffer.readVarInt();

			Fraction amount = Fraction.fromPacket(buffer);
			Fraction size = Fraction.fromPacket(buffer);

			return of(amount, size, Registry.FLUID.get(id));
		}
	}

	/** Serialize this volume to a {@link ByteBuf}. */
	public PacketByteBuf toPacket(PacketByteBuf buffer) {
		if (this.isEmpty()) {
			buffer.writeBoolean(false);
		} else {
			buffer.writeBoolean(true);

			buffer.writeVarInt(Registry.FLUID.getRawId(getFluid()));

			getAmount().toPacket(buffer);
			getSize().toPacket(buffer);
		}

		return buffer;
	}
}
