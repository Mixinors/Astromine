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

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;

public class AstromineFoundationsBlocks extends AstromineBlocks {
	// Materials - Ores
	public static final Block COPPER_ORE = register("copper_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sound(SoundType.STONE)), AstromineFoundationsItems.getBasicSettings());
	public static final Block IRIDIUM_ORE = register("iridium_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 1).requiresCorrectToolForDrops().strength(3, 3).sound(SoundType.STONE)), AstromineFoundationsItems.getBasicSettings());
	public static final Block TIN_ORE = register("tin_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 1).requiresCorrectToolForDrops().strength(3, 3).sound(SoundType.STONE)), AstromineFoundationsItems.getBasicSettings());
	public static final Block LEAD_ORE = register("lead_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 2).requiresCorrectToolForDrops().strength(3, 3).sound(SoundType.STONE)), AstromineFoundationsItems.getBasicSettings());

	public static final Block COPPER_BLOCK = register("copper_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_ORANGE).breakByTool(FabricToolTags.PICKAXES, 1).requiresCorrectToolForDrops().strength(4, 6).sound(SoundType.METAL)), AstromineFoundationsItems.getBasicSettings());
	public static final Block TIN_BLOCK = register("tin_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES, 1).requiresCorrectToolForDrops().strength(4, 6).sound(SoundType.METAL)), AstromineFoundationsItems.getBasicSettings());
	public static final Block LEAD_BLOCK = register("lead_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.TERRACOTTA_BLUE).breakByTool(FabricToolTags.PICKAXES, 2).requiresCorrectToolForDrops().strength(6, 8).sound(SoundType.METAL)), AstromineFoundationsItems.getBasicSettings());
	public static final Block BRONZE_BLOCK = register("bronze_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.TERRACOTTA_ORANGE).breakByTool(FabricToolTags.PICKAXES, 2).requiresCorrectToolForDrops().strength(6, 6).sound(SoundType.METAL)), AstromineFoundationsItems.getBasicSettings());
	public static final Block STEEL_BLOCK = register("steel_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_GRAY).breakByTool(FabricToolTags.PICKAXES, 2).requiresCorrectToolForDrops().strength(8, 6).sound(SoundType.METAL)), AstromineFoundationsItems.getBasicSettings());

	public static final Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_BLACK).breakByTool(FabricToolTags.PICKAXES, 3).requiresCorrectToolForDrops().strength(30, 1500)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block METEOR_STONE_SLAB = register("meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block METEOR_STONE_STAIRS = register("meteor_stone_stairs", new StairBlock(METEOR_STONE.defaultBlockState(), FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block METEOR_STONE_WALL = register("meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());

	public static final Block SMOOTH_METEOR_STONE = register("smooth_meteor_stone", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block SMOOTH_METEOR_STONE_SLAB = register("smooth_meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block SMOOTH_METEOR_STONE_STAIRS = register("smooth_meteor_stone_stairs", new StairBlock(SMOOTH_METEOR_STONE.defaultBlockState(), FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block SMOOTH_METEOR_STONE_WALL = register("smooth_meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());

	public static final Block POLISHED_METEOR_STONE = register("polished_meteor_stone", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block POLISHED_METEOR_STONE_SLAB = register("polished_meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(POLISHED_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block POLISHED_METEOR_STONE_STAIRS = register("polished_meteor_stone_stairs", new StairBlock(POLISHED_METEOR_STONE.defaultBlockState(), FabricBlockSettings.copyOf(POLISHED_METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());

	public static final Block METEOR_STONE_BRICKS = register("meteor_stone_bricks", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block METEOR_STONE_BRICK_SLAB = register("meteor_stone_brick_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block METEOR_STONE_BRICK_STAIRS = register("meteor_stone_brick_stairs", new StairBlock(METEOR_STONE_BRICKS.defaultBlockState(), FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AstromineFoundationsItems.getBasicSettings().fireResistant());
	public static final Block METEOR_STONE_BRICK_WALL = register("meteor_stone_brick_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AstromineFoundationsItems.getBasicSettings().fireResistant());

	public static void initialize() {

	}
}
