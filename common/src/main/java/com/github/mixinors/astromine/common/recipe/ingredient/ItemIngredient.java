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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import com.mojang.serialization.JsonOps;

import com.github.mixinors.astromine.techreborn.common.util.StackUtils;
import io.netty.buffer.ByteBuf;

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

/**
 * A recipe ingredient consisting of (an) {@link ItemStack}(s).
 *
 * Serialization and deserialization methods are provided for:
 * - {@link JsonElement} - through {@link #toJson()} and {@link #fromJson(JsonElement)}.
 * - {@link ByteBuf} - through {@link #toPacket(PacketByteBuf)} and {@link #fromPacket(PacketByteBuf)}.
 */
public final  class ItemIngredient implements Predicate<ItemStack> {
	private final Entry[] entries;

	private ItemStack[] matchingStacks;

	private Ingredient ingredient;

	/** Instantiates am {@link ItemIngredient}. */
	private ItemIngredient(Entry... entries) {
		this.entries = entries;
	}

	/** Instantiates am {@link ItemIngredient}. */
	public static ItemIngredient ofItemStacks(ItemStack... stacks) {
		return ofItemStacks(Arrays.asList(stacks));
	}

	/** Instantiates am {@link ItemIngredient}. */
	public static ItemIngredient ofItemStacks(Collection<ItemStack> stacks) {
		return new ItemIngredient(new SimpleEntry(stacks));
	}

	/** Instantiates am {@link ItemIngredient}. */
	public static ItemIngredient ofEntries(Stream<? extends Entry> stacks) {
		return new ItemIngredient(stacks.toArray(Entry[]::new));
	}

