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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.AirlockBlock;
import com.github.mixinors.astromine.common.block.HoloBridgeInvisibleBlock;
import com.github.mixinors.astromine.common.block.HoloBridgeProjectorBlock;
import com.github.mixinors.astromine.common.block.NuclearWarheadBlock;
import com.github.mixinors.astromine.common.block.dynamic.DynamicBlock;
import com.github.mixinors.astromine.common.block.dynamic.DynamicSlabBlock;
import com.github.mixinors.astromine.common.block.dynamic.DynamicStairsBlock;
import com.github.mixinors.astromine.common.block.dynamic.DynamicWallBlock;
import com.github.mixinors.astromine.common.block.machine.*;
import com.github.mixinors.astromine.common.block.machine.generator.FluidGeneratorBlock;
import com.github.mixinors.astromine.common.block.machine.generator.SolidGeneratorBlock;
import com.github.mixinors.astromine.common.block.network.EnergyCableBlock;
import com.github.mixinors.astromine.common.block.network.FluidPipeBlock;
import com.github.mixinors.astromine.common.block.network.ItemConduitBlock;
import com.github.mixinors.astromine.common.block.ore.AsteroidOreBlock;
import com.github.mixinors.astromine.common.block.ore.DarkMoonStoneOreBlock;
import com.github.mixinors.astromine.common.block.ore.MoonStoneOreBlock;
import com.github.mixinors.astromine.common.block.ore.base.ExtendedOreBlock;
import com.github.mixinors.astromine.common.block.rocket.RocketControllerBlock;
import com.github.mixinors.astromine.common.block.rocket.RocketDoorBlock;
import com.github.mixinors.astromine.common.block.station.StationControllerBlock;
import com.github.mixinors.astromine.common.block.storage.BufferBlock;
import com.github.mixinors.astromine.common.block.storage.CapacitorBlock;
import com.github.mixinors.astromine.common.block.storage.TankBlock;
import com.github.mixinors.astromine.common.block.utility.*;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;
import java.util.function.Supplier;

