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

package com.github.chainmailstudios.astromine.common.volume.base;

import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.Objects;

/**
 * A class representing a {@link Volume} of {@link N},
 * with an {@link #amount} and a {@link #size}.
 */
public abstract class Volume<N extends Number> {
	private N amount;

	private N size;

	private Runnable runnable;

	/** Instantiates a volume with the given values and a listener. */
	protected Volume(N amount, N size) {
		this.amount = amount;
		this.size = size;
	}

	/** Instantiates a volume with the given values and a listener. */
	protected Volume(N amount, N size, Runnable runnable) {
		this(amount, size);

		this.runnable = runnable;
	}

	/** Returns the amount of this volume. */
	public N getAmount() {
		return amount;
	}

	/** Sets the amount of this volume to the specified value. */
	public void setAmount(N n) {
		this.amount = n;

		if (runnable != null)
			runnable.run();
	}

	/** Returns this volume with an amount equal to the specified amount. */
	public <V extends Volume<N>> V withAmount(N amount) {
		V volume = copy();
		volume.setAmount(amount);
		return volume;
	}

	/** Returns the size of this volume. */
	public N getSize() {
		return size;
	}

	/** Sets the size of this volume to the specified size. */
	public void setSize(N size) {
		this.size = size;

		if (runnable != null)
			runnable.run();
	}

	/** Returns this volume with a size equal to the specified amount. */
	public <V extends Volume<N>> V withSize(N size) {
		V volume = copy();
		volume.setSize(size);
		return volume;
	}

	/** Returns the runnable of this volume. */
	public Runnable getRunnable() {
		return runnable;
	}

	/** Sets the runnable of this volume to the specified value. */
	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	/** Returns this volume with a runnable equal to the specified amount. */
	public <V extends Volume<N>> V withRunnable(Runnable runnable) {
		setRunnable(runnable);

		return (V) this;
	}

	/** Asserts whether this volume is empty. */
	public boolean isEmpty() {
		return amount.doubleValue() == 0.0D;
	}

	/** Asserts whether this volume is full. */
	public boolean isFull() {
		return amount.equals(size);
	}

	/** Asserts whether this volume has the specified {@link Number} available. */
	public boolean hasAvailable(Number required) {
		return size.doubleValue() - amount.doubleValue() >= required.doubleValue();
	}

	/** Asserts whether this volume has the specified {@link Number} stored. */
	public boolean hasStored(Number required) {
		return amount.doubleValue() >= required.doubleValue();
	}

	/** Asserts whether this volume is bigger than the specified {@link Number}. */
	public boolean biggerThan(Number number) {
		return amount.doubleValue() > number.doubleValue();
	}

	/** Asserts whether this volume is smaller than the specified {@link Number}. */
	public boolean smallerThan(Number number) {
		return amount.doubleValue() < number.doubleValue();
	}

	/** Asserts whether this volume is bigger or equal than the specified {@link Number}. */
	public boolean biggerOrEqualThan(Number number) {
		return amount.doubleValue() >= number.doubleValue();
	}

	/** Asserts whether this volume is smaller or equal than the specified {@link Number}. */
	public boolean smallerOrEqualThan(Number number) {
		return amount.doubleValue() <= number.doubleValue();
	}

	/** Asserts the quality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;

		if (!(object instanceof Volume))
			return false;

		Volume<?> volume = (Volume<?>) object;

		if (!Objects.equals(amount, volume.amount))
			return false;

		return Objects.equals(size, volume.size);
	}

	/** Returns the hash for this volume. */
	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(amount, size, runnable);
	}

	/** Returns this volume's string representation.
	 * For example, it may be "8.00 of 32.00" */
	@Override
	public String toString() {
		return String.format("%s of %s", getAmount(), getSize());
	}

	/**
	 * Attempts to give the given {@link V} volume a {@link Fraction} of this
	 * volume's content.
	 */
	public abstract <V extends Volume<N>> void give(V volume, N amount);

	/** Gives this volume the minimum between the available amount and the
	 * specified amount. */
	public abstract void give(N amount);

	/** Attempts to give the given {@link V} volume all our content. */
	public <V extends Volume<N>> void give(V amount) {
		give(amount, amount.getAmount());
	}

	/**
	 * Attempts to take the given {@link N} from a {@link V}
	 * volume's content.
	 */
	public abstract <V extends Volume<N>> void take(V volume, N amount);

	/** Takes the minimum between the stored amount and the
	 * specified amount from this volume. */
	public abstract void take(N amount);

	/** Attempts to take all the content from the specified {@link V} volume. */
	public <V extends Volume<N>> void take(V volume) {
		take(volume, volume.getAmount());
	}

	/** Returns a copy of this volume. */
	public abstract <V extends Volume<N>> V copy();

	/** Serializes this volume to a {@link CompoundTag}. */
	public abstract CompoundTag toTag();

	/** Deserializes a volume from a {@link JsonElement}. */
	public abstract JsonElement toJson();

	/** Serialize this volume to a {@link ByteBuf}. */
	public abstract PacketByteBuf toPacket(PacketByteBuf buffer);
}
