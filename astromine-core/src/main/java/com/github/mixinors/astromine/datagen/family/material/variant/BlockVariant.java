package com.github.mixinors.astromine.datagen.family.material.variant;

import java.util.function.BiConsumer;

import com.github.mixinors.astromine.common.util.WordUtils;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;

import net.minecraft.block.Block;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.tag.TagFactory;

public enum BlockVariant implements Variant<Block> {
	BLOCK("block"),
	STONE_ORE("stone_ore"),
	DEEPSLATE_ORE("deepslate_ore"),
	NETHER_ORE("nether_ore"),
	METEOR_ORE("meteor_ore"),
	ASTEROID_ORE("asteroid_ore"),
	RAW_ORE_BLOCK("raw_ore_block", "raw", "blocks");

	private final String name;
	private final String path;
	private final String prefix;
	private final String suffix;

	BlockVariant(String name) {
		this(name, WordUtils.pluralize(name));
	}

	BlockVariant(String name, String path) {
		this(name, path, "", path);
	}

	BlockVariant(String name, String prefix, String suffix) {
		this(name, WordUtils.pluralize(name), prefix, suffix);
	}

	BlockVariant(String name, String path, String prefix, String suffix) {
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
	public BiConsumer<BlockStateModelGenerator, Block> getModelRegistrar() {
		return switch (this) {
			case METEOR_ORE -> AMModelProvider::registerMeteorOre;
			case ASTEROID_ORE -> AMModelProvider::registerAsteroidOre;
			default -> BlockStateModelGenerator::registerSimpleCubeAll;
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
	public TagFactory<Block> getTagFactory() {
		return TagFactory.BLOCK;
	}

	public Tag.Identified<Item> getItemTag() {
		return TagFactory.ITEM.create(new Identifier("c", getTagPath()));
	}
}