public class AMBlocks {
	public static final RegistrySupplier<Block> ASTEROID_STONE = register("asteroid_stone", () -> new Block(FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(1.5F, 3F)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STONE_SLAB = register("asteroid_stone_slab", () -> new SlabBlock(FabricBlockSettings.copy(ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", () -> new StairsBlock(ASTEROID_STONE.get().getDefaultState(), FabricBlockSettings.copy(ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STONE_WALL = register("asteroid_stone_wall", () -> new WallBlock(FabricBlockSettings.copy(ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE = register("smooth_asteroid_stone", () -> new Block(FabricBlockSettings.copy(ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE_SLAB = register("smooth_asteroid_stone_slab", () -> new SlabBlock(FabricBlockSettings.copy(SMOOTH_ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE_STAIRS = register("smooth_asteroid_stone_stairs", () -> new StairsBlock(SMOOTH_ASTEROID_STONE.get().getDefaultState(), FabricBlockSettings.copy(SMOOTH_ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE_WALL = register("smooth_asteroid_stone_wall", () -> new WallBlock(FabricBlockSettings.copy(SMOOTH_ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> POLISHED_ASTEROID_STONE = register("polished_asteroid_stone", () -> new Block(FabricBlockSettings.copy(ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> POLISHED_ASTEROID_STONE_SLAB = register("polished_asteroid_stone_slab", () -> new SlabBlock(FabricBlockSettings.copy(POLISHED_ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> POLISHED_ASTEROID_STONE_STAIRS = register("polished_asteroid_stone_stairs", () -> new StairsBlock(POLISHED_ASTEROID_STONE.get().getDefaultState(), FabricBlockSettings.copy(POLISHED_ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICKS = register("asteroid_stone_bricks", () -> new Block(FabricBlockSettings.copy(ASTEROID_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICK_SLAB = register("asteroid_stone_brick_slab", () -> new SlabBlock(FabricBlockSettings.copy(ASTEROID_STONE_BRICKS.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICK_STAIRS = register("asteroid_stone_brick_stairs", () -> new StairsBlock(ASTEROID_STONE_BRICKS.get().getDefaultState(), FabricBlockSettings.copy(ASTEROID_STONE_BRICKS.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICK_WALL = register("asteroid_stone_brick_wall", () -> new WallBlock(FabricBlockSettings.copy(ASTEROID_STONE_BRICKS.get())), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", () -> new MagmaBlock(FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(50, 1500).luminance((state) -> 3).ticksRandomly().allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_METITE_ORE = register("asteroid_metite_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_ASTERITE_ORE = register("asteroid_asterite_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(40, 1000).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STELLUM_ORE = register("asteroid_stellum_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(25, 80).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_GALAXIUM_ORE = register("asteroid_galaxium_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(80, 1300).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_TIN_ORE = register("asteroid_tin_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_SILVER_ORE = register("asteroid_silver_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_LEAD_ORE = register("asteroid_lead_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_COAL_ORE = register("asteroid_coal_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_IRON_ORE = register("asteroid_iron_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_GOLD_ORE = register("asteroid_gold_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_COPPER_ORE = register("asteroid_copper_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", () -> new AsteroidOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> MOON_TIN_ORE = register("moon_tin_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_SILVER_ORE = register("moon_silver_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_LEAD_ORE = register("moon_lead_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> MOON_COAL_ORE = register("moon_coal_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_IRON_ORE = register("moon_iron_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_GOLD_ORE = register("moon_gold_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_COPPER_ORE = register("moon_copper_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_REDSTONE_ORE = register("moon_redstone_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_LAPIS_ORE = register("moon_lapis_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_DIAMOND_ORE = register("moon_diamond_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> MOON_EMERALD_ORE = register("moon_emerald_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 7).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> DARK_MOON_TIN_ORE = register("dark_moon_tin_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2, 8).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_SILVER_ORE = register("dark_moon_silver_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_LEAD_ORE = register("dark_moon_lead_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> DARK_MOON_COAL_ORE = register("dark_moon_coal_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_IRON_ORE = register("dark_moon_iron_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_GOLD_ORE = register("dark_moon_gold_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_COPPER_ORE = register("dark_moon_copper_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_REDSTONE_ORE = register("dark_moon_redstone_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_LAPIS_ORE = register("dark_moon_lapis_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_DIAMOND_ORE = register("dark_moon_diamond_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> DARK_MOON_EMERALD_ORE = register("dark_moon_emerald_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 8.5F).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> SPACE_SLIME_BLOCK = register("space_slime_block", () -> new SlimeBlock(FabricBlockSettings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.PURPLE)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> TIN_ORE = register("tin_ore", () -> new ExtendedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getSettings());
	public static final RegistrySupplier<Block> SILVER_ORE = register("silver_ore", () -> new ExtendedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getSettings());
	public static final RegistrySupplier<Block> LEAD_ORE = register("lead_ore", () -> new ExtendedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> DEEPSLATE_TIN_ORE = register("deepslate_tin_ore", () -> new ExtendedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.5f, 3).sounds(BlockSoundGroup.DEEPSLATE).mapColor(MapColor.DEEPSLATE_GRAY)), AMItems.getSettings());
	public static final RegistrySupplier<Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", () -> new ExtendedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.5f, 3).sounds(BlockSoundGroup.DEEPSLATE).mapColor(MapColor.DEEPSLATE_GRAY)), AMItems.getSettings());
	public static final RegistrySupplier<Block> DEEPSLATE_LEAD_ORE = register("deepslate_lead_ore", () -> new ExtendedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(4.5f, 3).sounds(BlockSoundGroup.DEEPSLATE).mapColor(MapColor.DEEPSLATE_GRAY)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> RAW_TIN_BLOCK = register("raw_tin_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(4, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> RAW_SILVER_BLOCK = register("raw_silver_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> RAW_LEAD_BLOCK = register("raw_lead_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_BLUE).requiresTool().strength(6, 8).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());

	public static final RegistrySupplier<Block> RAW_LUNUM_BLOCK = register("raw_lunum_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_BLUE).requiresTool().strength(6, 10).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());

	public static final RegistrySupplier<Block> METEOR_METITE_ORE = register("meteor_metite_ore", () -> new ExtendedOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getSettings().fireproof());

	public static final RegistrySupplier<Block> MOON_LUNUM_ORE = register("moon_lunum_ore", () -> new MoonStoneOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.STONE_GRAY).requiresTool().strength(6, 10).sounds(BlockSoundGroup.STONE)), AMItems.getSettings());
	public static final RegistrySupplier<Block> DARK_MOON_LUNUM_ORE = register("dark_moon_lunum_ore", () -> new DarkMoonStoneOreBlock(FabricBlockSettings.of(Material.STONE, MapColor.DEEPSLATE_GRAY).requiresTool().strength(7, 11).sounds(BlockSoundGroup.STONE)), AMItems.getSettings());

	public static final RegistrySupplier<Block> METITE_BLOCK = register("metite_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.PINK).requiresTool().strength(8, 100).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> ASTERITE_BLOCK = register("asterite_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.RED).requiresTool().strength(25, 1000).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> STELLUM_BLOCK = register("stellum_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(10, 80).sounds(BlockSoundGroup.METAL)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> GALAXIUM_BLOCK = register("galaxium_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(50, 1300).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> UNIVITE_BLOCK = register("univite_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(80, 2000).sounds(BlockSoundGroup.METAL)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> LUNUM_BLOCK = register("lunum_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(7, 25).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());

	public static final RegistrySupplier<Block> TIN_BLOCK = register("tin_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(4, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> SILVER_BLOCK = register("silver_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> LEAD_BLOCK = register("lead_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_BLUE).requiresTool().strength(6, 8).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> BRONZE_BLOCK = register("bronze_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_ORANGE).requiresTool().strength(6, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> STEEL_BLOCK = register("steel_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(8, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELECTRUM_BLOCK = register("electrum_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(6, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> FOOLS_GOLD_BLOCK = register("fools_gold_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.GOLD).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> METEORIC_STEEL_BLOCK = register("meteoric_steel_block", () -> new Block(FabricBlockSettings.of(Material.METAL, MapColor.GOLD).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> METEOR_STONE = register("meteor_stone", () -> new Block(FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(30, 1500)), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_SLAB = register("meteor_stone_slab", () -> new SlabBlock(FabricBlockSettings.copy(METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_STAIRS = register("meteor_stone_stairs", () -> new StairsBlock(METEOR_STONE.get().getDefaultState(), FabricBlockSettings.copy(METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_WALL = register("meteor_stone_wall", () -> new WallBlock(FabricBlockSettings.copy(METEOR_STONE.get())), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE = register("smooth_meteor_stone", () -> new Block(FabricBlockSettings.copy(METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE_SLAB = register("smooth_meteor_stone_slab", () -> new SlabBlock(FabricBlockSettings.copy(SMOOTH_METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE_STAIRS = register("smooth_meteor_stone_stairs", () -> new StairsBlock(SMOOTH_METEOR_STONE.get().getDefaultState(), FabricBlockSettings.copy(SMOOTH_METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE_WALL = register("smooth_meteor_stone_wall", () -> new WallBlock(FabricBlockSettings.copy(SMOOTH_METEOR_STONE.get())), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> POLISHED_METEOR_STONE = register("polished_meteor_stone", () -> new Block(FabricBlockSettings.copy(METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> POLISHED_METEOR_STONE_SLAB = register("polished_meteor_stone_slab", () -> new SlabBlock(FabricBlockSettings.copy(POLISHED_METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> POLISHED_METEOR_STONE_STAIRS = register("polished_meteor_stone_stairs", () -> new StairsBlock(POLISHED_METEOR_STONE.get().getDefaultState(), FabricBlockSettings.copy(POLISHED_METEOR_STONE.get())), AMItems.getSettings().fireproof());
	
	public static final RegistrySupplier<Block> METEOR_STONE_BRICKS = register("meteor_stone_bricks", () -> new Block(FabricBlockSettings.copy(METEOR_STONE.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_BRICK_SLAB = register("meteor_stone_brick_slab", () -> new SlabBlock(FabricBlockSettings.copy(METEOR_STONE_BRICKS.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_BRICK_STAIRS = register("meteor_stone_brick_stairs", () -> new StairsBlock(METEOR_STONE_BRICKS.get().getDefaultState(), FabricBlockSettings.copy(METEOR_STONE_BRICKS.get())), AMItems.getSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_BRICK_WALL = register("meteor_stone_brick_wall", () -> new WallBlock(FabricBlockSettings.copy(METEOR_STONE_BRICKS.get())), AMItems.getSettings().fireproof());

	public static final RegistrySupplier<Block> MOON_STONE = register("moon_stone", () -> new DynamicBlock(FabricBlockSettings.of(Material.STONE, MapColor.STONE_GRAY).requiresTool().strength(2, 7)), AMItems.getSettings());
	public static final RegistrySupplier<Block> MOON_STONE_SLAB = register("moon_stone_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> MOON_STONE_STAIRS = register("moon_stone_stairs", () -> new DynamicStairsBlock(MOON_STONE.get().getDefaultState(), FabricBlockSettings.copy(MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> MOON_STONE_WALL = register("moon_stone_wall", () -> new DynamicWallBlock(FabricBlockSettings.copy(MOON_STONE.get())), AMItems.getSettings());

	public static final RegistrySupplier<Block> SMOOTH_MOON_STONE = register("smooth_moon_stone", () -> new DynamicBlock(FabricBlockSettings.copy(MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> SMOOTH_MOON_STONE_SLAB = register("smooth_moon_stone_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(SMOOTH_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> SMOOTH_MOON_STONE_STAIRS = register("smooth_moon_stone_stairs", () -> new DynamicStairsBlock(SMOOTH_MOON_STONE.get().getDefaultState(), FabricBlockSettings.copy(SMOOTH_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> SMOOTH_MOON_STONE_WALL = register("smooth_moon_stone_wall", () -> new DynamicWallBlock(FabricBlockSettings.copy(SMOOTH_MOON_STONE.get())), AMItems.getSettings());

	public static final RegistrySupplier<Block> POLISHED_MOON_STONE = register("polished_moon_stone", () -> new DynamicBlock(FabricBlockSettings.copy(MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> POLISHED_MOON_STONE_SLAB = register("polished_moon_stone_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(POLISHED_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> POLISHED_MOON_STONE_STAIRS = register("polished_moon_stone_stairs", () -> new DynamicStairsBlock(POLISHED_MOON_STONE.get().getDefaultState(), FabricBlockSettings.copy(POLISHED_MOON_STONE.get())), AMItems.getSettings());

	public static final RegistrySupplier<Block> MOON_STONE_BRICKS = register("moon_stone_bricks", () -> new DynamicBlock(FabricBlockSettings.copy(MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> MOON_STONE_BRICK_SLAB = register("moon_stone_brick_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> MOON_STONE_BRICK_STAIRS = register("moon_stone_brick_stairs", () -> new DynamicStairsBlock(MOON_STONE_BRICKS.get().getDefaultState(), FabricBlockSettings.copy(MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> MOON_STONE_BRICK_WALL = register("moon_stone_brick_wall", () -> new DynamicWallBlock(FabricBlockSettings.copy(MOON_STONE_BRICKS.get())), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> DARK_MOON_STONE = register("dark_moon_stone", () -> new DynamicBlock(FabricBlockSettings.of(Material.STONE, MapColor.DEEPSLATE_GRAY).requiresTool().strength(3, 8.5F)), AMItems.getSettings());
	public static final RegistrySupplier<Block> DARK_MOON_STONE_SLAB = register("dark_moon_stone_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> DARK_MOON_STONE_STAIRS = register("dark_moon_stone_stairs", () -> new DynamicStairsBlock(DARK_MOON_STONE.get().getDefaultState(), FabricBlockSettings.copy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> DARK_MOON_STONE_WALL = register("dark_moon_stone_wall", () -> new DynamicWallBlock(FabricBlockSettings.copy(DARK_MOON_STONE.get())), AMItems.getSettings());

	public static final RegistrySupplier<Block> SMOOTH_DARK_MOON_STONE = register("smooth_dark_moon_stone", () -> new DynamicBlock(FabricBlockSettings.copy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> SMOOTH_DARK_MOON_STONE_SLAB = register("smooth_dark_moon_stone_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(SMOOTH_DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> SMOOTH_DARK_MOON_STONE_STAIRS = register("smooth_dark_moon_stone_stairs", () -> new DynamicStairsBlock(SMOOTH_DARK_MOON_STONE.get().getDefaultState(), FabricBlockSettings.copy(SMOOTH_DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> SMOOTH_DARK_MOON_STONE_WALL = register("smooth_dark_moon_stone_wall", () -> new DynamicWallBlock(FabricBlockSettings.copy(SMOOTH_DARK_MOON_STONE.get())), AMItems.getSettings());

	public static final RegistrySupplier<Block> POLISHED_DARK_MOON_STONE = register("polished_dark_moon_stone", () -> new DynamicBlock(FabricBlockSettings.copy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> POLISHED_DARK_MOON_STONE_SLAB = register("polished_dark_moon_stone_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(POLISHED_DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> POLISHED_DARK_MOON_STONE_STAIRS = register("polished_dark_moon_stone_stairs", () -> new DynamicStairsBlock(POLISHED_DARK_MOON_STONE.get().getDefaultState(), FabricBlockSettings.copy(POLISHED_DARK_MOON_STONE.get())), AMItems.getSettings());

	public static final RegistrySupplier<Block> DARK_MOON_STONE_BRICKS = register("dark_moon_stone_bricks", () -> new DynamicBlock(FabricBlockSettings.copy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> DARK_MOON_STONE_BRICK_SLAB = register("dark_moon_stone_brick_slab", () -> new DynamicSlabBlock(FabricBlockSettings.copy(DARK_MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> DARK_MOON_STONE_BRICK_STAIRS = register("dark_moon_stone_brick_stairs", () -> new DynamicStairsBlock(DARK_MOON_STONE_BRICKS.get().getDefaultState(), FabricBlockSettings.copy(DARK_MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final RegistrySupplier<Block> DARK_MOON_STONE_BRICK_WALL = register("dark_moon_stone_brick_wall", () -> new DynamicWallBlock(FabricBlockSettings.copy(DARK_MOON_STONE_BRICKS.get())), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", () -> new HoloBridgeProjectorBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", () -> new HoloBridgeInvisibleBlock(FabricBlockSettings.of(HoloBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().luminance($ -> 15).allowsSpawning((a, b, c, d) -> false)));
	
	public static final RegistrySupplier<Block> PRIMITIVE_TANK = register("primitive_tank", () -> new TankBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_TANK = register("basic_tank", () -> new TankBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_TANK = register("advanced_tank", () -> new TankBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_TANK = register("elite_tank", () -> new TankBlock.Elite(getEliteSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> CREATIVE_TANK = register("creative_tank", () -> new TankBlock.Creative(getCreativeSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", () -> new SolidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_SOLID_GENERATOR = register("basic_solid_generator", () -> new SolidGeneratorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", () -> new SolidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_SOLID_GENERATOR = register("elite_solid_generator", () -> new SolidGeneratorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_FLUID_GENERATOR = register("primitive_fluid_generator", () -> new FluidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_FLUID_GENERATOR = register("basic_fluid_generator", () -> new FluidGeneratorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_FLUID_GENERATOR = register("advanced_fluid_generator", () -> new FluidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_FLUID_GENERATOR = register("elite_fluid_generator", () -> new FluidGeneratorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ELECTRIC_FURNACE = register("primitive_electric_furnace", () -> new ElectricFurnaceBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_ELECTRIC_FURNACE = register("basic_electric_furnace", () -> new ElectricFurnaceBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_ELECTRIC_FURNACE = register("advanced_electric_furnace", () -> new ElectricFurnaceBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_ELECTRIC_FURNACE = register("elite_electric_furnace", () -> new ElectricFurnaceBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", () -> new AlloySmelterBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", () -> new AlloySmelterBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", () -> new AlloySmelterBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", () -> new AlloySmelterBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_TRITURATOR = register("primitive_triturator", () -> new TrituratorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_TRITURATOR = register("basic_triturator", () -> new TrituratorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_TRITURATOR = register("advanced_triturator", () -> new TrituratorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_TRITURATOR = register("elite_triturator", () -> new TrituratorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_PRESSER = register("primitive_press", () -> new PresserBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_PRESSER = register("basic_press", () -> new PresserBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_PRESSER = register("advanced_press", () -> new PresserBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_PRESSER = register("elite_press", () -> new PresserBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_WIRE_MILL = register("primitive_wire_mill", () -> new WireMillBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_WIRE_MILL = register("basic_wire_mill", () -> new WireMillBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_WIRE_MILL = register("advanced_wire_mill", () -> new WireMillBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_WIRE_MILL = register("elite_wire_mill", () -> new WireMillBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", () -> new ElectrolyzerBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_ELECTROLYZER = register("basic_electrolyzer", () -> new ElectrolyzerBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", () -> new ElectrolyzerBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_ELECTROLYZER = register("elite_electrolyzer", () -> new ElectrolyzerBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_REFINERY = register("primitive_refinery", () -> new RefineryBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_REFINERY = register("basic_refinery", () -> new RefineryBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_REFINERY = register("advanced_refinery", () -> new RefineryBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_REFINERY = register("elite_refinery", () -> new RefineryBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", () -> new FluidMixerBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_FLUID_MIXER = register("basic_fluid_mixer", () -> new FluidMixerBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", () -> new FluidMixerBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_FLUID_MIXER = register("elite_fluid_mixer", () -> new FluidMixerBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_SOLIDIFIER = register("primitive_solidifier", () -> new SolidifierBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_SOLIDIFIER = register("basic_solidifier", () -> new SolidifierBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_SOLIDIFIER = register("advanced_solidifier", () -> new SolidifierBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_SOLIDIFIER = register("elite_solidifier", () -> new SolidifierBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_MELTER = register("primitive_melter", () -> new MelterBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_MELTER = register("basic_melter", () -> new MelterBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_MELTER = register("advanced_melter", () -> new MelterBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_MELTER = register("elite_melter", () -> new MelterBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_BUFFER = register("primitive_buffer", () -> new BufferBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_BUFFER = register("basic_buffer", () -> new BufferBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_BUFFER = register("advanced_buffer", () -> new BufferBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_BUFFER = register("elite_buffer", () -> new BufferBlock.Elite(getEliteSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> CREATIVE_BUFFER = register("creative_buffer", () -> new BufferBlock.Creative(getCreativeSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> FLUID_EXTRACTOR = register("fluid_collector", () -> new FluidCollectorBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> FLUID_INSERTER = register("fluid_placer", () -> new FluidPlacerBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BLOCK_BREAKER = register("block_breaker", () -> new BlockBreakerBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BLOCK_PLACER = register("block_placer", () -> new BlockPlacerBlock(getAdvancedSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PUMP = register("pump", () -> new PumpBlock(getAdvancedSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> NUCLEAR_WARHEAD = register("nuclear_warhead", () -> new NuclearWarheadBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1F, 4F).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_CAPACITOR = register("primitive_capacitor", () -> new CapacitorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_CAPACITOR = register("basic_capacitor", () -> new CapacitorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_CAPACITOR = register("advanced_capacitor", () -> new CapacitorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_CAPACITOR = register("elite_capacitor", () -> new CapacitorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	public static final RegistrySupplier<Block> CREATIVE_CAPACITOR = register("creative_capacitor", () -> new CapacitorBlock.Creative(getCreativeSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> AIRLOCK = register("airlock", () -> new AirlockBlock(getBasicSettings()), block -> new TallBlockItem(block, AMItems.getSettings()));
	
	public static final RegistrySupplier<Block> DRAIN = register("drain", () -> new DrainBlock(getBasicSettings()), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> FLUID_PIPE = register("fluid_pipe", () -> new FluidPipeBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> ITEM_CONDUIT = register("item_conduit", () -> new ItemConduitBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ENERGY_CABLE = register("primitive_energy_cable", () -> new EnergyCableBlock.Primitive(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> BASIC_ENERGY_CABLE = register("basic_energy_cable", () -> new EnergyCableBlock.Basic(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> ADVANCED_ENERGY_CABLE = register("advanced_energy_cable", () -> new EnergyCableBlock.Advanced(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	public static final RegistrySupplier<Block> ELITE_ENERGY_CABLE = register("elite_energy_cable", () -> new EnergyCableBlock.Elite(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> ROCKET_CONTROLLER = register("rocket_controller", () -> new RocketControllerBlock(getAdvancedSettings().luminance(s -> 15)), AMItems.getSettings());
	public static final RegistrySupplier<Block> STATION_CONTROLLER = register("station_controller", () -> new StationControllerBlock(getAdvancedSettings().luminance(s -> 15)), AMItems.getSettings());
	
	public static final RegistrySupplier<Block> ROCKET_WALL = register("rocket_wall", () -> new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1.0F, 4.0F).sounds(BlockSoundGroup.METAL).luminance(15)), AMItems.getSettings());
	public static final RegistrySupplier<Block> ROCKET_WINDOW = register("rocket_window", () -> new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1.0F, 4.0F).sounds(BlockSoundGroup.METAL).luminance(15)), AMItems.getSettings());
	public static final RegistrySupplier<Block> ROCKET_DOOR = register("rocket_door", () -> new RocketDoorBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1.0F, 4.0F).sounds(BlockSoundGroup.METAL).luminance(15)), AMItems.getSettings());
	
	public static void init() {
	}
	
	/**
	 * @param name     Name of block instance to be registered
	 * @param block    Block instance to be registered
	 * @param settings Item.Settings of BlockItem of Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block, Item.Settings settings) {
		return register(name, block, b -> new BlockItem(b, settings));
	}
	
	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @param item  BlockItem instance of Block to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block, Function<T, BlockItem> item) {
		var b = register(AMCommon.id(name), block);
		if (item != null) {
			AMCommon.registry(Registry.ITEM_KEY).register(b.getId(), () -> {
				var t = b.get();
				var blockItem = item.apply(t);
				Item.BLOCK_ITEMS.put(t, blockItem);
				return blockItem;
			});
		}
		return b;
	}
	
	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block) {
		return register(AMCommon.id(name), block);
	}
	
	/**
	 * @param name  Identifier of block instance to be registered
	 * @param block Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(Identifier name, Supplier<T> block) {
		return AMCommon.registry(Registry.BLOCK_KEY).register(name, block);
	}
	
	public static AbstractBlock.Settings getPrimitiveSettings() {
		return AbstractBlock.Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(4, 6).sounds(BlockSoundGroup.METAL);
	}
	
	public static AbstractBlock.Settings getBasicSettings() {
		return AbstractBlock.Settings.of(Material.METAL, MapColor.TERRACOTTA_ORANGE).requiresTool().strength(6, 6).sounds(BlockSoundGroup.METAL);
	}
	
	public static AbstractBlock.Settings getAdvancedSettings() {
		return AbstractBlock.Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(8, 6).sounds(BlockSoundGroup.METAL);
	}
	
	public static AbstractBlock.Settings getEliteSettings() {
		return AbstractBlock.Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(8, 100).sounds(BlockSoundGroup.METAL);
	}
	
	public static AbstractBlock.Settings getCreativeSettings() {
		return AbstractBlock.Settings.of(Material.METAL, MapColor.LIME).dropsNothing().strength(-1.0F, 3600000.8F).sounds(BlockSoundGroup.METAL);
	}
}
