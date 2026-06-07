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

import com.github.mixinors.astromine.common.transfer.storage.SimpleItemVariantStorage;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
	private ItemStack[] matchingStacks;
	
	public ItemIngredient(Entry entry) {
		this.entry = entry;
	}
	
	public ItemIngredient(ItemStack stack, int amount) {
		this.entry = new StackEntry(stack, amount);
	}
	
	public boolean test(SimpleItemVariantStorage testStorage) {
		if (testStorage.isResourceBlank()) {
			return false;
		}
		
		return test(testStorage.getResource(), testStorage.getAmount());
	}
	
	public boolean test(ItemStack testStack, Long testAmount) {
		return entry.test(testStack, testAmount);
	}
	
	public Entry getEntry() {
		return entry;
	}
	
	public int getAmount() {
		return entry.getAmount();
	}
	
	public Ingredient asIngredient() {
		return Ingredient.of(entry.getStacks().stream().map(stack -> stack.copyWithCount(entry.getAmount())));
	}
	
	public ItemStack[] getMatchingStacks() {
		this.cacheMatchingStacks();
		
		return this.matchingStacks;
	}
	
	private void cacheMatchingStacks() {
		if (this.matchingStacks == null) {
			this.matchingStacks = entry.getStacks().stream().distinct().toArray(ItemStack[]::new);
		}
	}
	
	public static ItemIngredient fromJson(JsonElement json) {
		if (json.isJsonPrimitive()) {
			var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(json.getAsString()));
			
			return new ItemIngredient(new StackEntry(new ItemStack(item)));
		}
		
		if (json.isJsonObject()) {
			var jsonObject = json.getAsJsonObject();
			
			if (jsonObject.has(ITEM_KEY)) {
				var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(jsonObject.get(ITEM_KEY).getAsString()));
				var amount = jsonObject.has(COUNT_KEY) ? jsonObject.get(COUNT_KEY).getAsInt() : 1;
				
				return new ItemIngredient(new StackEntry(new ItemStack(item), amount));
			}
			
			if (jsonObject.has(TAG_KEY)) {
				var tag = AMTagKeys.createItemTag(ResourceLocation.parse(jsonObject.get(TAG_KEY).getAsString()));
				var amount = jsonObject.has(COUNT_KEY) ? jsonObject.get(COUNT_KEY).getAsInt() : 1;
				
				return new ItemIngredient(new TagEntry(tag, amount));
			}
		}
		
		return null;
	}
	
	public static JsonObject toJson(ItemIngredient ingredient) {
		var jsonObject = new JsonObject();
		
		if (ingredient.entry instanceof StackEntry stackEntry) {
			jsonObject.addProperty(ITEM_KEY, BuiltInRegistries.ITEM.getKey(stackEntry.requiredStack.getItem()).toString());
			jsonObject.addProperty(COUNT_KEY, stackEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			jsonObject.addProperty(TAG_KEY, tagEntry.requiredTag.location().toString());
			jsonObject.addProperty(COUNT_KEY, tagEntry.requiredAmount);
		}
		
		return jsonObject;
	}
	
	public static ItemIngredient fromPacket(FriendlyByteBuf buf) {
		var entryType = buf.readUtf();
		var entryTypeId = ResourceLocation.parse(buf.readUtf());
		var entryAmount = buf.readInt();
		
		if (entryType.equals(ITEM_KEY)) {
			return new ItemIngredient(new StackEntry(new ItemStack(BuiltInRegistries.ITEM.get(entryTypeId)), entryAmount));
		}
		
		if (entryType.equals(TAG_KEY)) {
			return new ItemIngredient(new TagEntry(AMTagKeys.createItemTag(entryTypeId), entryAmount));
		}
		
		return null;
	}
	
	public static void toPacket(FriendlyByteBuf buf, ItemIngredient ingredient) {
		if (ingredient.entry instanceof StackEntry stackEntry) {
			buf.writeUtf(ITEM_KEY);
			buf.writeUtf(BuiltInRegistries.ITEM.getKey(stackEntry.requiredStack.getItem()).toString());
			buf.writeInt(stackEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			buf.writeUtf(TAG_KEY);
			buf.writeUtf(tagEntry.requiredTag.location().toString());
			buf.writeInt(tagEntry.requiredAmount);
		}
	}
	
	public static abstract class Entry implements BiPredicate<ItemStack, Long> {
		public abstract int getAmount();
		
		public abstract Collection<ItemStack> getStacks();
	}
	
	public static class StackEntry extends Entry {
		private final ItemStack requiredStack;
		private final int requiredAmount;
		
		public StackEntry(ItemStack stack) {
			this(stack, 1);
		}
		
		public StackEntry(ItemStack stack, int amount) {
			this.requiredStack = stack.copyWithCount(1);
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(ItemStack testStack, Long testAmount) {
			return ItemStack.isSameItemSameComponents(testStack, requiredStack) && testAmount >= requiredAmount;
		}
		
		@Override
		public int getAmount() {
			return requiredAmount;
		}
		
		@Override
		public Collection<ItemStack> getStacks() {
			return ImmutableList.of(requiredStack);
		}
	}
	
	public static class TagEntry extends Entry {
		private List<ItemStack> requiredStacks;
		private final TagKey<Item> requiredTag;
		private final int requiredAmount;
		
		public TagEntry(TagKey<Item> tag) {
			this(tag, 1);
		}
		
		public TagEntry(TagKey<Item> tag, int amount) {
			this.requiredTag = tag;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(ItemStack testStack, Long testAmount) {
			return testStack.is(requiredTag) && testAmount >= requiredAmount;
		}
		
		@Override
		public int getAmount() {
			return requiredAmount;
		}
		
		@Override
		public Collection<ItemStack> getStacks() {
			if (requiredStacks == null) {
				var builder = ImmutableList.<ItemStack>builder();
				
				for (var item : BuiltInRegistries.ITEM.getTagOrEmpty(requiredTag)) {
					builder.add(new ItemStack(item.value()));
				}
				
				requiredStacks = builder.build();
			}
			
			return requiredStacks;
		}
	}
}
