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

package com.github.mixinors.astromine.common.recipe.ingredient;

import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

public final class FluidIngredient {
	private static final String FLUID_KEY = "fluid";
	private static final String AMOUNT_KEY = "amount";
	private static final String TAG_KEY = "tag";
	
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
	
	public boolean testVariant(FluidVariant testVariant) {
		return entry.testVariant(testVariant);
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
			var entryAsId = new Identifier(json.getAsString());
			var entryAsFluid = Registry.FLUID.get(entryAsId);
			var entryAsFluidVariant = FluidVariant.of(entryAsFluid);
			
			return new FluidIngredient(new VariantEntry(entryAsFluidVariant));
		}
		
		if (json.isJsonObject()) {
			var jsonObject = json.getAsJsonObject();
			
			if (jsonObject.has(FLUID_KEY)) {
				if (jsonObject.has(AMOUNT_KEY)) {
					var entryAsId = new Identifier(jsonObject.get(FLUID_KEY).getAsString());
					var entryAsFluid = Registry.FLUID.get(entryAsId);
					var entryAsFluidVariant = FluidVariant.of(entryAsFluid);
					
					var entryAmount = jsonObject.get(AMOUNT_KEY).getAsLong();
					
					return new FluidIngredient(new VariantEntry(entryAsFluidVariant, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get(FLUID_KEY).getAsString());
					var entryAsFluid = Registry.FLUID.get(entryAsId);
					var entryAsFluidVariant = FluidVariant.of(entryAsFluid);
					
					return new FluidIngredient(new VariantEntry(entryAsFluidVariant));
				}
			}
			
			if (jsonObject.has(TAG_KEY)) {
				if (jsonObject.has(AMOUNT_KEY)) {
					var entryAsId = new Identifier(jsonObject.get(TAG_KEY).getAsString());
					var entryAsTag = AMTagKeys.createFluidTag(entryAsId);
					
					var entryAmount = jsonObject.get(AMOUNT_KEY).getAsLong();
					
					return new FluidIngredient(new TagEntry(entryAsTag, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get(TAG_KEY).getAsString());
					var entryAsTag = AMTagKeys.createFluidTag(entryAsId);
					
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
			
			entryJsonObject.addProperty(FLUID_KEY, Registry.FLUID.getId(variantEntry.requiredVariant.getFluid()).toString());
			entryJsonObject.addProperty(AMOUNT_KEY, variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			var entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty(TAG_KEY, tagEntry.requiredTag.id().toString());
			entryJsonObject.addProperty(AMOUNT_KEY, tagEntry.requiredAmount);
		}
		
		return jsonObject;
	}
	
	public static FluidIngredient fromPacket(PacketByteBuf buf) {
		var entryType = buf.readString();
		var entryTypeId = new Identifier(buf.readString());
		
		var entryAmount = buf.readLong();
		
		if (entryType.equals(FLUID_KEY)) {
			var entryFluid = Registry.FLUID.get(entryTypeId);
			var entryVariant = FluidVariant.of(entryFluid);
			
			return new FluidIngredient(new VariantEntry(entryVariant, entryAmount));
		}
		
		if (entryType.equals(TAG_KEY)) {
			var entryTag = AMTagKeys.createFluidTag(entryTypeId);
			
			return new FluidIngredient(new TagEntry(entryTag, entryAmount));
		}
		
		return null;
	}
	
	public static void toPacket(PacketByteBuf buf, FluidIngredient ingredient) {
		if (ingredient.entry instanceof VariantEntry variantEntry) {
			buf.writeString(FLUID_KEY);
			buf.writeString(Registry.FLUID.getId(variantEntry.requiredVariant.getFluid()).toString());
			buf.writeLong(variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			buf.writeString(TAG_KEY);
			buf.writeString(tagEntry.requiredTag.id().toString());
			buf.writeLong(tagEntry.requiredAmount);
		}
	}
	
	public static abstract class Entry implements BiPredicate<FluidVariant, Long> {
		public abstract long getAmount();
		
		public abstract Collection<FluidVariant> getVariants();
		
		public abstract boolean testVariant(FluidVariant testVariant);
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
			return testVariant(testVariant) && testAmount >= requiredAmount;
		}
		
		@Override
		public boolean testVariant(FluidVariant testVariant) {
			return testVariant.equals(requiredVariant);
		}
		
		@Override
		public long getAmount() {
			return requiredAmount;
		}
		
		@Override
		public ImmutableCollection<FluidVariant> getVariants() {
			return ImmutableList.of(requiredVariant);
		}
	}
	
	public static class TagEntry extends Entry {
		private List<FluidVariant> requiredVariants;
		
		private final TagKey<Fluid> requiredTag;
		
		private final long requiredAmount;
		
		public TagEntry(TagKey<Fluid> tag) {
			this.requiredTag = tag;
			this.requiredAmount = 1;
		}
		
		public TagEntry(TagKey<Fluid> tag, long amount) {
			this.requiredTag = tag;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(FluidVariant testVariant, Long testAmount) {
			return testVariant(testVariant) && testAmount >= requiredAmount;
		}
		
		@Override
		public boolean testVariant(FluidVariant testVariant) {
			for (var requiredVariant : getVariants()) {
				if (requiredVariant.equals(testVariant)) {
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
			if (requiredVariants == null) {
				var builder = ImmutableList.<FluidVariant>builder();
				
				for (var entry : Registry.FLUID.iterateEntries(requiredTag)) {
					var fluid = entry.value();
					
					if (fluid.isStill(fluid.getDefaultState())) {
						builder.add(FluidVariant.of(fluid));
					}
				}
				
				requiredVariants = builder.build();
			}
			
			return requiredVariants;
		}
	}
}
