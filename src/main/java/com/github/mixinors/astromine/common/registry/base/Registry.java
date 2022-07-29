/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A unidirectional registry, where all keys must be unique and values may be repeated.
 */
public class Registry<K, V> {
	public static <K, V> Codec<Registry<K, V>> createCodec(Codec<K> keyCodec, Codec<V> valueCodec) {
		return RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.unboundedMap(keyCodec, valueCodec).fieldOf("entries").forGetter(Registry::getEntries)
				).apply(instance, Registry::new)
		);
	}
	
	private Map<K, V> entries;
	
	public Registry() {
		this.entries = new HashMap<>();
	}
	
	public Registry(Map<K, V> entries) {
		this.entries = entries;
	}
	
	/**
	 * Returns a supplier for the {@link V} value associated with the given {@link K} key.
	 */
	public Supplier<V> getSupplier(K key) {
		return () -> getEntries().get(key);
	}
	
	/** Returns the {@link V} value associated with the given {@link K} key. */
	public V get(K k) {
		return entries.get(k);
	}
	
	/**
	 * Associates the given {@link K} key with the specified {@link V} value. Returns the existing value for the {@link K} key, or null if none existed.
	 */
	public V set(K k, V v) {
		return entries.put(k, v);
	}
	
	/**
	 * Associates the given {@link K} key with the specified {@link V} value. Returns the existing value for the {@link K} key, or null if none existed.
	 */
	public V add(K k, V v) {
		return set(k, v);
	}
	
	/**
	 * Dissociates the given {@link K} key from the specified {@link V} value. Returns whether the operation was successful or not.
	 */
	public boolean remove(K k, V v) {
		return entries.remove(k, v);
	}
	
	/**
	 * Dissociates the given {@link K} key from its associated {@link V} value. Returns the existing value for the {@link K} key, or null if none existed.
	 */
	public V removeKey(K k) {
		return entries.remove(k);
	}
	
	/**
	 * Associates the given {@link K} key with the specified {@link V} value. Returns the given {@link V} value.
	 */
	public V register(K k, V v) {
		set(k, v);
		
		return v;
	}
	
	/**
	 * Dissociates the given {@link K} key from the specified {@link V} value. Returns whether the operation was successful or not.
	 */
	public boolean unregister(K k, V v) {
		return remove(k, v);
	}
	
	/**
	 * Dissociates the given {@link K} key from its associated {@link V} value. Returns the existing value for the {@link K} key, or null if none existed.
	 */
	public V unregisterKey(K k) {
		return removeKey(k);
	}
	
	/**
	 * Clears this registry's entries.
	 */
	public void clear() {
		entries.clear();
	}
	
	/**
	 * Asserts whether this registry contains the given {@link K} key associated with the specified {@link V} value.
	 */
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
	
	/**
	 * Sets this registry's entries.
	 * @param entries the new entries.
	 */
	public void setEntries(Map<K, V> entries) {
		this.entries = entries;
	}
	
	/**
	 * Returns a map of this registry's entries.
	 */
	public Map<K, V> getEntries() {
		return entries;
	}
}
