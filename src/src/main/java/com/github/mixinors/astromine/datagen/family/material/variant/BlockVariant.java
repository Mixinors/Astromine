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

package com.github.mixinors.astromine.datagen.family.material.variant;

import com.github.mixinors.astromine.common.fluid.ExtraFluidConstants;
import com.github.mixinors.astromine.common.util.WordUtils;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import com.github.mixinors.astromine.registry.common.AMTags;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

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
	
	public TagKey<Item> getItemTag() {
		return AMTags.ofItem(new Identifier("c", getTagPath()));
	}
	
	public int getMiningLevel() {
		return switch (this) {
			case ASTEROID_ORE, METEOR_ORE -> 4;
			default -> 0;
		};
	}
	
	public int getMiningLevel(MaterialFamily family) {
		return Math.max(family.getMiningLevel(), getMiningLevel());
	}
	
	public TagKey<Block> getMineableTag() {
		return BlockTags.PICKAXE_MINEABLE;
	}
	
	public AMDatagen.HarvestData getHarvestData() {
		return new AMDatagen.HarvestData(getMineableTag(), getMiningLevel());
	}
	
	public AMDatagen.HarvestData getHarvestData(MaterialFamily family) {
		return new AMDatagen.HarvestData(getMineableTag(), getMiningLevel(family));
	}
	
	@Override
	public TagKey<Block> createTag(Identifier id) {
		return AMTags.ofBlock(id);
	}

	public boolean isOre() {
		return switch(this) {
			case STONE_ORE, DEEPSLATE_ORE, METEOR_ORE, ASTEROID_ORE, NETHER_ORE -> true;
			default -> false;
		};
	}

	@Override
	public long getMeltedFluidAmount(boolean block2x2) {
		return switch(this) {
			case BLOCK -> FluidConstants.BLOCK;
			case STONE_ORE, DEEPSLATE_ORE, METEOR_ORE, ASTEROID_ORE, NETHER_ORE -> ExtraFluidConstants.nuggets(12, block2x2);
			case RAW_ORE_BLOCK -> ExtraFluidConstants.ingots(12, block2x2);
		};
	}

	@Override
	public float getMeltingTimeMultiplier() {
		return switch(this) {
			case BLOCK -> 10.0f;
			case STONE_ORE -> 2.5f;
			case DEEPSLATE_ORE -> 3.0f;
			case NETHER_ORE -> 2.2f;
			case METEOR_ORE -> 4.5f;
			case ASTEROID_ORE -> 5.0f;
			case RAW_ORE_BLOCK -> 12.0f;
		};
	}
}
