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

		if (r != null)
			r.run();
	}

	public <V extends Volume<T, N>> V withType(T t) {
		V v = copy();
		v.setType(t);
		return v;
	}

	public N getAmount() {
		return n;
	}

	public void setAmount(N n) {
		this.n = n;

		if (r != null)
			r.run();
	}

	public <V extends Volume<T, N>> V withAmount(N n) {
		V v = copy();
		v.setAmount(n);
		return v;
	}

	public N getSize() {
		return s;
	}

	public void setSize(N s) {
		this.s = s;

		if (r != null)
			r.run();
	}

	public <V extends Volume<T, N>> V withSize(N n) {
		V v = copy();
		v.setSize(n);
		return v;
	}

	public void setRunnable(Runnable r) {
		this.r = r;
	}

	public <V extends Volume<T, N>> V withRunnable(Runnable r) {
		setRunnable(r);

		return (V) this;
	}

	public boolean isFull() {
		return n.equals(s);
	}

	public boolean isEmpty() {
		return n.doubleValue() == 0.0D;
	}

	public boolean hasAvailable(Number required) {
		return s.doubleValue() - n.doubleValue() >= required.doubleValue();
	}

	public boolean hasStored(Number required) {
		return n.doubleValue() >= required.doubleValue();
	}

	public boolean biggerThan(Number number) {
		return n.doubleValue() > number.doubleValue();
	}

	public boolean smallerThan(Number number) {
		return n.doubleValue() < number.doubleValue();
	}

	public boolean biggerOrEqualThan(Number number) {
		return n.doubleValue() >= number.doubleValue();
	}

	public boolean smallerOrEqualThan(Number number) {
		return n.doubleValue() <= number.doubleValue();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;

		if (!(object instanceof Volume))
			return false;

		Volume<?, ?> volume = (Volume<?, ?>) object;

		if (!Objects.equals(t, volume.t))
			return false;
		if (!Objects.equals(n, volume.n))
			return false;

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

	public abstract <V extends Volume<T, N>> V moveFrom(V v);

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
