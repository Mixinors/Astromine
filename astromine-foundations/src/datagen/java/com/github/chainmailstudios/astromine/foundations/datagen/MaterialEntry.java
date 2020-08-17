package com.github.chainmailstudios.astromine.foundations.datagen;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialEntry {
	private final Identifier itemId;
	private final Identifier itemTagId;
	private Identifier dustId;
	private Identifier dustTagId;
	private Identifier tinyDustId;
	private Identifier tinyDustTagId;

	public MaterialEntry(Identifier itemId, Identifier itemTagId) {
		this.itemId = itemId;
		this.itemTagId = itemTagId;
	}

	public String getMaterialId() {
		return AstromineFoundationsDatagen.MATERIALS.inverse().get(this);
	}

	public Identifier getIngotItemId() {
		return itemId;
	}

	public Identifier getItemTagId() {
		return itemTagId;
	}

	public Identifier getDustId() {
		return dustId;
	}

	public Identifier getDustTagId() {
		return dustTagId;
	}

	public Identifier getTinyDustId() {
		return tinyDustId;
	}

	public Identifier getTinyDustTagId() {
		return tinyDustTagId;
	}

	public static MaterialEntry of(ItemConvertible item, String tagId) {
		return of(Registry.ITEM.getId(item.asItem()).toString(), tagId);
	}

	public static MaterialEntry of(String itemId, String tagId) {
		return new MaterialEntry(new Identifier(itemId), new Identifier(tagId));
	}

	public MaterialEntry dust(ItemConvertible dust) {
		return dust(Registry.ITEM.getId(dust.asItem()));
	}

	public MaterialEntry dust(Identifier dustId) {
		this.dustId = dustId;
		this.dustTagId = new Identifier("c", dustId.getPath() + "s");
		if (dustId.toString().contains("_dust")) {
			Identifier tinyDustId = new Identifier(dustId.toString().replace("_dust", "_tiny_dust"));
			if (Registry.ITEM.getOrEmpty(tinyDustId).isPresent())
				return tinyDust(tinyDustId);
		}
		return this;
	}

	public MaterialEntry dustTag(Identifier dustTagId) {
		this.dustTagId = dustTagId;
		return this;
	}

	public MaterialEntry tinyDust(ItemConvertible tinyDust) {
		return tinyDust(Registry.ITEM.getId(tinyDust.asItem()));
	}

	public MaterialEntry tinyDust(Identifier tinyDustId) {
		this.tinyDustId = tinyDustId;
		this.tinyDustTagId = new Identifier("c", tinyDustId.getPath() + "s");
		return this;
	}

	public Ingredient asIngredient() {
		return Ingredient.fromTag(TagRegistry.item(itemTagId));
	}

	@Override
	public String toString() {
		return "MaterialEntry{" +
		       "itemId=" + itemId +
		       ", itemTagId=" + itemTagId +
		       ", dustId=" + dustId +
		       ", dustTagId=" + dustTagId +
		       ", tinyDustId=" + tinyDustId +
		       ", tinyDustTagId=" + tinyDustTagId +
		       '}';
	}
}