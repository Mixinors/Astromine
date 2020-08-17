package com.github.chainmailstudios.astromine.foundations.datagen;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialEntry {
	private final Identifier itemId;
	private final Identifier tagId;

	public MaterialEntry(Identifier itemId, Identifier tagId) {
		this.itemId = itemId;
		this.tagId = tagId;
	}

	public String getMaterialId() {
		return AstromineFoundationsDatagen.MATERIALS.inverse().get(this);
	}

	public Identifier getIngotItemId() {
		return itemId;
	}

	public Identifier getTagId() {
		return tagId;
	}

	public static MaterialEntry of(ItemConvertible item, String tagId) {
		return of(Registry.ITEM.getId(item.asItem()).toString(), tagId);
	}

	public static MaterialEntry of(String itemId, String tagId) {
		return new MaterialEntry(new Identifier(itemId), new Identifier(tagId));
	}

	public Ingredient asIngredient() {
		return Ingredient.fromTag(TagRegistry.item(tagId));
	}
}