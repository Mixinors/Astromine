package com.github.chainmailstudios.astromine.datagen.material;

import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class MaterialEntry implements ItemConvertible {
	private final Identifier itemId;
	private final Optional<Identifier> itemTagId;

	public MaterialEntry(Identifier itemId) {
		this.itemId = itemId;
		this.itemTagId = Optional.empty();
	}

	public MaterialEntry(Identifier itemId, Identifier itemTagId) {
		this.itemId = itemId;
		this.itemTagId = Optional.of(itemTagId);
	}

	public MaterialEntry(Identifier itemId, String itemTagId) {
		this(itemId, new Identifier("c", itemTagId));
	}

	public Identifier getItemId() {
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

	public Identifier getItemTagId() {
		return itemTagId.get();
	}

	public Ingredient asIngredient() {
		if (hasItemTag()) return Ingredient.fromTag(asItemTag());
		else return Ingredient.ofItems(asItem());
	}

	@Override
	public Item asItem() {
		Item item = Registry.ITEM.get(itemId);
		if (item.equals(Items.AIR))
			throw new InvalidIdentifierException("oh fuck entry " + this.getItemId() + " returned air");
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