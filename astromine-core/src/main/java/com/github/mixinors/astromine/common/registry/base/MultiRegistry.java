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

package com.github.mixinors.astromine.common.registry.base;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * A unidirectional registry,
 * where all keys must be unique, values may be
 * repeated, and multiple values can be associated
 * with one key.
 */
public abstract class MultiRegistry<K, V> {
	private final Multimap<K, V> entries = HashMultimap.create();

	/** Returns the {@link Collection<V>} collection of values associated with the given {@link K} key. */
	public Collection<V> get(K k) {
		return entries.get(k);
	}

	/** Associates the given {@link K} key with the specified {@link V} value.
	 * Returns whether another key-value pair of same hash did not already exist. */
	public boolean set(K k, V v) {
		return entries.put(k, v);
	}

	/** Associates the given {@link K} key with the specified {@link V} value.
	 * Returns whether another key-value pair of same hash did not already exist. */
	public boolean add(K k, V v) {
		return set(k, v);
	}

	/** Dissociates the given {@link K} key from the specified {@link V} value.
	 * Returns whether the operation was successful or not. */
	public boolean remove(K k, V v) {
		return entries.remove(k, v);
	}

	/** Dissociates the given {@link K} key from its associated {@link Collection<V>} values.
	 * Returns the existing values for the {@link K} key, or empty if none existed. */
	public Collection<V> removeKey(K k) {
		return entries.removeAll(k);
	}

	/** Associates the given {@link K} key with the specified {@link V} value.
	 * Returns the given {@link V} value. */
	public V register(K k, V v) {
		set(k, v);

		return v;
	}

	/** Dissociates the given {@link K} key from the specified {@link V} value.
	 * Returns whether the operation was successful or not. */
	public boolean unregister(K k, V v) {
		return remove(k, v);
	}

	/** Dissociates the given {@link K} key from its associated {@link V} value.
	 * Returns the existing values for the {@link K} key, or empty if none existed. */
	public Collection<V> unregisterKey(K k) {
		return removeKey(k);
	}

	/** Asserts whether this registry contains the given {@link K} key
	 * associated with the specified {@link V} value. */
	public boolean contains(K k, V v) {
		return containsKey(k) && get(k).equals(v);
	}

	/** Asserts whether this registry contains the given {@link K} key. */
	public boolean containsKey(K k) {
		return entries.containsKey(k);
	}

	/** Asserts whether this registry contains the given {@link V} value. */
	public boolean containsValue(V v) {
		return entries.containsValue(v);
	}

	/** Returns a collection of this registry's {@link K} keys. */
	public Collection<K> getKeys() {
		return entries.keySet();
	}

	/** Returns a collection of this registry's {@link V} values. */
	public Collection<V> getValues() {
		return entries.values();
	}
}
