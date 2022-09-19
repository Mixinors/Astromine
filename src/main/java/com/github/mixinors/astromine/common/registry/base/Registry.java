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

import com.github.mixinors.astromine.AMCommon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Registry<V> {
	private static final String VALUES = "values";
	
	public static <V> Codec<Registry<V>> createCodec(Registry<V> registry, Codec<V> valueCodec) {
		return RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.unboundedMap(Identifier.CODEC, valueCodec).fieldOf("values").forGetter(r -> r.values)
				).apply(instance, (values) -> {
					registry.values.clear();
					registry.values.putAll(values);

					return registry;
				})
		);
	}
	
	private final Map<Identifier, RegistryEntry<V>> entries = new HashMap<>();
	private final Map<Identifier, V> values = new HashMap<>();
	
	public Registry() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ReloadListener());
	}
	
	/**
	 * Clears this registry.
	 */
	public void clear() {
		this.entries.clear();
		this.values.clear();
	}
	
	/**
	 * Returns the given key's entry, creating a new once if necessary.
	 * @param k the key.
	 * @return the entry.
	 */
	public RegistryEntry<V> getEntry(Identifier k) {
		entries.computeIfAbsent(k, $ -> new RegistryEntry<>(k, () -> values.get(k)));
		return entries.get(k);
	}
	
	/**
	 * Returns the given key's value.
	 */
	public V get(Identifier k) {
		return values.get(k);
	}
	
	/**
	 * Returns the given value's first key.
	 * @param v the value.
	 * @return the key.
	 */
	public Identifier getKey(V v) {
		var entry = entries.values().stream().filter(e -> e.getValue() == v).findFirst().orElse(null);
		return entry == null ? null : entry.getKey();
	}
	
	/**
	 * Registers the given key and value combination's entry.
	 * @param k the key.
	 * @param v the value.
	 * @return the entry.
	 */
	public RegistryEntry<V> register(Identifier k, V v) {
		values.put(k, v);
		return getEntry(k);
	}
	
	 /**
	 * Returns whether this registry contains the given key and value combination.
	  * @param k the key.
	  * @param v the value.
	 * @return whether this registry contains the given key and value combination.
	 */
	public boolean contains(Identifier k, V v) {
		return containsKey(k) && getEntry(k) == v;
	}
	
	/**
	 * Returns whether this registry contains the given key.
	 * @param k the key.
	 * @return whether this registry contains the given key.
	 */
	public boolean containsKey(Identifier k) {
		return entries.containsKey(k);
	}
	
	/**
	 * Returns whether this registry contains the given value.
	 * @param v the value.
	 * @return whether this registry contains the given value.
	 */
	public boolean containsValue(V v) {
		return entries.containsValue(v);
	}
	
	/**
	 * Returns a collection of this registry's keys.
	 */
	public Collection<Identifier> getKeys() {
		return values.keySet();
	}
	
	/**
	 * Returns a collection of this registry's values.
	 */
	public Collection<V> getValues() {
		return values.values();
	}
	
	/**
	 * Returns a collection of this registry's entries.
	 */
	public Collection<RegistryEntry<V>> getEntries() {
		return entries.values();
	}
	
	/**
	 * Serializes this registry to a {@link PacketByteBuf}.
	 * @param codec the codec to use.
	 * @param buf the buffer to serialize to.
	 */
	public void writeToBuf(Codec<Registry<V>> codec, PacketByteBuf buf) {
		var result = codec.encodeStart(NbtOps.INSTANCE, this);
		
		var nbt = new NbtCompound();
		nbt.put(VALUES, result.result().get());
		
		buf.writeNbt(nbt);
	}
	
	/**
	 * Deserializes this registry from a {@link PacketByteBuf}.
	 * @param codec the codec to use.
	 * @param buf the buffer to deserialize from.
	 */
	public void readFromBuf(Codec<Registry<V>> codec, PacketByteBuf buf) {
		var nbt = buf.readNbt();
		var entriesNbt = nbt.get(VALUES);
		
		codec.decode(NbtOps.INSTANCE, entriesNbt).result();
	}
	
	private class ReloadListener implements SimpleSynchronousResourceReloadListener {
		private final Identifier id;
		
		public ReloadListener() {
			this.id = AMCommon.id("registry_reload_listener_" + hashCode());
		}
		
		@Override
		public Identifier getFabricId() {
			return id;
		}
		
		@Override
		public void reload(ResourceManager manager) {
			getEntries().forEach(RegistryEntry::invalidate);
		}
	}
}
