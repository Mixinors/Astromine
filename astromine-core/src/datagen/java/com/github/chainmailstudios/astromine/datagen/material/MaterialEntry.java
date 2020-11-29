package com.github.chainmailstudios.astromine.datagen.material;

import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class MaterialEntry implements ItemLike {
	private final ResourceLocation itemId;
	private final Optional<ResourceLocation> itemTagId;

	public MaterialEntry(ResourceLocation itemId) {
		this.itemId = itemId;
		this.itemTagId = Optional.empty();
	}

	public MaterialEntry(ResourceLocation itemId, ResourceLocation itemTagId) {
		this.itemId = itemId;
		this.itemTagId = Optional.of(itemTagId);
	}

	public MaterialEntry(ResourceLocation itemId, String itemTagId) {
		this(itemId, new ResourceLocation("c", itemTagId));
	}

	public ResourceLocation getItemId() {
		return itemId;
	}

	public String getName() {
		return getItemId().getPath();
	}

	public boolean hasItemTag() {
		return itemTagId.isPresent();
	}

	public boolean isBlock() {
		return asBlock() != Blocks.AIR;
	}

	public ResourceLocation getItemTagId() {
		return itemTagId.get();
	}

	public Ingredient asIngredient() {
		if (hasItemTag()) return Ingredient.of(asItemTag());
		else return Ingredient.of(asItem());
	}

	@Override
	public Item asItem() {
		Item item = Registry.ITEM.get(itemId);
		if (item.equals(Items.AIR))
			throw new ResourceLocationException("oh fuck entry " + this.getItemId() + " returned air");
		return item;
	}

	public Block asBlock() {
		return Registry.BLOCK.get(itemId);
	}

	private Tag<Item> asItemTag() {
		return TagRegistry.item(getItemTagId());
	}

	public boolean isFromVanilla() {
		return itemId.getNamespace().equals("minecraft");
	}
}