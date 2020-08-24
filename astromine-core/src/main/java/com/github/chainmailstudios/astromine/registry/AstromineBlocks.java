/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

import net.minecraft.block.Block;
import net.minecraft.block.MagmaBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.AstromineBlockItem;
import com.github.chainmailstudios.astromine.common.item.AstromineEnergyBlockItem;
import com.github.chainmailstudios.astromine.common.utilities.EnergyCapacityProvider;

public class AstromineBlocks {
	public static final Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(30, 1500)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE = register("moon_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 0).strength(1, 3)), AstromineItems.getBasicSettings());
	public static final Block VULCAN_STONE = register("vulcan_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 4)), AstromineItems.getBasicSettings());
	public static final Block MARTIAN_SOIL = register("martian_soil", new Block(FabricBlockSettings.of(Material.SOIL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.SHOVELS, 0).strength(0.5f, 0.75f)), AstromineItems.getBasicSettings());
	public static final Block MARTIAN_STONE = register("martian_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 0).strength(1.5f, 6.0f)), AstromineItems.getBasicSettings());
	public static final Block BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", new MagmaBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500).lightLevel((state) -> 3).ticksRandomly().allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)), AstromineItems.getBasicSettings().fireproof());

	public static final Block METEOR_STONE_SLAB = register("meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_SLAB = register("asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_SLAB = register("moon_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineItems.getBasicSettings());
	public static final Block VULCAN_STONE_SLAB = register("vulcan_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineItems.getBasicSettings());
	public static final Block MARTIAN_STONE_SLAB = register("martian_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineItems.getBasicSettings());

	public static final Block METEOR_STONE_STAIRS = register("meteor_stone_stairs", new StairsBlock(METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", new StairsBlock(ASTEROID_STONE.getDefaultState(), FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_STAIRS = register("moon_stone_stairs", new StairsBlock(MOON_STONE.getDefaultState(), FabricBlockSettings.copyOf(MOON_STONE)), AstromineItems.getBasicSettings());
	public static final Block VULCAN_STONE_STAIRS = register("vulcan_stone_stairs", new StairsBlock(VULCAN_STONE.getDefaultState(), FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineItems.getBasicSettings());
	public static final Block MARTIAN_STONE_STAIRS = register("martian_stone_stairs", new StairsBlock(MARTIAN_STONE.getDefaultState(), FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineItems.getBasicSettings());

	public static final Block METEOR_STONE_WALL = register("meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_WALL = register("asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_WALL = register("moon_stone_wall", new WallBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineItems.getBasicSettings());
	public static final Block VULCAN_STONE_WALL = register("vulcan_stone_wall", new WallBlock(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineItems.getBasicSettings());
	public static final Block MARTIAN_STONE_WALL = register("martian_stone_wall", new WallBlock(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineItems.getBasicSettings());

	public static void initialize() {

	}

	/**
	 * @param name
	 *        Name of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 * @param settings
	 *        Item.Settings of BlockItem of Block instance to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, block instanceof EnergyCapacityProvider ? AstromineEnergyBlockItem.create(block, settings) : new AstromineBlockItem(block, settings));
	}

	/**
	 * @param name
	 *        Name of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 * @param item
	 *        BlockItem instance of Block to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(String name, T block, BlockItem item) {
		T b = register(AstromineCommon.identifier(name), block);
		if (item != null) {
			AstromineItems.register(name, item);
		}
		return b;
	}

	/**
	 * @param name
	 *        Name of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(String name, T block) {
		return register(AstromineCommon.identifier(name), block);
	}

	/**
	 * @param name
	 *        Identifier of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(Identifier name, T block) {
		return Registry.register(Registry.BLOCK, name, block);
	}

	public static FabricBlockSettings getPrimitiveSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL);
	}

	public static FabricBlockSettings getBasicSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE_TERRACOTTA).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL);
	}

	public static FabricBlockSettings getAdvancedSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(8, 6).sounds(BlockSoundGroup.METAL);
	}

	public static FabricBlockSettings getEliteSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.PINK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(8, 100).sounds(BlockSoundGroup.METAL);
	}

	public static FabricBlockSettings getCreativeSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.LIME).dropsNothing().strength(-1.0F, 3600000.8F).sounds(BlockSoundGroup.METAL);
	}
}