	/** Deserializes an {@link ItemIngredient} from a {@link JsonElement}. */
	public static ItemIngredient fromJson(JsonElement json) {
		if (json != null && !json.isJsonNull()) {
			if (json.isJsonObject()) {
				return ofEntries(Stream.of(Entry.fromJson(json.getAsJsonObject())));
			} else if (json.isJsonArray()) {
				var jsonArray = json.getAsJsonArray();

				if (jsonArray.size() == 0) {
					throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
				} else {
					return ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map((jsonElement) -> Entry.fromJson(jsonElement.getAsJsonObject().get("item"))));
				}
			} else if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
				return ofEntries(Stream.generate(() -> new SimpleEntry(new ItemStack(Registry.ITEM.get(new Identifier(json.getAsString())), 1))));
			} else {
				throw new JsonSyntaxException("Expected item to be object or array of objects");
			}
		} else {
			throw new JsonSyntaxException("Item cannot be null");
		}
	}

	/** Serializes this {@link ItemIngredient} to a {@link JsonElement}. */
	public JsonElement toJson() {
		if (entries.length == 1 && entries[0] instanceof TagEntry) {
			var jsonObject = new JsonObject();

			jsonObject.addProperty("tag", ServerTagManagerHolder.getTagManager().getItems().getTagId(((TagEntry) entries[0]).tag).toString());
			jsonObject.addProperty("amount", ((TagEntry) entries[0]).count);

			return jsonObject;
		} else if (entries.length >= 1) {
			var jsonArray = new JsonArray();

			for (var entry : entries) {
				jsonArray.add(StackUtils.toJson(((SimpleEntry) entry).stacks.iterator().next()));
			}

			return jsonArray;
		} else {
			throw new UnsupportedOperationException("Cannot serialize an ItemIngredient with no entries");
		}
	}

	/** Deserializes an {@link ItemIngredient} from a {@link ByteBuf}. */
	public static ItemIngredient fromPacket(PacketByteBuf buffer) {
		var size = buffer.readVarInt();
		return ofEntries(Stream.generate(() -> new SimpleEntry(buffer.readItemStack())).limit(size));
	}

	/** Serializes an {@link ItemIngredient} to a {@link ByteBuf}. */
	public void toPacket(PacketByteBuf buffer) {
		this.cacheMatchingStacks();
		buffer.writeVarInt(this.matchingStacks.length);

		for (ItemStack matchingStack : this.matchingStacks) {
			buffer.writeItemStack(matchingStack);
		}
	}

	/** Returns the matching stacks of this ingredient. */
	public ItemStack[] getMatchingStacks() {
		this.cacheMatchingStacks();
		return this.matchingStacks;
	}

	/** Caches the matching stacks of this ingredient. */
	private void cacheMatchingStacks() {
		if (this.matchingStacks == null) {
			this.matchingStacks = Arrays.stream(this.entries).flatMap(Entry::getStacks).distinct().toArray(ItemStack[]::new);
		}
	}

	/** Returns this {@link ItemIngredient} as the standard {@link Ingredient}. */
	public Ingredient asIngredient() {
		if (ingredient == null) {
			ingredient = Ingredient.ofStacks(Stream.of(getMatchingStacks()));
		}
		
		return ingredient;
	}

	/** Asserts whether the given {@link ItemStack} has the
	 * same item, tag (if present), and equal or bigger amount, than any of the
	 * stacks of this ingredient or not. */
	@Override
	public boolean test(ItemStack stack) {
		return testMatching(stack) != ItemStack.EMPTY;
	}

	/** Asserts whether the given {@link ItemStack} has the same item and tag (if present)
	 * as any of the {@link ItemStack}s of this ingredient or not. */
	public boolean testWeak(ItemStack stack) {
		var matchingStacks = getMatchingStacks();
		
		if (this.matchingStacks.length == 0)
			return false;
		
		for (var matchingStack : matchingStacks) {
			if (ItemStack.areItemsEqual(stack, matchingStack) && (!matchingStack.hasTag() || ItemStack.areTagsEqual(stack, matchingStack))) return true;
		}
		
		return false;
	}

	/** Returns the first stack to have the same
	 * item, tag (if present), and equal or bigger count, than the
	 * given {@link ItemStack}. */
	public ItemStack testMatching(ItemStack stack) {
		if (stack == null)
			return ItemStack.EMPTY;
		
		var matchingStacks = getMatchingStacks();
		
		if (this.matchingStacks.length == 0)
			return ItemStack.EMPTY;
		
		for (var matchingStack : matchingStacks) {
			if (ItemStack.areItemsEqual(matchingStack, stack) && (!matchingStack.hasTag() || ItemStack.areTagsEqual(matchingStack, stack)) && stack.getCount() >= matchingStack.getCount())
				return matchingStack.copy();
		}
		
		return ItemStack.EMPTY;
	}

	/**
	 * A supplier of {@link ItemStack}s as a {@link Stream}.
	 */
	interface Entry {
		/** Returns the {@link ItemStack}s of this entry. */
		Stream<ItemStack> getStacks();

		/** Deserializes a {@link SimpleEntry} or {@link TagEntry}
		 * from a {@link JsonElement}. */
		static Entry fromJson(JsonElement jsonElement) {
			if (!jsonElement.isJsonObject()) throw new JsonParseException("An item ingredient entry must be an object");

			var jsonObject = jsonElement.getAsJsonObject();

			if (jsonObject.has("item") && jsonObject.has("tag")) {
				throw new JsonParseException("An item ingredient entry is either a tag or an item, not both");
			} else {
				var count = 1;

				var stackTag = new CompoundTag();

				if (jsonObject.has("count")) {
					count = JsonHelper.getInt(jsonObject, "count");
				}

				if (jsonObject.has("item")) {
					if (jsonObject.has("nbt")) {
						stackTag = (CompoundTag) JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, jsonObject.get("nbt"));
					}

					var itemId = new Identifier(JsonHelper.getString(jsonObject, "item"));

					var item = Registry.ITEM.getOrEmpty(itemId).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + itemId + "'"));

					var stack = new ItemStack(item, count);

					if (!stackTag.isEmpty()) {
						stack.setTag(stackTag);
					}

					return new SimpleEntry(stack);
				} else if (jsonObject.has("tag")) {
					var tagId = new Identifier(JsonHelper.getString(jsonObject, "tag"));

					var tag = ServerTagManagerHolder.getTagManager().getItems().getTag(tagId);

					if (tag == null) {
						throw new JsonSyntaxException("Unknown item tag '" + tagId + "'");
					} else {
						return new TagEntry(tag, count);
					}
				} else {
					throw new JsonParseException("An item ingredient entry needs either a tag or an item");
				}
			}
		}
	}

	/**
	 * An {@link Entry} of a fixed collection of {@link ItemStack}s.
	 */
	private static class SimpleEntry implements Entry {
		private final Collection<ItemStack> stacks;

		/** Instantiates a {@link SimpleEntry}. */
		public SimpleEntry(Collection<ItemStack> stacks) {
			this.stacks = stacks;
		}

		/** Instantiates a {@link SimpleEntry}. */
		public SimpleEntry(ItemStack stack) {
			this(Collections.singleton(stack));
		}

		/** Returns the {@link ItemStack}s of this entry. */
		@Override
		public Stream<ItemStack> getStacks() {
			return stacks.stream();
		}
	}

	/**
	 * An {@link Entry} of a dynamic collection of {@link ItemStack}s,
	 * gathered from a {@link Tag<Item>}.
	 */
	private static class TagEntry implements Entry {
		private final Tag<Item> tag;

		private final int count;

		/** Instantiates a {@link TagEntry}. */
		private TagEntry(Tag<Item> tag, int count) {
			this.tag = tag;
			this.count = count;
		}

		/** Returns the {@link ItemStack}s of this entry. */
		@Override
		public Stream<ItemStack> getStacks() {
			return this.tag.values().stream().map(item -> new ItemStack(item, count));
		}
	}
}
