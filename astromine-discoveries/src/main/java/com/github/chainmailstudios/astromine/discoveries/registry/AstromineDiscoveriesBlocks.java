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

package com.github.chainmailstudios.astromine.discoveries.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;

public class AstromineDiscoveriesBlocks extends AstromineBlocks {
	public static final Block ASTEROID_STONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_GRAY).breakByTool(FabricToolTags.PICKAXES, 3).requiresCorrectToolForDrops().strength(1.5F, 3F)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block MOON_STONE = register("moon_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_GRAY).breakByTool(FabricToolTags.PICKAXES, 0).requiresCorrectToolForDrops().strength(1, 3)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE = register("vulcan_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_GRAY).breakByTool(FabricToolTags.PICKAXES, 1).requiresCorrectToolForDrops().strength(3, 4)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_SOIL = register("martian_soil", new Block(FabricBlockSettings.of(Material.DIRT, MaterialColor.COLOR_RED).breakByTool(FabricToolTags.SHOVELS, 0).requiresCorrectToolForDrops().strength(0.5f, 0.75f)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE = register("martian_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_RED).breakByTool(FabricToolTags.PICKAXES, 0).requiresCorrectToolForDrops().strength(1.5f, 6.0f)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", new MagmaBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_GRAY).breakByTool(FabricToolTags.PICKAXES, 3).requiresCorrectToolForDrops().strength(50, 1500).lightLevel((state) -> 3).randomTicks()
		.isValidSpawn((state, world, pos, entityType) -> entityType.fireImmune()).hasPostProcess((state, world, pos) -> true).emissiveRendering((state, world, pos) -> true)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());

	public static final Block SMOOTH_ASTEROID_STONE = register("smooth_asteroid_stone", new Block(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block SMOOTH_VULCAN_STONE = register("smooth_vulcan_stone", new Block(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block SMOOTH_MARTIAN_STONE = register("smooth_martian_stone", new Block(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block POLISHED_ASTEROID_STONE = register("polished_asteroid_stone", new Block(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block POLISHED_VULCAN_STONE = register("polished_vulcan_stone", new Block(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block POLISHED_MARTIAN_STONE = register("polished_martian_stone", new Block(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_BRICKS = register("asteroid_stone_bricks", new Block(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block VULCAN_STONE_BRICKS = register("vulcan_stone_bricks", new Block(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_BRICKS = register("martian_stone_bricks", new Block(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_SLAB = register("asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block MOON_STONE_SLAB = register("moon_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE_SLAB = register("vulcan_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_SLAB = register("martian_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block SMOOTH_ASTEROID_STONE_SLAB = register("smooth_asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(SMOOTH_ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block SMOOTH_VULCAN_STONE_SLAB = register("smooth_vulcan_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(SMOOTH_VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block SMOOTH_MARTIAN_STONE_SLAB = register("smooth_martian_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(SMOOTH_MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block POLISHED_ASTEROID_STONE_SLAB = register("polished_asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(POLISHED_ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block POLISHED_VULCAN_STONE_SLAB = register("polished_vulcan_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(POLISHED_VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block POLISHED_MARTIAN_STONE_SLAB = register("polished_martian_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(POLISHED_MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_BRICK_SLAB = register("asteroid_stone_brick_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block VULCAN_STONE_BRICK_SLAB = register("vulcan_stone_brick_slab", new SlabBlock(FabricBlockSettings.copyOf(VULCAN_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_BRICK_SLAB = register("martian_stone_brick_slab", new SlabBlock(FabricBlockSettings.copyOf(MARTIAN_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", new StairBlock(ASTEROID_STONE.defaultBlockState(), FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block MOON_STONE_STAIRS = register("moon_stone_stairs", new StairBlock(MOON_STONE.defaultBlockState(), FabricBlockSettings.copyOf(MOON_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE_STAIRS = register("vulcan_stone_stairs", new StairBlock(VULCAN_STONE.defaultBlockState(), FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_STAIRS = register("martian_stone_stairs", new StairBlock(MARTIAN_STONE.defaultBlockState(), FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block SMOOTH_ASTEROID_STONE_STAIRS = register("smooth_asteroid_stone_stairs", new StairBlock(SMOOTH_ASTEROID_STONE.defaultBlockState(), FabricBlockSettings.copyOf(SMOOTH_ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block SMOOTH_VULCAN_STONE_STAIRS = register("smooth_vulcan_stone_stairs", new StairBlock(SMOOTH_VULCAN_STONE.defaultBlockState(), FabricBlockSettings.copyOf(SMOOTH_VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block SMOOTH_MARTIAN_STONE_STAIRS = register("smooth_martian_stone_stairs", new StairBlock(SMOOTH_MARTIAN_STONE.defaultBlockState(), FabricBlockSettings.copyOf(SMOOTH_MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block POLISHED_ASTEROID_STONE_STAIRS = register("polished_asteroid_stone_stairs", new StairBlock(POLISHED_ASTEROID_STONE.defaultBlockState(), FabricBlockSettings.copyOf(POLISHED_ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block POLISHED_VULCAN_STONE_STAIRS = register("polished_vulcan_stone_stairs", new StairBlock(POLISHED_VULCAN_STONE.defaultBlockState(), FabricBlockSettings.copyOf(POLISHED_VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block POLISHED_MARTIAN_STONE_STAIRS = register("polished_martian_stone_stairs", new StairBlock(POLISHED_MARTIAN_STONE.defaultBlockState(), FabricBlockSettings.copyOf(POLISHED_MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_BRICK_STAIRS = register("asteroid_stone_brick_stairs", new StairBlock(ASTEROID_STONE_BRICKS.defaultBlockState(), FabricBlockSettings.copyOf(ASTEROID_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block VULCAN_STONE_BRICK_STAIRS = register("vulcan_stone_brick_stairs", new StairBlock(VULCAN_STONE_BRICKS.defaultBlockState(), FabricBlockSettings.copyOf(VULCAN_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_BRICK_STAIRS = register("martian_stone_brick_stairs", new StairBlock(MARTIAN_STONE_BRICKS.defaultBlockState(), FabricBlockSettings.copyOf(MARTIAN_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_WALL = register("asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block MOON_STONE_WALL = register("moon_stone_wall", new WallBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE_WALL = register("vulcan_stone_wall", new WallBlock(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_WALL = register("martian_stone_wall", new WallBlock(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block SMOOTH_ASTEROID_STONE_WALL = register("smooth_asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(SMOOTH_ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block SMOOTH_VULCAN_STONE_WALL = register("smooth_vulcan_stone_wall", new WallBlock(FabricBlockSettings.copyOf(SMOOTH_VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block SMOOTH_MARTIAN_STONE_WALL = register("smooth_martian_stone_wall", new WallBlock(FabricBlockSettings.copyOf(SMOOTH_MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_BRICK_WALL = register("asteroid_stone_brick_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block VULCAN_STONE_BRICK_WALL = register("vulcan_stone_brick_wall", new WallBlock(FabricBlockSettings.copyOf(VULCAN_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_BRICK_WALL = register("martian_stone_brick_wall", new WallBlock(FabricBlockSettings.copyOf(MARTIAN_STONE_BRICKS)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_COPPER_ORE = register("asteroid_copper_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_TIN_ORE = register("asteroid_tin_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_LEAD_ORE = register("asteroid_lead_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());

	public static final Block ASTEROID_COAL_ORE = register("asteroid_coal_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_IRON_ORE = register("asteroid_iron_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_GOLD_ORE = register("asteroid_gold_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());
	public static final Block ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", new OreBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireResistant());

	public static final Block SPACE_SLIME_BLOCK = register("space_slime_block", new SlimeBlock(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK).materialColor(MaterialColor.COLOR_PURPLE)), AstromineDiscoveriesItems.getBasicSettings());

	public static void initialize() {

	}
}
