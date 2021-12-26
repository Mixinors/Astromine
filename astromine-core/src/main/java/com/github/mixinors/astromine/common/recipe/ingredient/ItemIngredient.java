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

import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;

public final class ItemIngredient {
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
			
			if (jsonObject.has("item")) {
				if (jsonObject.has("count")) {
					var entryAsId = new Identifier(jsonObject.get("item").getAsString());
					var entryAsItem = Registry.ITEM.get(entryAsId);
					var entryAsItemVariant = ItemVariant.of(entryAsItem);
					
					var entryAmount = jsonObject.get("count").getAsInt();
					
					return new ItemIngredient(new VariantEntry(entryAsItemVariant, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get("item").getAsString());
					var entryAsItem = Registry.ITEM.get(entryAsId);
					var entryAsItemVariant = ItemVariant.of(entryAsItem);
					
					return new ItemIngredient(new VariantEntry(entryAsItemVariant));
				}
			}
			
			if (jsonObject.has("tag")) {
				if (jsonObject.has("count")) {
					var entryAsId = new Identifier(jsonObject.get("tag").getAsString());
					var entryAsTag = TagHooks.optionalItem(entryAsId);
					
					var entryAmount = jsonObject.get("count").getAsInt();
					
					return new ItemIngredient(new TagEntry(entryAsTag, entryAmount));
				} else {
					var entryAsId = new Identifier(jsonObject.get("tag").getAsString());
					var entryAsTag = TagHooks.optionalItem(entryAsId);
					
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
			
			entryJsonObject.addProperty("item", Registry.ITEM.getId(variantEntry.requiredVariant.getItem()).toString());
			entryJsonObject.addProperty("count", variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			var entryJsonObject = new JsonObject();
			
			entryJsonObject.addProperty("tag", ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.ITEM_KEY).getUncheckedTagId(tagEntry.requiredTag).toString());
			entryJsonObject.addProperty("count", tagEntry.requiredAmount);
		}
		
		return jsonObject;
	}
	
	public static ItemIngredient fromPacket(PacketByteBuf buf) {
		var entryType = buf.readString();
		var entryTypeId = new Identifier(buf.readString());
		
		var entryAmount = buf.readInt();
		
		if (entryType.equals("item")) {
			var entryItem = Registry.ITEM.get(entryTypeId);
			var entryVariant = ItemVariant.of(entryItem);
			
			return new ItemIngredient(new VariantEntry(entryVariant, entryAmount));
		}
		
		if (entryType.equals("tag")) {
			var entryTag = TagHooks.optionalItem(entryTypeId);
			
			return new ItemIngredient(new TagEntry(entryTag, entryAmount));
		}
		
		return null;
	}
	
	public static void toPacket(PacketByteBuf buf, ItemIngredient ingredient) {
		if (ingredient.entry instanceof VariantEntry variantEntry) {
			buf.writeString("item");
			buf.writeString(Registry.ITEM.getId(variantEntry.requiredVariant.getItem()).toString());
			buf.writeLong(variantEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			buf.writeString("tag");
			buf.writeString(ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.ITEM_KEY).getUncheckedTagId(tagEntry.requiredTag).toString());
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
			return Collections.singleton(requiredVariant);
		}
	}
	
	public static class TagEntry extends Entry {
		private final Tag<Item> requiredTag;
		private List<ItemVariant> requiredVariants;
		private final int requiredAmount;
		
		public TagEntry(Tag<Item> tag) {
			this.requiredTag = tag;
			this.requiredAmount = 1;
		}
		
		public TagEntry(Tag<Item> tag, int amount) {
			this.requiredTag = tag;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(ItemVariant testVariant, Long testAmount) {
			if (requiredVariants == null) {
				requiredVariants = new ArrayList<>();
				
				for (var item : requiredTag.values()) {
					requiredVariants.add(ItemVariant.of(item));
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
		public int getAmount() {
			return requiredAmount;
		}

		@Override
		public Collection<ItemVariant> getVariants() {
			ArrayList<ItemVariant> list = Lists.newArrayList();
			for (Item item : this.requiredTag.values()) {
				list.add(ItemVariant.of(item));
			}
			return list;
		}
	}
}
