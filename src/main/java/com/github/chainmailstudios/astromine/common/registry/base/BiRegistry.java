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

package com.github.chainmailstudios.astromine.common.registry.base;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Collection;

public abstract class BiRegistry<T, U> {
	private final BiMap<T, U> entries = HashBiMap.create();

	public U get(T t) {
		return entries.get(t);
	}

	public T getKey(U u) {
		return entries.inverse().get(u);
	}

	public U set(T t, U u) {
		entries.putIfAbsent(t, u);
		return u;
	}

	public U add(T t, U u) {
		return set(t, u);
	}

	public U remove(T t, U u) {
		entries.remove(t, u);
		return u;
	}

	public void removeKey(T t) {
		entries.remove(t);
	}

	public void removeValue(U u) {
		entries.inverse().remove(u);
	}

	public U register(T t, U u) {
		return set(t, u);
	}

	public void unregister(T t, U u) {
		remove(t, u);
	}

	public void unregisterKey(T t) {
		removeKey(t);
	}

	public void unregisterValue(U u) {
		removeValue(u);
	}

	public boolean contains(T t, U u) {
		return containsKey(t) && get(t).equals(u);
	}

	public boolean containsKey(T t) {
		return entries.containsKey(t);
	}

	public boolean containsValue(U u) {
		return entries.containsValue(u);
	}

	public Collection<T> getKeys() {
		return entries.keySet();
	}

	public Collection<U> getValues() {
		return entries.values();
	}
}
