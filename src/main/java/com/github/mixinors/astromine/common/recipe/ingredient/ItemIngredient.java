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
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

public final class ItemIngredient {
	private static final String ITEM_KEY = "item";
	private static final String COUNT_KEY = "count";
	private static final String TAG_KEY = "tag";
	
	private final Entry entry;
	
	@Nullable
	private ItemVariant[] matchingVariants;
	
	public ItemIngredient(Entry entry) {
		this.entry = entry;
	}
	
	public ItemIngredient(ItemVariant variant, int amount) {
		this.entry = new VariantEntry(variant, amount);
	}
	
	public boolean test(SingleSlotStorage<ItemVariant> testStorage) {
		if (testStorage.isResourceBlank()) {
			return false;
		}
		return test(testStorage.getResource(), testStorage.getAmount());
	}
	
	public boolean test(ItemVariant testVariant, Long testAmount) {
		return entry.test(testVariant, testAmount);
	}
	
	public Entry getEntry() {
		return entry;
	}
	
	public int getAmount() {
		return entry.getAmount();
	}
	
	public Ingredient asIngredient() {
		return Ingredient.ofStacks(entry.getVariants().stream().map(variant -> variant.toStack(entry.getAmount())));
	}
	
	public ItemVariant[] getMatchingVariants() {
		this.cacheMatchingVariants();
		
		return this.matchingVariants;
	}
	
	private void cacheMatchingVariants() {
		if (this.matchingVariants == null) {
			this.matchingVariants = entry.getVariants().stream().distinct().toArray(ItemVariant[]::new);
		}
	}
	
	public static ItemIngredient fromJson(JsonElement json) {
		if (json.isJsonPrimitive()) {
			var entryAsId = new Identifier(json.getAsString());
			var entryAsItem = Registry.ITEM.get(entryAsId);
			var entryAsItemVariant = ItemVariant.of(entryAsItem);
			
			return new ItemIngredient(new VariantEntry(entryAsItemVariant));
		}
		
		if (json.isJsonObject()) {
			var jsonObject = json.getAsJsonObject();
			
			if (jsonObject.has(ITEM_KEY)) {
				if (jsonObject.has(COUNT_KEY)) {
					var entryAsId = new Identifier(jsonObject.get(ITEM_KEY).getAsString());
					var entryAsItem = Registry.ITEM.get(entryAsId);
					var entryAsItemVariant = ItemVariant.of(entryAsItem);
					
					var entryAmount = jsonObject.get(COUNT_KEY).getAsInt();
					
					return new ItemIngredient(new VariantEntry(entryAsItemVariant, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get(ITEM_KEY).getAsString());
					var entryAsItem = Registry.ITEM.get(entryAsId);
					var entryAsItemVariant = ItemVariant.of(entryAsItem);
					
					return new ItemIngredient(new VariantEntry(entryAsItemVariant));
				}
			}
			
			if (jsonObject.has(TAG_KEY)) {
				if (jsonObject.has(COUNT_KEY)) {
					var entryAsId = new Identifier(jsonObject.get(TAG_KEY).getAsString());
					var entryAsTag = AMTagKeys.createItemTag(entryAsId);
					
					var entryAmount = jsonObject.get(COUNT_KEY).getAsInt();
					
					return new ItemIngredient(new TagEntry(entryAsTag, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get(TAG_KEY).getAsString());
					var entryAsTag = AMTagKeys.createItemTag(entryAsId);
					
					return new ItemIngredient(new TagEntry(entryAsTag));
				}
			}
		}
		
		return null;
	}
	
	public static JsonObject toJson(ItemIngredient ingredient) {
		var jsonObject = new JsonObject();
		
		if (ingredient.entry instanceof VariantEntry variantEntry) {
			var entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty(ITEM_KEY, Registry.ITEM.getId(variantEntry.requiredVariant.getItem()).toString());
			entryJsonObject.addProperty(COUNT_KEY, variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			var entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty(TAG_KEY, tagEntry.requiredTag.id().toString());
			entryJsonObject.addProperty(COUNT_KEY, tagEntry.requiredAmount);
		}
		
		return jsonObject;
	}
	
	public static ItemIngredient fromPacket(PacketByteBuf buf) {
		var entryType = buf.readString();
		var entryTypeId = new Identifier(buf.readString());
		
		var entryAmount = buf.readInt();
		
		if (entryType.equals(ITEM_KEY)) {
			var entryItem = Registry.ITEM.get(entryTypeId);
			var entryVariant = ItemVariant.of(entryItem);
			
			return new ItemIngredient(new VariantEntry(entryVariant, entryAmount));
		}
		
		if (entryType.equals(TAG_KEY)) {
			var entryTag = AMTagKeys.createItemTag(entryTypeId);
			
			return new ItemIngredient(new TagEntry(entryTag, entryAmount));
		}
		
		return null;
	}
	
	public static void toPacket(PacketByteBuf buf, ItemIngredient ingredient) {
		if (ingredient.entry instanceof VariantEntry variantEntry) {
			buf.writeString(ITEM_KEY);
			buf.writeString(Registry.ITEM.getId(variantEntry.requiredVariant.getItem()).toString());
			buf.writeLong(variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			buf.writeString(TAG_KEY);
			buf.writeString(tagEntry.requiredTag.id().toString());
			buf.writeLong(tagEntry.requiredAmount);
		}
	}
	
	public static abstract class Entry implements BiPredicate<ItemVariant, Long> {
		public abstract int getAmount();
		
		public abstract Collection<ItemVariant> getVariants();
	}
	
	public static class VariantEntry extends Entry {
		private final ItemVariant requiredVariant;
		
		private final int requiredAmount;
		
		public VariantEntry(ItemVariant variant) {
			this.requiredVariant = variant;
			this.requiredAmount = 1;
		}
		
		public VariantEntry(ItemVariant variant, int amount) {
			this.requiredVariant = variant;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(ItemVariant testVariant, Long testAmount) {
			return testVariant.equals(requiredVariant) && testAmount >= requiredAmount;
		}
		
		@Override
		public int getAmount() {
			return requiredAmount;
		}
		
		@Override
		public Collection<ItemVariant> getVariants() {
			return ImmutableList.of(requiredVariant);
		}
	}
	
	public static class TagEntry extends Entry {
		private List<ItemVariant> requiredVariants;
		
		private final TagKey<Item> requiredTag;
		
		private final int requiredAmount;
		
		public TagEntry(TagKey<Item> tag) {
			this.requiredTag = tag;
			this.requiredAmount = 1;
		}
		
		public TagEntry(TagKey<Item> tag, int amount) {
			this.requiredTag = tag;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(ItemVariant testVariant, Long testAmount) {
			for (var requiredVariant : getVariants()) {
				if (requiredVariant.equals(testVariant) && testAmount >= requiredAmount) {
					return true;
				}
			}
			
			return false;
		}
		
		@Override
		public int getAmount() {
			return requiredAmount;
		}
		
		@Override
		public Collection<ItemVariant> getVariants() {
			if (requiredVariants == null) {
				var builder = ImmutableList.<ItemVariant>builder();
				
				for (var item : Registry.ITEM.iterateEntries(requiredTag)) {
					builder.add(ItemVariant.of(item.value()));
				}
				
				requiredVariants = builder.build();
			}
			
			return requiredVariants;
		}
	}
}
