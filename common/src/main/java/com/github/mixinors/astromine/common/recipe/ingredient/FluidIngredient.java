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

package com.github.mixinors.astromine.common.recipe.ingredient;

import com.google.gson.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.common.util.StringUtils;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A recipe ingredient consisting of (a) {@link Fluid}(s) and its
 * {@link long}(s) fractional amount(s).
 *
 * Serialization and deserialization methods are provided for:
 * - {@link JsonElement} - through {@link #toJson()} and {@link #fromJson(JsonElement)}.
 * - {@link ByteBuf} - through {@link #toPacket(PacketByteBuf)} and {@link #fromPacket(PacketByteBuf)}.
 */
public final class FluidIngredient implements Predicate<FluidVolume> {
	private final Entry[] entries;

	private FluidVolume[] matchingVolumes;

	/** Instantiates a {@link FluidIngredient}. */
	private FluidIngredient(Entry... entries) {
		this.entries = entries;
	}

	/** Instantiates a {@link FluidIngredient}. */
	public static FluidIngredient ofFluidVolumes(FluidVolume... volumes) {
		return ofFluidVolumes(Arrays.asList(volumes));
	}

	/** Instantiates a {@link FluidIngredient}. */
	public static FluidIngredient ofFluidVolumes(Collection<FluidVolume> volumes) {
		return new FluidIngredient(new SimpleEntry(volumes));
	}

	/** Instantiates a {@link FluidIngredient}. */
	public static FluidIngredient ofEntries(Stream<? extends Entry> volumes) {
		return new FluidIngredient(volumes.toArray(Entry[]::new));
	}

	/** Deserializes a {@link FluidIngredient} from a {@link JsonElement}. */
	public static FluidIngredient fromJson(JsonElement json) {
		if (json != null && !json.isJsonNull()) {
			if (json.isJsonObject()) {
				return ofEntries(Stream.of(Entry.fromJson(json)));
			} else if (json.isJsonArray()) {
				JsonArray jsonArray = json.getAsJsonArray();

				if (jsonArray.size() == 0) {
					throw new JsonSyntaxException("Fluid array cannot be empty, at least one fluid must be defined");
				} else {
					return ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map((jsonElement) -> Entry.fromJson(jsonElement.getAsJsonObject().get("fluid"))));
				}
			} else if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
				return ofEntries(Stream.generate(() -> new SimpleEntry(FluidVolume.of(FluidVolume.BUCKET, Registry.FLUID.get(new Identifier(json.getAsString()))))).limit(1));
			} else {
				throw new JsonSyntaxException("Expected fluid to be object or array of objects");
			}
		} else {
			throw new JsonSyntaxException("Fluid cannot be null");
		}
	}

	/** Serializes this {@link FluidIngredient} to a {@link JsonElement}. */
	public JsonElement toJson() {
		if (entries.length == 1 && entries[0] instanceof TagEntry) {
			JsonObject jsonObject = new JsonObject();

			jsonObject.addProperty("tag", ServerTagManagerHolder.getTagManager().getFluids().getTagId(((TagEntry) entries[0]).tag).toString());
			jsonObject.addProperty("amount", ((TagEntry) entries[0]).amount);

			return jsonObject;
		} else if (entries.length >= 1) {
			JsonArray jsonArray = new JsonArray();

			for (Entry entry : entries) {
				jsonArray.add(((SimpleEntry) entry).volumes.iterator().next().toJson());
			}

			return jsonArray;
		} else {
			throw new UnsupportedOperationException("Cannot serialize a FluidIngredient with no entries");
		}
	}

	/** Deserializes a {@link FluidIngredient} from a {@link ByteBuf}. */
	public static FluidIngredient fromPacket(PacketByteBuf buffer) {
		int size = buffer.readVarInt();
		return ofEntries(Stream.generate(() -> new SimpleEntry(FluidVolume.fromPacket(buffer))).limit(size));
	}

	/** Serializes a {@link ItemIngredient} to a {@link ByteBuf}. */
	public PacketByteBuf toPacket(PacketByteBuf buffer) {
		this.cacheMatchingVolumes();

		buffer.writeVarInt(this.matchingVolumes.length);

		for (FluidVolume matchingVolume : this.matchingVolumes) {
			matchingVolume.toPacket(buffer);
		}

		return buffer;
	}

	/** Returns the matching volumes of this ingredient. */
	public FluidVolume[] getMatchingVolumes() {
		this.cacheMatchingVolumes();
		return this.matchingVolumes;
	}

	/** Caches the matching volumes of this ingredient. */
	private void cacheMatchingVolumes() {
		if (this.matchingVolumes == null) {
			this.matchingVolumes = Arrays.stream(this.entries).flatMap(Entry::getVolumes).distinct().toArray(FluidVolume[]::new);
		}
	}

	/** Asserts whether the given {@link FluidVolume} has the
	 * same fluid and equal or bigger amount than any of the
	 * volumes of this ingredient or not. */
	@Override
	public boolean test(FluidVolume volume) {
		return testMatching(volume) != null;
	}

	/** Asserts whether the given {@link Fluid} has the same fluid
	 * as any of the volumes of this ingredient or not. */
	public boolean testWeak(FluidVolume volume) {
		FluidVolume[] matchingVolumes = getMatchingVolumes();
		if (this.matchingVolumes.length == 0)
			return false;
		for (FluidVolume matchingVolume : matchingVolumes) {
			if (matchingVolume.getFluid().equals(volume.getFluid()))
				return true;
		}
		return false;
	}

	/** Returns the first volume to have the same
	 * fluid and equal or bigger amount than the
	 * given {@link FluidVolume}. */
	public FluidVolume testMatching(FluidVolume volume) {
		if (volume == null)
			return null;
		FluidVolume[] matchingVolumes = getMatchingVolumes();
		if (this.matchingVolumes.length == 0)
			return null;
		for (FluidVolume matchingVolume : matchingVolumes) {
			if (FluidVolume.fluidsEqual(matchingVolume, volume) && volume.getAmount() >= matchingVolume.getAmount())
				return matchingVolume.copy();
		}
		return null;
	}

	/**
	 * A supplier of {@link FluidVolume}s as a {@link Stream}.
	 */
	interface Entry {
		/** Returns the volumes of this entry. */
		Stream<FluidVolume> getVolumes();

		/** Deserializes a {@link SimpleEntry} or {@link TagEntry}
		 * from a {@link JsonElement}. */
		static Entry fromJson(JsonElement jsonElement) {
			if (!jsonElement.isJsonObject()) throw new JsonParseException("A fluid ingredient entry must be an object");

			JsonObject jsonObject = jsonElement.getAsJsonObject();

			if (jsonObject.has("fluid") && jsonObject.has("tag")) {
				throw new JsonParseException("A fluid ingredient entry is either a tag or a fluid, not both!");
			} else {
				long amount = FluidVolume.BUCKET;

				if (jsonObject.has("amount")) {
					amount = jsonObject.get("amount").getAsLong();
				}

				if (jsonObject.has("fluid")) {
					Identifier fluidId = new Identifier(StringUtils.fromJson(jsonObject.get("fluid")));

					Fluid fluid = Registry.FLUID.getOrEmpty(fluidId).orElseThrow(() -> new JsonSyntaxException("Unknown fluid '" + fluidId + "'!"));

					return new SimpleEntry(FluidVolume.of(amount, fluid));
				} else if (jsonObject.has("tag")) {
					Identifier tagId = new Identifier(StringUtils.fromJson(jsonObject.get("tag")));

					Tag<Fluid> tag = ServerTagManagerHolder.getTagManager().getFluids().getTag(tagId);

					if (tag == null) {
						throw new JsonSyntaxException("Unknown fluid tag '" + tagId + "'!");
					} else {
						return new TagEntry(tag, amount);
					}
				} else {
					throw new JsonParseException("A fluid ingredient entry needs either a tag or a fluid!");
				}
			}
		}
	}

	/**
	 * An {@link Entry} of a fixed collection of {@link FluidVolume}s.
	 */
	private static class SimpleEntry implements Entry {
		private final Collection<FluidVolume> volumes;

		/** Instantiates a {@link SimpleEntry}. */
		public SimpleEntry(Collection<FluidVolume> volumes) {
			this.volumes = volumes;
		}

		/** Instantiates a {@link SimpleEntry}. */
		public SimpleEntry(FluidVolume volume) {
			this(Collections.singleton(volume));
		}

		/** Returns the volumes of this entry. */
		@Override
		public Stream<FluidVolume> getVolumes() {
			return volumes.stream();
		}
	}

	/**
	 * An {@link Entry} of a dynamic collection of {@link FluidVolume}s,
	 * gathered from a {@link Tag<Fluid>}.
	 */
	private static class TagEntry implements Entry {
		private final Tag<Fluid> tag;
		private final long amount;

		/** Instantiates a {@link TagEntry}. */
		private TagEntry(Tag<Fluid> tag, long amount) {
			this.tag = tag;
			this.amount = amount;
		}

		/** Returns the volumes of this entry. */
		@Override
		public Stream<FluidVolume> getVolumes() {
			return this.tag.values().stream().map(fluid -> FluidVolume.of(amount, fluid));
		}
	}
}
