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

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public abstract class Volume<T, N extends Number> {
	private T t;

	private N n;

	private N s;

	private Runnable r;

	public Volume(T t, N n, N s) {
		this.t = t;
		this.n = n;
		this.s = s;
	}

	public Volume(T t, N n, N s, Runnable r) {
		this(t, n, s);

		this.r = r;
	}

	public T getType() {
		return t;
	}

	public void setType(T t) {
		this.t = t;

		if (r != null) r.run();
	}

	public N getAmount() {
		return n;
	}

	public void setAmount(N n) {
		this.n = n;

		if (r != null) r.run();
	}

	public N getSize() {
		return s;
	}

	public void setSize(N s) {
		this.s = s;

		if (r != null) r.run();
	}

	public void setRunnable(Runnable r) {
		this.r = r;
	}

	public boolean isFull() {
		return n.equals(s);
	}

	public Volume<T, N> ifFull(Runnable runnable) {
		if (isFull()) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotFull(Runnable runnable) {
		if (!isFull()) {
			runnable.run();
		}

		return this;
	}

	public boolean isEmpty() {
		return n.doubleValue() == 0.0D;
	}

	public Volume<T, N> ifEmpty(Runnable runnable) {
		if (isEmpty()) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotEmpty(Runnable runnable) {
		if (!isEmpty()) {
			runnable.run();
		}

		return this;
	}

	public boolean hasAvailable(Number required) {
		return s.doubleValue() - n.doubleValue() >= required.doubleValue();
	}

	public Volume<T, N> ifAvailable(Number required, Runnable runnable) {
		if (hasAvailable(required)) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotAvailable(Number required, Runnable runnable) {
		if (!hasAvailable(required)) {
			runnable.run();
		}

		return this;
	}

	public boolean hasStored(Number required) {
		return n.doubleValue() >= required.doubleValue();
	}

	public Volume<T, N> ifStored(Number required, Runnable runnable) {
		if (hasStored(required)) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotStored(Number required, Runnable runnable) {
		if (!hasStored(required)) {
			runnable.run();
		}

		return this;
	}

	public boolean biggerThan(Number number) {
		return n.doubleValue() > number.doubleValue();
	}

	public Volume<T, N> ifBiggerThan(Number number, Runnable runnable) {
		if (biggerThan(number)) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotBiggerThan(Number number, Runnable runnable) {
		if (!biggerThan(number)) {
			runnable.run();
		}

		return this;
	}

	public boolean smallerThan(Number number) {
		return n.doubleValue() < number.doubleValue();
	}

	public Volume<T, N> ifSmallerThan(Number number, Runnable runnable) {
		if (smallerThan(number)) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotSmallerThan(Number number, Runnable runnable) {
		if (!smallerThan(number)) {
			runnable.run();
		}

		return this;
	}

	public boolean biggerOrEqualThan(Number number) {
		return n.doubleValue() >= number.doubleValue();
	}

	public Volume<T, N> ifBiggerOrEqualThan(Number number, Runnable runnable) {
		if (biggerOrEqualThan(number)) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotBiggerOrEqualThan(Number number, Runnable runnable) {
		if (!biggerOrEqualThan(number)) {
			runnable.run();
		}

		return this;
	}

	public boolean smallerOrEqualThan(Number number) {
		return n.doubleValue() <= number.doubleValue();
	}

	public Volume<T, N> ifSmallerOrEqualThan(Number number, Runnable runnable) {
		if (smallerOrEqualThan(number)) {
			runnable.run();
		}

		return this;
	}

	public Volume<T, N> ifNotSmallerOrEqualThan(Number number, Runnable runnable) {
		if (!smallerOrEqualThan(number)) {
			runnable.run();
		}

		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;

		if (!(object instanceof Volume)) return false;

		Volume<?, ?> volume = (Volume<?, ?>) object;

		if (!Objects.equals(t, volume.t)) return false;
		if (!Objects.equals(n, volume.n)) return false;

		return Objects.equals(s, volume.s);
	}

	@Override
	public int hashCode() {
		int result = t != null ? t.hashCode() : 0;
		result = 31 * result + (n != null ? n.hashCode() : 0);
		result = 31 * result + (s != null ? s.hashCode() : 0);
		return result;
	}

	public abstract CompoundTag toTag();

	public abstract <V extends Volume<T, N>> V add(V v, N n);

	public abstract <V extends Volume<T, N>> V add(N n);

	public abstract <V extends Volume<T, N>> V moveFrom(V v, N n);

	public abstract <V extends Volume<T, N>> V minus(N n);

	public abstract <V extends Volume<T, N>> V copy();

	public boolean use(N n) {
		if (hasStored(n)) {
			minus(n);
			return true;
		}
		return false;
	}
}
