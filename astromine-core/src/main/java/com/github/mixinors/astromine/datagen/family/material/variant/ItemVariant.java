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

package com.github.mixinors.astromine.datagen.family.material.variant;

import java.util.function.BiConsumer;

import com.github.mixinors.astromine.common.util.WordUtils;

import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.Models;
import net.minecraft.item.Item;

import net.fabricmc.fabric.api.tag.TagFactory;

public enum ItemVariant implements Variant<Item> {
	INGOT("ingot"),
	GEM("gem", true, true),
	MISC("misc", true, true),
	NUGGET("nugget"),
	RAW_ORE("raw_ore", "raw", ""),
	METEOR_ORE_CLUSTER("meteor_ore_cluster"),
	ASTEROID_ORE_CLUSTER("asteroid_ore_cluster"),
	DUST("dust"),
	TINY_DUST("tiny_dust"),
	GEAR("gear"),
	PLATE("plate"),
	WIRE("wire"),
	PICKAXE("pickaxe"),
	AXE("axe"),
	SHOVEL("shovel"),
	SWORD("sword"),
	HOE("hoe"),
	HELMET("helmet"),
	CHESTPLATE("chestplate"),
	LEGGINGS("leggings", false),
	BOOTS("boots", false),
	APPLE("apple", "metal_apples", "", "apples"),
	HORSE_ARMOR("horse_armor", "horse_armor"),
	BALL("ball");

	private final String name;
	private final String path;
	private final String prefix;
	private final String suffix;

	ItemVariant(String name) {
		this(name, true);
	}

	ItemVariant(String name, boolean pluralize) {
		this(name, pluralize ? WordUtils.pluralize(name) : name);
	}

	ItemVariant(String name, boolean pluralize, boolean simple) {
		this(name, pluralize ? WordUtils.pluralize(name) : name, "", simple ? "" : pluralize ? WordUtils.pluralize(name) : name);
	}

	ItemVariant(String name, String path) {
		this(name, path, "", path);
	}

	ItemVariant(String name, String prefix, String suffix) {
		this(name, WordUtils.pluralize(name), prefix, suffix);
	}

	ItemVariant(String name, String path, String prefix, String suffix) {
		this.name = name;
		this.path = path;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public BiConsumer<ItemModelGenerator, Item> getModelRegistrar() {
		return switch (this) {
			case PICKAXE, AXE, SHOVEL, SWORD, HOE -> (itemModelGenerator, item) -> itemModelGenerator.register(item, Models.HANDHELD);
			default -> (itemModelGenerator, item) -> itemModelGenerator.register(item, Models.GENERATED);
		};
	}

	@Override
	public String getTagPath() {
		return path;
	}

	@Override
	public String getTagPrefix() {
		return prefix;
	}

	@Override
	public String getTagSuffix() {
		return suffix;
	}

	@Override
	public boolean hasTag() {
		return switch (this) {
			case MISC, BALL -> false;
			default -> true;
		};
	}

	@Override
	public TagFactory<Item> getTagFactory() {
		return TagFactory.ITEM;
	}
}
