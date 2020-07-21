package com.github.chainmailstudios.astromine.common.recipe;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagContainers;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BetterIngredient implements Predicate<ItemStack> {
	private final Entry[] entries;
	private ItemStack[] matchingStacks;
	private Ingredient ingredient;

	public static BetterIngredient ofItemStacks(ItemStack... stacks) {
		return ofItemStacks(Arrays.asList(stacks));
	}

	public static BetterIngredient ofItemStacks(Collection<ItemStack> stacks) {
		return new BetterIngredient(new SimpleEntry(stacks));
	}

	public static BetterIngredient ofEntries(Stream<? extends Entry> stacks) {
		return new BetterIngredient(stacks.toArray(Entry[]::new));
	}

	private BetterIngredient(Entry... entries) {
		this.entries = entries;
	}

	public static BetterIngredient fromJson(JsonElement json) {
		if (json != null && !json.isJsonNull()) {
			if (json.isJsonObject()) {
				return ofEntries(Stream.of(entryFromJson(json.getAsJsonObject())));
			} else if (json.isJsonArray()) {
				JsonArray jsonArray = json.getAsJsonArray();
				if (jsonArray.size() == 0) {
					throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
				} else {
					return ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map((jsonElement) -> {
						return entryFromJson(JsonHelper.asObject(jsonElement, "item"));
					}));
				}
			} else {
				throw new JsonSyntaxException("Expected item to be object or array of objects");
			}
		} else {
			throw new JsonSyntaxException("Item cannot be null");
		}
	}

	private static Entry entryFromJson(JsonObject json) {
		if (json.has("item") && json.has("tag")) {
			throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
		} else {
			int count = 1;
			if (json.has("count")) {
				count = JsonHelper.getInt(json, "count");
			}
			if (json.has("item")) {
				Identifier itemId = new Identifier(JsonHelper.getString(json, "item"));
				Item item = Registry.ITEM.getOrEmpty(itemId).orElseThrow(() -> {
					return new JsonSyntaxException("Unknown item '" + itemId + "'");
				});
				return new SimpleEntry(new ItemStack(item, count));
			} else if (json.has("tag")) {
				Identifier tagId = new Identifier(JsonHelper.getString(json, "tag"));
				Tag<Item> tag = TagContainers.instance().items().get(tagId);
				if (tag == null) {
					throw new JsonSyntaxException("Unknown item tag '" + tagId + "'");
				} else {
					return new TagEntry(tag, count);
				}
			} else {
				throw new JsonParseException("An ingredient entry needs either a tag or an item");
			}
		}
	}

	public static BetterIngredient fromPacket(PacketByteBuf buffer) {
		int i = buffer.readVarInt();
		return ofEntries(Stream.generate(() -> {
			return new SimpleEntry(buffer.readItemStack());
		}).limit(i));
	}

	public ItemStack[] getMatchingStacks() {
		this.cacheMatchingStacks();
		return this.matchingStacks;
	}

	private void cacheMatchingStacks() {
		if (this.matchingStacks == null) {
			this.matchingStacks = Arrays.stream(this.entries).flatMap(Entry::getStacks).distinct().toArray(ItemStack[]::new);
		}
	}

	public Ingredient asIngredient() {
		if (ingredient == null) {
			ingredient = Ingredient.method_26964(Stream.of(getMatchingStacks()));
		}
		return ingredient;
	}

	@Override
	public boolean test(ItemStack itemStack) {
		return testMatching(itemStack) != null;
	}

	public ItemStack testMatching(ItemStack itemStack) {
		if (itemStack == null) return null;
		ItemStack[] matchingStacks = getMatchingStacks();
		if (this.matchingStacks.length == 0) return null;
		for (ItemStack matchingStack : matchingStacks) {
			if (ItemStack.areItemsEqual(matchingStack, itemStack) && itemStack.getCount() >= matchingStack.getCount())
				return matchingStack.copy();
		}
		return null;
	}

	public void write(PacketByteBuf buffer) {
		this.cacheMatchingStacks();
		buffer.writeVarInt(this.matchingStacks.length);

		for (ItemStack matchingStack : this.matchingStacks) {
			buffer.writeItemStack(matchingStack);
		}
	}

	interface Entry {
		Stream<ItemStack> getStacks();
	}

	private static class SimpleEntry implements Entry {
		private final Collection<ItemStack> stacks;

		public SimpleEntry(Collection<ItemStack> stacks) {
			this.stacks = stacks;
		}

		public SimpleEntry(ItemStack itemStack) {
			this(Collections.singleton(itemStack));
		}

		@Override
		public Stream<ItemStack> getStacks() {
			return stacks.stream();
		}
	}

	private static class TagEntry implements Entry {
		private final Tag<Item> tag;
		private final int count;

		private TagEntry(Tag<Item> tag, int count) {
			this.tag = tag;
			this.count = count;
		}

		private TagEntry(Tag<Item> tag) {
			this(tag, 1);
		}

		@Override
		public Stream<ItemStack> getStacks() {
			return this.tag.values().stream().map(item -> new ItemStack(item, count));
		}
	}
}
