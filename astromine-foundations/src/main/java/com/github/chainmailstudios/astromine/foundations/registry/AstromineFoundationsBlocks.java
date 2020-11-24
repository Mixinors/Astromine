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

package com.github.chainmailstudios.astromine.foundations.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.sound.BlockSoundGroup;

import com.github.chainmailstudios.astromine.foundations.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;

public class AstromineFoundationsBlocks extends AstromineBlocks {
	// Materials - Ores
	public static final Block COPPER_ORE = register("copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineFoundationsItems.getBasicSettings());
	public static final Block TIN_ORE = register("tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineFoundationsItems.getBasicSettings());
	public static final Block SILVER_ORE = register("silver_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineFoundationsItems.getBasicSettings());
	public static final Block LEAD_ORE = register("lead_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineFoundationsItems.getBasicSettings());

	public static final Block METEOR_METITE_ORE = register("meteor_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineFoundationsItems
		.getBasicSettings().fireproof());

	// Material - Blocks
	public static final Block METITE_BLOCK = register("metite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PINK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(8, 100).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block ASTERITE_BLOCK = register("asterite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 1000).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block STELLUM_BLOCK = register("stellum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(10, 80).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings().fireproof());
	public static final Block GALAXIUM_BLOCK = register("galaxium_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PURPLE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(50, 1300).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block UNIVITE_BLOCK = register("univite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.WHITE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 6).strength(80, 2000).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings().fireproof());
	public static final Block LUNUM_BLOCK = register("lunum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.WHITE_TERRACOTTA).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(10, 75).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());

	public static final Block COPPER_BLOCK = register("copper_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block TIN_BLOCK = register("tin_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIGHT_GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block SILVER_BLOCK = register("silver_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIGHT_GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block LEAD_BLOCK = register("lead_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE_TERRACOTTA).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 8).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());

	public static final Block BRONZE_BLOCK = register("bronze_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE_TERRACOTTA).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block STEEL_BLOCK = register("steel_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(8, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block ELECTRUM_BLOCK = register("electrum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block ROSE_GOLD_BLOCK = register("rose_gold_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block STERLING_SILVER_BLOCK = register("sterling_silver_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIGHT_GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)),
		AstromineFoundationsItems.getBasicSettings());
	public static final Block FOOLS_GOLD_BLOCK = register("fools_gold_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL)), AstromineFoundationsItems
		.getBasicSettings());
	public static final Block METEORIC_STEEL_BLOCK = register("meteoric_steel_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL)),
		AstromineFoundationsItems.getBasicSettings());

	public static final Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(30, 1500)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_SLAB = register("meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_STAIRS = register("meteor_stone_stairs", new StairsBlock(METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_WALL = register("meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());

	public static final Block SMOOTH_METEOR_STONE = register("smooth_meteor_stone", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block SMOOTH_METEOR_STONE_SLAB = register("smooth_meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block SMOOTH_METEOR_STONE_STAIRS = register("smooth_meteor_stone_stairs", new StairsBlock(SMOOTH_METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block SMOOTH_METEOR_STONE_WALL = register("smooth_meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());

	public static final Block POLISHED_METEOR_STONE = register("polished_meteor_stone", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block POLISHED_METEOR_STONE_SLAB = register("polished_meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(POLISHED_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block POLISHED_METEOR_STONE_STAIRS = register("polished_meteor_stone_stairs", new StairsBlock(POLISHED_METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());

	public static final Block METEOR_STONE_BRICKS = register("meteor_stone_bricks", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_BRICK_SLAB = register("meteor_stone_brick_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_BRICK_STAIRS = register("meteor_stone_brick_stairs", new StairsBlock(METEOR_STONE_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AstromineFoundationsItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_BRICK_WALL = register("meteor_stone_brick_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AstromineFoundationsItems.getBasicSettings().fireproof());

	public static void initialize() {

	}
}
