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

package com.github.chainmailstudios.astromine.common.recipe.ingredient;

import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FluidIngredient implements Predicate<FluidVolume> {
	private final Entry[] entries;
	private FluidVolume[] matchingVolumes;

	private FluidIngredient(Entry... entries) {
		this.entries = entries;
	}

	public static FluidIngredient ofFluidVolumes(FluidVolume... volumes) {
		return ofFluidVolumes(Arrays.asList(volumes));
	}

	public static FluidIngredient ofFluidVolumes(Collection<FluidVolume> volumes) {
		return new FluidIngredient(new SimpleEntry(volumes));
	}

	public static FluidIngredient ofEntries(Stream<? extends Entry> volumes) {
		return new FluidIngredient(volumes.toArray(Entry[]::new));
	}

	public static FluidIngredient fromJson(JsonElement json) {
		if (json != null && !json.isJsonNull()) {
			if (json.isJsonObject()) {
				return ofEntries(Stream.of(entryFromJson(json.getAsJsonObject())));
			} else if (json.isJsonArray()) {
				JsonArray jsonArray = json.getAsJsonArray();
				if (jsonArray.size() == 0) {
					throw new JsonSyntaxException("Fluid array cannot be empty, at least one fluid must be defined");
				} else {
					return ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map((jsonElement) -> entryFromJson(JsonHelper.asObject(jsonElement, "fluid"))));
				}
			} else {
				throw new JsonSyntaxException("Expected fluid to be object or array of objects");
			}
		} else {
			throw new JsonSyntaxException("Fluid cannot be null");
		}
	}

	private static Entry entryFromJson(JsonObject json) {
		if (json.has("fluid") && json.has("tag")) {
			throw new JsonParseException("A fluid ingredient entry is either a tag or a fluid, not both!");
		} else {
			int numerator = 1;
			int denominator = 1;
			if (json.has("amount")) {
				JsonObject amount = JsonHelper.getObject(json, "amount");
				if (amount.has("numerator")) {
					numerator = JsonHelper.getInt(amount, "numerator");
				}
				if (amount.has("denominator")) {
					denominator = JsonHelper.getInt(amount, "denominator");
				}
			}
			if (json.has("fluid")) {
				Identifier fluidId = new Identifier(JsonHelper.getString(json, "fluid"));
				Fluid fluid = Registry.FLUID.getOrEmpty(fluidId).orElseThrow(() -> new JsonSyntaxException("Unknown fluid '" + fluidId + "'!"));
				return new SimpleEntry(FluidVolume.of(new Fraction(numerator, denominator), fluid));
			} else if (json.has("tag")) {
				Identifier tagId = new Identifier(JsonHelper.getString(json, "tag"));
				Tag<Fluid> tag = ServerTagManagerHolder.getTagManager().getFluids().getTag(tagId);
				if (tag == null) {
					throw new JsonSyntaxException("Unknown fluid tag '" + tagId + "'!");
				} else {
					return new TagEntry(tag, numerator, denominator);
				}
			} else {
				throw new JsonParseException("A fluid ingredient entry needs either a tag or a fluid!");
			}
		}
	}

	public static FluidIngredient fromPacket(PacketByteBuf buffer) {
		int i = buffer.readVarInt();
		return ofEntries(Stream.generate(() -> new SimpleEntry(FluidVolume.fromPacket(buffer))).limit(i));
	}

	public FluidVolume getFirstMatchingVolume() {
		return getMatchingVolumes()[0].copy();
	}

	public FluidVolume[] getMatchingVolumes() {
		this.cacheMatchingVolumes();
		return this.matchingVolumes;
	}

	private void cacheMatchingVolumes() {
		if (this.matchingVolumes == null) {
			this.matchingVolumes = Arrays.stream(this.entries).flatMap(Entry::getVolumes).distinct().toArray(FluidVolume[]::new);
		}
	}

	@Override
	public boolean test(FluidVolume volume) {
		return testMatching(volume) != null;
	}

	public boolean test(Fluid fluid) {
		FluidVolume[] matchingVolumes = getMatchingVolumes();
		if (this.matchingVolumes.length == 0)
			return false;
		for (FluidVolume matchingVolume : matchingVolumes) {
			if (matchingVolume.getFluid().equals(fluid))
				return true;
		}
		return false;
	}

	public FluidVolume testMatching(FluidVolume volume) {
		if (volume == null)
			return null;
		FluidVolume[] matchingVolumes = getMatchingVolumes();
		if (this.matchingVolumes.length == 0)
			return null;
		for (FluidVolume matchingVolume : matchingVolumes) {
			if (FluidVolume.areFluidsEqual(matchingVolume, volume) && volume.getAmount().biggerOrEqualThan(matchingVolume.getAmount()))
				return matchingVolume.copy();
		}
		return null;
	}

	public void write(PacketByteBuf buffer) {
		this.cacheMatchingVolumes();
		buffer.writeVarInt(this.matchingVolumes.length);

		for (FluidVolume matchingVolume : this.matchingVolumes) {
			matchingVolume.toPacket(buffer);
		}
	}

	interface Entry {
		Stream<FluidVolume> getVolumes();
	}

	private static class SimpleEntry implements Entry {
		private final Collection<FluidVolume> volumes;

		public SimpleEntry(Collection<FluidVolume> volumes) {
			this.volumes = volumes;
		}

		public SimpleEntry(FluidVolume volume) {
			this(Collections.singleton(volume));
		}

		@Override
		public Stream<FluidVolume> getVolumes() {
			return volumes.stream();
		}
	}

	private static class TagEntry implements Entry {
		private final Tag<Fluid> tag;
		private final long numerator;
		private final long denominator;

		private TagEntry(Tag<Fluid> tag, long numerator, long denominator) {
			this.tag = tag;
			this.numerator = numerator;
			this.denominator = denominator;
		}

		private TagEntry(Tag<Fluid> tag) {
			this(tag, 1, 1);
		}

		@Override
		public Stream<FluidVolume> getVolumes() {
			return this.tag.values().stream().map(fluid -> FluidVolume.of(new Fraction(numerator, denominator), fluid));
		}
	}
}
