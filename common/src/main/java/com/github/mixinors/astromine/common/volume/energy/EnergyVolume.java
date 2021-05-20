/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.volume.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.volume.base.Volume;
import io.netty.buffer.ByteBuf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A {@link Volume} of energy, whose amount and size
 * are represented by {@link Double}.
 *
 * It is not an inventory, thus it is recommended to use an {@link EnergyComponent}.
 *
 * A few utility methods for instantiation are provided, that being:
 * - {@link #ofEmpty()}, returning an empty volume.
 *
 * - {@link #ofEmpty(Runnable)}, returning an empty volume, with an a listener.
 *
 * - {@link #of(double)}, returning a volume with the specified amount.
 *
 * - {@link #of(double, Runnable)}, returning a volume with the specified amount
 * as a {@link Double} and a listener as a {@link Runnable}.
 *
 * - {@link #of(double, double)}, returning a volume with the specified amount
 * as a {@link Double)} and size as a {@link Double}.
 *
 * - {@link #of(double, double, Runnable)}, returning a volume with the specified amount
 * as a {@link Double)}, size as a {@link Double}, and a listener as a {@link Runnable}.
 *
 * It is recommended that you always set the volume's listener to
 * {@link EnergyComponent#updateListeners()}, if using one.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag()} and {@link #fromTag(CompoundTag)}.
 * - {@link JsonElement} - through {@link #toJson()} and {@link #fromJson(JsonElement)}.
 * - {@link ByteBuf} - through {@link #toPacket(PacketByteBuf)} and {@link #fromPacket(PacketByteBuf)}.
 */
public class EnergyVolume extends Volume<Double> {
	/** Instantiates an {@link EnergyVolume} with. */
	protected EnergyVolume(double amount, double size) {
		super(amount, size);
	}

	/** Instantiates an {@link EnergyVolume} with a listener. */
	protected EnergyVolume(double amount, double size, Runnable runnable) {
		super(amount, size, runnable);
	}

	/** Instantiates an empty {@link EnergyVolume}. */
	public static EnergyVolume ofEmpty() {
		return new EnergyVolume(0.0D, Long.MAX_VALUE);
	}

	/** Instantiates an empty {@link EnergyVolume} with a listener. */
	public static EnergyVolume ofEmpty(Runnable runnable) {
		return new EnergyVolume(0.0D, Long.MAX_VALUE, runnable);
	}

	/** Instantiates an {@link EnergyVolume}. */
	public static EnergyVolume of(double size) {
		return new EnergyVolume(0, size);
	}

	/** Instantiates an {@link EnergyVolume} with a listener. */
	public static EnergyVolume of(double size, Runnable runnable) {
		return new EnergyVolume(0, size, runnable);
	}

	/** Instantiates an {@link EnergyVolume}. */
	public static EnergyVolume of(double amount, double size) {
		return new EnergyVolume(amount, size);
	}

	/** Instantiates an {@link EnergyVolume} with a listener. */
	public static EnergyVolume of(double amount, double size, Runnable runnable) {
		return new EnergyVolume(amount, size, runnable);
	}

	/**
	 * Attempts to give the given {@link V} volume a {@link Double} part of this
	 * volume's content.
	 *
	 * The amount transferred is the {@link Math#min(double, double)} between
	 * the target's available space, our amount, and the specified amount.
	 */
	@Override
	public <V extends Volume<Double>> void give(V volume, Double amount) {
		if (!(volume instanceof EnergyVolume))
			return;

		var difference = Math.min(volume.getSize() - volume.getAmount(), Math.min(getAmount(), amount));

		if (difference > 0.0D) {
			volume.setAmount(volume.getAmount() + difference);
			setAmount(getAmount() - difference);
		}
	}

	/** Gives this volume the minimum between the available amount and the
	 * specified amount. */
	@Override
	public void give(Double amount) {
		var difference = Math.min(getSize() - getAmount(), amount);

		setAmount(getAmount() + difference);
	}

	/**
	 * Attempts to take the given {@link Double} amount from a {@link V}
	 * volume's content.
	 *
	 * The amount transferred is the {@link Math#min(long, long)} between
	 * our available space, their amount, and the specified amount.
	 */
	@Override
	public <V extends Volume<Double>> void take(V volume, Double amount) {
		if (!(volume instanceof EnergyVolume))
			return;

		volume.give(this, amount);
	}

	/** Takes the minimum between the stored amount and the
	 * specified amount from this volume. */
	@Override
	public void take(Double doubleA) {
		var amount = Math.min(getAmount(), doubleA);

		setAmount(getAmount() - amount);
	}

	/** Returns a copy of this volume. */
	@Override
	public <V extends Volume<Double>> V copy() {
		return (V) new EnergyVolume(getAmount(), getSize());
	}

	/** Returns this volume's string representation.
	 * For example, it may be "25000.00 of 32000.00 E */
	@Override
	public String toString() {
		return String.format("%s E", super.toString());
	}

	/** Deserializes a volume from a {@link CompoundTag}. */
	public static EnergyVolume fromTag(CompoundTag tag) {
		return of(tag.getDouble("Amount"), tag.getDouble("Size"));
	}

	/** Serializes this volume to a {@link CompoundTag}. */
	@Override
	public CompoundTag toTag() {
		var tag = new CompoundTag();
		
		tag.putDouble("Amount", getAmount());
		tag.putDouble("Size", getSize());
		
		return tag;
	}

	/** Deserializes a volume from a {@link JsonElement}. */
	public static EnergyVolume fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) return EnergyVolume.of(jsonElement.getAsDouble());
		else {
			var jsonObject = jsonElement.getAsJsonObject();

			if (!jsonObject.has("amount")) {
				return null;
			} else {
				if (!jsonObject.has("size")) {
					return EnergyVolume.of(jsonObject.get("amount").getAsDouble());
				} else {
					return EnergyVolume.of(jsonObject.get("amount").getAsDouble(), jsonObject.get("size").getAsDouble());
				}
			}
		}
	}

	/** Serializes this volume to a {@link JsonElement}. */
	public JsonElement toJson() {
		var object = new JsonObject();
		
		object.addProperty("amount", getAmount());
		object.addProperty("size", getSize());
		
		return object;
	}

	/** Deserializes a volume from a {@link ByteBuf}. */
	public PacketByteBuf toPacket(PacketByteBuf buffer) {
		buffer.writeDouble(getAmount());
		buffer.writeDouble(getSize());

		return buffer;
	}

	/** Serializes this volume to a {@link JsonElement}. */
	public static EnergyVolume fromPacket(PacketByteBuf buffer) {
		return EnergyVolume.of(buffer.readDouble(), buffer.readDouble());
	}
}
