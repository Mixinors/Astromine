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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.hooks.tags.TagHooks;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public final class FluidIngredient {
	private final Entry entry;
	
	public FluidIngredient(Entry entry) {
		this.entry = entry;
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
	
	public static FluidIngredient fromJson(JsonElement json) {
		if (json.isJsonPrimitive()) {
			var entryAsId = new Identifier(json.getAsString());
			var entryAsFluid = Registry.FLUID.get(entryAsId);
			var entryAsFluidVariant = FluidVariant.of(entryAsFluid);
			
			return new FluidIngredient(new VariantEntry(entryAsFluidVariant));
		}
		
		if (json.isJsonObject()) {
			var jsonObject = json.getAsJsonObject();
			
			if (jsonObject.has("fluid")) {
				if (jsonObject.has("amount")) {
					var entryAsId = new Identifier(jsonObject.get("fluid").getAsString());
					var entryAsFluid = Registry.FLUID.get(entryAsId);
					var entryAsFluidVariant = FluidVariant.of(entryAsFluid);
					
					var entryAmount = jsonObject.get("amount").getAsLong();
					
					return new FluidIngredient(new VariantEntry(entryAsFluidVariant, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get("fluid").getAsString());
					var entryAsFluid = Registry.FLUID.get(entryAsId);
					var entryAsFluidVariant = FluidVariant.of(entryAsFluid);
					
					return new FluidIngredient(new VariantEntry(entryAsFluidVariant));
				}
			}
			
			if (jsonObject.has("tag")) {
				if (jsonObject.has("amount")) {
					var entryAsId = new Identifier(jsonObject.get("tag").getAsString());
					var entryAsTag = TagHooks.optionalFluid(entryAsId);
					
					var entryAmount = jsonObject.get("amount").getAsLong();
					
					return new FluidIngredient(new TagEntry(entryAsTag, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get("tag").getAsString());
					var entryAsTag = TagHooks.optionalFluid(entryAsId);
					
					return new FluidIngredient(new TagEntry(entryAsTag));
				}
			}
		}
		
		return null;
	}
	
	public static JsonObject toJson(FluidIngredient ingredient) {
		var jsonObject = new JsonObject();
		
		if (ingredient.entry instanceof VariantEntry variantEntry) {
			var entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty("fluid", Registry.FLUID.getId(variantEntry.requiredVariant.getFluid()).toString());
			entryJsonObject.addProperty("amount", variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			var entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty("tag", ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.FLUID_KEY).getUncheckedTagId(tagEntry.requiredTag).toString());
			entryJsonObject.addProperty("amount", tagEntry.requiredAmount);
		}
		
		return jsonObject;
	}
	
	public static FluidIngredient fromPacket(PacketByteBuf buf) {
		var entryType = buf.readString();
		var entryTypeId = new Identifier(buf.readString());
		
		var entryAmount = buf.readLong();
		
		if (entryType.equals("fluid")) {
			var entryFluid = Registry.FLUID.get(entryTypeId);
			var entryVariant = FluidVariant.of(entryFluid);
			
			return new FluidIngredient(new VariantEntry(entryVariant, entryAmount));
		}
		
		if (entryType.equals("tag")) {
			var entryTag = TagHooks.optionalFluid(entryTypeId);
			
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
				
				for (var fluid : requiredTag.values()) {
					requiredVariants.add(FluidVariant.of(fluid));
				}
			}
			
			for (var requiredVariant : requiredVariants) {
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
	}
}
