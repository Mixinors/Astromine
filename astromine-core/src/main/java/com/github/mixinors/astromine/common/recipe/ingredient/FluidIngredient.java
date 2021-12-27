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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.architectury.hooks.tags.TagHooks;
import org.jetbrains.annotations.Nullable;

import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;

public final class FluidIngredient {
	private final Entry entry;
	
	@Nullable
	private FluidVariant[] matchingVariants;
	
	public FluidIngredient(Entry entry) {
		this.entry = entry;
	}
	
	public FluidIngredient(FluidVariant variant, long amount) {
		this.entry = new VariantEntry(variant, amount);
	}
	
	public boolean test(SingleSlotStorage<FluidVariant> testStorage) {
		return test(testStorage.getResource(), testStorage.getAmount());
	}
	
	public boolean test(FluidVariant testVariant, Long testAmount) {
		return entry.test(testVariant, testAmount);
	}
	
	public Entry getEntry() {
		return entry;
	}
	
	public long getAmount() {
		return entry.getAmount();
	}

	public FluidVariant[] getMatchingVariants() {
		this.cacheMatchingVariants();
		return this.matchingVariants;
	}

	private void cacheMatchingVariants() {
		if (this.matchingVariants == null) {
			this.matchingVariants = entry.getVariants().stream().distinct().toArray(FluidVariant[]::new);
		}
	}
	
	public static FluidIngredient fromJson(JsonElement json) {
		if (json.isJsonPrimitive()) {
			Identifier entryAsId = new Identifier(json.getAsString());
			Fluid entryAsFluid = Registry.FLUID.get(entryAsId);
			FluidVariant entryAsFluidVariant = FluidVariant.of(entryAsFluid);
			
			return new FluidIngredient(new VariantEntry(entryAsFluidVariant));
		}
		
		if (json.isJsonObject()) {
			JsonObject jsonObject = json.getAsJsonObject();
			
			if (jsonObject.has("fluid")) {
				if (jsonObject.has("amount")) {
					Identifier entryAsId = new Identifier(jsonObject.get("fluid").getAsString());
					Fluid entryAsFluid = Registry.FLUID.get(entryAsId);
					FluidVariant entryAsFluidVariant = FluidVariant.of(entryAsFluid);

					long entryAmount = jsonObject.get("amount").getAsLong();
					
					return new FluidIngredient(new VariantEntry(entryAsFluidVariant, entryAmount));
				} else {
					Identifier entryAsId = new Identifier(jsonObject.get("fluid").getAsString());
					Fluid entryAsFluid = Registry.FLUID.get(entryAsId);
					FluidVariant entryAsFluidVariant = FluidVariant.of(entryAsFluid);
					
					return new FluidIngredient(new VariantEntry(entryAsFluidVariant));
				}
			}
			
			if (jsonObject.has("tag")) {
				if (jsonObject.has("amount")) {
					Identifier entryAsId = new Identifier(jsonObject.get("tag").getAsString());
					Tag.Identified<Fluid> entryAsTag = TagHooks.optionalFluid(entryAsId);

					long entryAmount = jsonObject.get("amount").getAsLong();
					
					return new FluidIngredient(new TagEntry(entryAsTag, entryAmount));
				} else {
					Identifier entryAsId = new Identifier(jsonObject.get("tag").getAsString());
					Tag.Identified<Fluid> entryAsTag = TagHooks.optionalFluid(entryAsId);
					
					return new FluidIngredient(new TagEntry(entryAsTag));
				}
			}
		}
		
		return null;
	}
	
	public static JsonObject toJson(FluidIngredient ingredient) {
		JsonObject jsonObject = new JsonObject();
		
		if (ingredient.entry instanceof VariantEntry variantEntry) {
			JsonObject entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty("fluid", Registry.FLUID.getId(variantEntry.requiredVariant.getFluid()).toString());
			entryJsonObject.addProperty("amount", variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			JsonObject entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty("tag", ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.FLUID_KEY).getUncheckedTagId(tagEntry.requiredTag).toString());
			entryJsonObject.addProperty("amount", tagEntry.requiredAmount);
		}
		
		return jsonObject;
	}
	
	public static FluidIngredient fromPacket(PacketByteBuf buf) {
		String entryType = buf.readString();
		Identifier entryTypeId = new Identifier(buf.readString());

		long entryAmount = buf.readLong();
		
		if (entryType.equals("fluid")) {
			Fluid entryFluid = Registry.FLUID.get(entryTypeId);
			FluidVariant entryVariant = FluidVariant.of(entryFluid);
			
			return new FluidIngredient(new VariantEntry(entryVariant, entryAmount));
		}
		
		if (entryType.equals("tag")) {
			Tag.Identified<Fluid> entryTag = TagHooks.optionalFluid(entryTypeId);
			
			return new FluidIngredient(new TagEntry(entryTag, entryAmount));
		}
		
		return null;
	}
	
	public static void toPacket(PacketByteBuf buf, FluidIngredient ingredient) {
		if (ingredient.entry instanceof VariantEntry variantEntry) {
			buf.writeString("fluid");
			buf.writeString(Registry.FLUID.getId(variantEntry.requiredVariant.getFluid()).toString());
			buf.writeLong(variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			buf.writeString("tag");
			buf.writeString(ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.FLUID_KEY).getUncheckedTagId(tagEntry.requiredTag).toString());
			buf.writeLong(tagEntry.requiredAmount);
		}
	}
	
	public static abstract class Entry implements BiPredicate<FluidVariant, Long> {
		public abstract long getAmount();

		public abstract Collection<FluidVariant> getVariants();
	}
	
	public static class VariantEntry extends Entry {
		private final FluidVariant requiredVariant;
		private final long requiredAmount;
		
		public VariantEntry(FluidVariant variant) {
			this.requiredVariant = variant;
			this.requiredAmount = 1;
		}
		
		public VariantEntry(FluidVariant variant, long amount) {
			this.requiredVariant = variant;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(FluidVariant testVariant, Long testAmount) {
			return testVariant.equals(requiredVariant) && testAmount >= requiredAmount;
		}
		
		@Override
		public long getAmount() {
			return requiredAmount;
		}

		@Override
		public Collection<FluidVariant> getVariants() {
			return Collections.singleton(requiredVariant);
		}
	}
	
	public static class TagEntry extends Entry {
		private final Tag<Fluid> requiredTag;
		private List<FluidVariant> requiredVariants;
		private final long requiredAmount;
		
		public TagEntry(Tag<Fluid> tag) {
			this.requiredTag = tag;
			this.requiredAmount = 1;
		}
		
		public TagEntry(Tag<Fluid> tag, long amount) {
			this.requiredTag = tag;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(FluidVariant testVariant, Long testAmount) {
			if (requiredVariants == null) {
				requiredVariants = new ArrayList<>();
				
				for (Fluid fluid : requiredTag.values()) {
					requiredVariants.add(FluidVariant.of(fluid));
				}
			}
			
			for (FluidVariant requiredVariant : requiredVariants) {
				if (requiredVariant.equals(testVariant) && testAmount >= requiredAmount) {
					return true;
				}
			}
			
			return false;
		}
		
		@Override
		public long getAmount() {
			return requiredAmount;
		}

		@Override
		public Collection<FluidVariant> getVariants() {
			ArrayList<FluidVariant> list = new ArrayList<FluidVariant>();
			
			for (Fluid fluid : this.requiredTag.values()) {
				if (fluid.isStill(fluid.getDefaultState())) {
					list.add(FluidVariant.of(fluid));
				}
			}
			
			return list;
		}
	}
}
