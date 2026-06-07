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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Function;
import java.util.function.Supplier;

public class AMBlocks {
	static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, AMCommon.MOD_ID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, AMCommon.MOD_ID);
	
	public static final DeferredHolder<Block, Block> ASTEROID_STONE = register("asteroid_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_STONE_SLAB = register("asteroid_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", () -> new StairBlock(ASTEROID_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_STONE_WALL = register("asteroid_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> SMOOTH_ASTEROID_STONE = register("smooth_asteroid_stone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> SMOOTH_ASTEROID_STONE_SLAB = register("smooth_asteroid_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> SMOOTH_ASTEROID_STONE_STAIRS = register("smooth_asteroid_stone_stairs", () -> new StairBlock(SMOOTH_ASTEROID_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SMOOTH_ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> SMOOTH_ASTEROID_STONE_WALL = register("smooth_asteroid_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> POLISHED_ASTEROID_STONE = register("polished_asteroid_stone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> POLISHED_ASTEROID_STONE_SLAB = register("polished_asteroid_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> POLISHED_ASTEROID_STONE_STAIRS = register("polished_asteroid_stone_stairs", () -> new StairBlock(POLISHED_ASTEROID_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> ASTEROID_STONE_BRICKS = register("asteroid_stone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_STONE_BRICK_SLAB = register("asteroid_stone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE_BRICKS.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_STONE_BRICK_STAIRS = register("asteroid_stone_brick_stairs", () -> new StairBlock(ASTEROID_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE_BRICKS.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_STONE_BRICK_WALL = register("asteroid_stone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ASTEROID_STONE_BRICKS.get())), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", () -> new MagmaBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(50, 1500).lightLevel((state) -> 3).randomTicks().isValidSpawn((state, world, pos, entityType) -> entityType.fireImmune()).hasPostProcess((state, world, pos) -> true).emissiveRendering((state, world, pos) -> true)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> ASTEROID_METITE_ORE = register("asteroid_metite_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_ASTERITE_ORE = register("asteroid_asterite_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(40, 1000).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_STELLUM_ORE = register("asteroid_stellum_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(25, 80).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_GALAXIUM_ORE = register("asteroid_galaxium_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(80, 1300).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> ASTEROID_TIN_ORE = register("asteroid_tin_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_SILVER_ORE = register("asteroid_silver_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_LEAD_ORE = register("asteroid_lead_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> ASTEROID_COAL_ORE = register("asteroid_coal_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_IRON_ORE = register("asteroid_iron_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_GOLD_ORE = register("asteroid_gold_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_COPPER_ORE = register("asteroid_copper_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", () -> new AsteroidOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> MOON_TIN_ORE = register("moon_tin_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_SILVER_ORE = register("moon_silver_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_LEAD_ORE = register("moon_lead_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> MOON_COAL_ORE = register("moon_coal_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_IRON_ORE = register("moon_iron_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_GOLD_ORE = register("moon_gold_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_COPPER_ORE = register("moon_copper_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_REDSTONE_ORE = register("moon_redstone_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_LAPIS_ORE = register("moon_lapis_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_DIAMOND_ORE = register("moon_diamond_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> MOON_EMERALD_ORE = register("moon_emerald_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 7).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> DARK_MOON_TIN_ORE = register("dark_moon_tin_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(2, 8).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_SILVER_ORE = register("dark_moon_silver_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_LEAD_ORE = register("dark_moon_lead_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> DARK_MOON_COAL_ORE = register("dark_moon_coal_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_IRON_ORE = register("dark_moon_iron_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_GOLD_ORE = register("dark_moon_gold_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_COPPER_ORE = register("dark_moon_copper_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_REDSTONE_ORE = register("dark_moon_redstone_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_LAPIS_ORE = register("dark_moon_lapis_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_DIAMOND_ORE = register("dark_moon_diamond_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> DARK_MOON_EMERALD_ORE = register("dark_moon_emerald_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 8.5F).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> SPACE_SLIME_BLOCK = register("space_slime_block", () -> new SlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).mapColor(MapColor.COLOR_PURPLE)), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> TIN_ORE = register("tin_ore", () -> new ExtendedOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 3).sound(SoundType.STONE)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SILVER_ORE = register("silver_ore", () -> new ExtendedOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 3).sound(SoundType.STONE)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> LEAD_ORE = register("lead_ore", () -> new ExtendedOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3, 3).sound(SoundType.STONE)), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> DEEPSLATE_TIN_ORE = register("deepslate_tin_ore", () -> new ExtendedOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(4.5f, 3).sound(SoundType.DEEPSLATE).mapColor(MapColor.DEEPSLATE)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", () -> new ExtendedOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(4.5f, 3).sound(SoundType.DEEPSLATE).mapColor(MapColor.DEEPSLATE)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DEEPSLATE_LEAD_ORE = register("deepslate_lead_ore", () -> new ExtendedOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(4.5f, 3).sound(SoundType.DEEPSLATE).mapColor(MapColor.DEEPSLATE)), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> RAW_TIN_BLOCK = register("raw_tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(4, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> RAW_SILVER_BLOCK = register("raw_silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(5, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> RAW_LEAD_BLOCK = register("raw_lead_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(6, 8).sound(SoundType.METAL)), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> RAW_LUNUM_BLOCK = register("raw_lunum_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).requiresCorrectToolForDrops().strength(6, 10).sound(SoundType.METAL)), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> METEOR_METITE_ORE = register("meteor_metite_ore", () -> new ExtendedOreBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(15, 100).sound(SoundType.STONE)), AMItems.getSettings().fireResistant());

	public static final DeferredHolder<Block, Block> MOON_LUNUM_ORE = register("moon_lunum_ore", () -> new MoonStoneOreBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(6, 10).sound(SoundType.STONE)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DARK_MOON_LUNUM_ORE = register("dark_moon_lunum_ore", () -> new DarkMoonStoneOreBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops().strength(7, 11).sound(SoundType.STONE)), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> METITE_BLOCK = register("metite_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(8, 100).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ASTERITE_BLOCK = register("asterite_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(25, 1000).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> STELLUM_BLOCK = register("stellum_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(10, 80).sound(SoundType.METAL)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> GALAXIUM_BLOCK = register("galaxium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(50, 1300).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> UNIVITE_BLOCK = register("univite_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(80, 2000).sound(SoundType.METAL)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> LUNUM_BLOCK = register("lunum_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(7, 25).sound(SoundType.METAL)), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> TIN_BLOCK = register("tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(4, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SILVER_BLOCK = register("silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(5, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> LEAD_BLOCK = register("lead_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(6, 8).sound(SoundType.METAL)), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> BRONZE_BLOCK = register("bronze_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).requiresCorrectToolForDrops().strength(6, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> STEEL_BLOCK = register("steel_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(8, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELECTRUM_BLOCK = register("electrum_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(6, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> FOOLS_GOLD_BLOCK = register("fools_gold_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(5, 6).sound(SoundType.METAL)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> METEORIC_STEEL_BLOCK = register("meteoric_steel_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(5, 6).sound(SoundType.METAL)), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> METEOR_STONE = register("meteor_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(30, 1500)), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> METEOR_STONE_SLAB = register("meteor_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> METEOR_STONE_STAIRS = register("meteor_stone_stairs", () -> new StairBlock(METEOR_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> METEOR_STONE_WALL = register("meteor_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> SMOOTH_METEOR_STONE = register("smooth_meteor_stone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> SMOOTH_METEOR_STONE_SLAB = register("smooth_meteor_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> SMOOTH_METEOR_STONE_STAIRS = register("smooth_meteor_stone_stairs", () -> new StairBlock(SMOOTH_METEOR_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SMOOTH_METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> SMOOTH_METEOR_STONE_WALL = register("smooth_meteor_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> POLISHED_METEOR_STONE = register("polished_meteor_stone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> POLISHED_METEOR_STONE_SLAB = register("polished_meteor_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> POLISHED_METEOR_STONE_STAIRS = register("polished_meteor_stone_stairs", () -> new StairBlock(POLISHED_METEOR_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	
	public static final DeferredHolder<Block, Block> METEOR_STONE_BRICKS = register("meteor_stone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(METEOR_STONE.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> METEOR_STONE_BRICK_SLAB = register("meteor_stone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(METEOR_STONE_BRICKS.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> METEOR_STONE_BRICK_STAIRS = register("meteor_stone_brick_stairs", () -> new StairBlock(METEOR_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(METEOR_STONE_BRICKS.get())), AMItems.getSettings().fireResistant());
	public static final DeferredHolder<Block, Block> METEOR_STONE_BRICK_WALL = register("meteor_stone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(METEOR_STONE_BRICKS.get())), AMItems.getSettings().fireResistant());

	public static final DeferredHolder<Block, Block> MOON_STONE = register("moon_stone", () -> new DynamicBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2, 7)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> MOON_STONE_SLAB = register("moon_stone_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> MOON_STONE_STAIRS = register("moon_stone_stairs", () -> new DynamicStairsBlock(MOON_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> MOON_STONE_WALL = register("moon_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(MOON_STONE.get())), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> SMOOTH_MOON_STONE = register("smooth_moon_stone", () -> new DynamicBlock(BlockBehaviour.Properties.ofFullCopy(MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SMOOTH_MOON_STONE_SLAB = register("smooth_moon_stone_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SMOOTH_MOON_STONE_STAIRS = register("smooth_moon_stone_stairs", () -> new DynamicStairsBlock(SMOOTH_MOON_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SMOOTH_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SMOOTH_MOON_STONE_WALL = register("smooth_moon_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_MOON_STONE.get())), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> POLISHED_MOON_STONE = register("polished_moon_stone", () -> new DynamicBlock(BlockBehaviour.Properties.ofFullCopy(MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> POLISHED_MOON_STONE_SLAB = register("polished_moon_stone_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> POLISHED_MOON_STONE_STAIRS = register("polished_moon_stone_stairs", () -> new DynamicStairsBlock(POLISHED_MOON_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_MOON_STONE.get())), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> MOON_STONE_BRICKS = register("moon_stone_bricks", () -> new DynamicBlock(BlockBehaviour.Properties.ofFullCopy(MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> MOON_STONE_BRICK_SLAB = register("moon_stone_brick_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> MOON_STONE_BRICK_STAIRS = register("moon_stone_brick_stairs", () -> new DynamicStairsBlock(MOON_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> MOON_STONE_BRICK_WALL = register("moon_stone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(MOON_STONE_BRICKS.get())), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> DARK_MOON_STONE = register("dark_moon_stone", () -> new DynamicBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops().strength(3, 8.5F)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DARK_MOON_STONE_SLAB = register("dark_moon_stone_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DARK_MOON_STONE_STAIRS = register("dark_moon_stone_stairs", () -> new DynamicStairsBlock(DARK_MOON_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DARK_MOON_STONE_WALL = register("dark_moon_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE.get())), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> SMOOTH_DARK_MOON_STONE = register("smooth_dark_moon_stone", () -> new DynamicBlock(BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SMOOTH_DARK_MOON_STONE_SLAB = register("smooth_dark_moon_stone_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SMOOTH_DARK_MOON_STONE_STAIRS = register("smooth_dark_moon_stone_stairs", () -> new DynamicStairsBlock(SMOOTH_DARK_MOON_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SMOOTH_DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> SMOOTH_DARK_MOON_STONE_WALL = register("smooth_dark_moon_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_DARK_MOON_STONE.get())), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> POLISHED_DARK_MOON_STONE = register("polished_dark_moon_stone", () -> new DynamicBlock(BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> POLISHED_DARK_MOON_STONE_SLAB = register("polished_dark_moon_stone_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> POLISHED_DARK_MOON_STONE_STAIRS = register("polished_dark_moon_stone_stairs", () -> new DynamicStairsBlock(POLISHED_DARK_MOON_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_DARK_MOON_STONE.get())), AMItems.getSettings());

	public static final DeferredHolder<Block, Block> DARK_MOON_STONE_BRICKS = register("dark_moon_stone_bricks", () -> new DynamicBlock(BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DARK_MOON_STONE_BRICK_SLAB = register("dark_moon_stone_brick_slab", () -> new DynamicSlabBlock(BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DARK_MOON_STONE_BRICK_STAIRS = register("dark_moon_stone_brick_stairs", () -> new DynamicStairsBlock(DARK_MOON_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE_BRICKS.get())), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> DARK_MOON_STONE_BRICK_WALL = register("dark_moon_stone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(DARK_MOON_STONE_BRICKS.get())), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", () -> new HoloBridgeProjectorBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", () -> new HoloBridgeInvisibleBlock(BlockBehaviour.Properties.of().noLootTable().strength(-1.0F, 3600000.8F).noOcclusion().dynamicShape().lightLevel($ -> 15).isValidSpawn((a, b, c, d) -> false)));
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_TANK = register("primitive_tank", () -> new TankBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_TANK = register("basic_tank", () -> new TankBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_TANK = register("advanced_tank", () -> new TankBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_TANK = register("elite_tank", () -> new TankBlock.Elite(getEliteSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> CREATIVE_TANK = register("creative_tank", () -> new TankBlock.Creative(getCreativeSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", () -> new SolidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_SOLID_GENERATOR = register("basic_solid_generator", () -> new SolidGeneratorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", () -> new SolidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_SOLID_GENERATOR = register("elite_solid_generator", () -> new SolidGeneratorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_FLUID_GENERATOR = register("primitive_fluid_generator", () -> new FluidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_FLUID_GENERATOR = register("basic_fluid_generator", () -> new FluidGeneratorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_FLUID_GENERATOR = register("advanced_fluid_generator", () -> new FluidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_FLUID_GENERATOR = register("elite_fluid_generator", () -> new FluidGeneratorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_ELECTRIC_FURNACE = register("primitive_electric_furnace", () -> new ElectricFurnaceBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_ELECTRIC_FURNACE = register("basic_electric_furnace", () -> new ElectricFurnaceBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_ELECTRIC_FURNACE = register("advanced_electric_furnace", () -> new ElectricFurnaceBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_ELECTRIC_FURNACE = register("elite_electric_furnace", () -> new ElectricFurnaceBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", () -> new AlloySmelterBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", () -> new AlloySmelterBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", () -> new AlloySmelterBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", () -> new AlloySmelterBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_TRITURATOR = register("primitive_triturator", () -> new TrituratorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_TRITURATOR = register("basic_triturator", () -> new TrituratorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_TRITURATOR = register("advanced_triturator", () -> new TrituratorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_TRITURATOR = register("elite_triturator", () -> new TrituratorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_PRESSER = register("primitive_press", () -> new PresserBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_PRESSER = register("basic_press", () -> new PresserBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_PRESSER = register("advanced_press", () -> new PresserBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_PRESSER = register("elite_press", () -> new PresserBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_WIRE_MILL = register("primitive_wire_mill", () -> new WireMillBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_WIRE_MILL = register("basic_wire_mill", () -> new WireMillBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_WIRE_MILL = register("advanced_wire_mill", () -> new WireMillBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_WIRE_MILL = register("elite_wire_mill", () -> new WireMillBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", () -> new ElectrolyzerBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_ELECTROLYZER = register("basic_electrolyzer", () -> new ElectrolyzerBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", () -> new ElectrolyzerBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_ELECTROLYZER = register("elite_electrolyzer", () -> new ElectrolyzerBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_REFINERY = register("primitive_refinery", () -> new RefineryBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_REFINERY = register("basic_refinery", () -> new RefineryBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_REFINERY = register("advanced_refinery", () -> new RefineryBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_REFINERY = register("elite_refinery", () -> new RefineryBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", () -> new FluidMixerBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_FLUID_MIXER = register("basic_fluid_mixer", () -> new FluidMixerBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", () -> new FluidMixerBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_FLUID_MIXER = register("elite_fluid_mixer", () -> new FluidMixerBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_SOLIDIFIER = register("primitive_solidifier", () -> new SolidifierBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_SOLIDIFIER = register("basic_solidifier", () -> new SolidifierBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_SOLIDIFIER = register("advanced_solidifier", () -> new SolidifierBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_SOLIDIFIER = register("elite_solidifier", () -> new SolidifierBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_MELTER = register("primitive_melter", () -> new MelterBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_MELTER = register("basic_melter", () -> new MelterBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_MELTER = register("advanced_melter", () -> new MelterBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_MELTER = register("elite_melter", () -> new MelterBlock.Elite(getEliteSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_BUFFER = register("primitive_buffer", () -> new BufferBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_BUFFER = register("basic_buffer", () -> new BufferBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_BUFFER = register("advanced_buffer", () -> new BufferBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_BUFFER = register("elite_buffer", () -> new BufferBlock.Elite(getEliteSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> CREATIVE_BUFFER = register("creative_buffer", () -> new BufferBlock.Creative(getCreativeSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> FLUID_EXTRACTOR = register("fluid_collector", () -> new FluidCollectorBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> FLUID_INSERTER = register("fluid_placer", () -> new FluidPlacerBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BLOCK_BREAKER = register("block_breaker", () -> new BlockBreakerBlock(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BLOCK_PLACER = register("block_placer", () -> new BlockPlacerBlock(getAdvancedSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PUMP = register("pump", () -> new PumpBlock(getAdvancedSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> NUCLEAR_WARHEAD = register("nuclear_warhead", () -> new NuclearWarheadBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1F, 4F).sound(SoundType.METAL)), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_CAPACITOR = register("primitive_capacitor", () -> new CapacitorBlock.Primitive(getPrimitiveSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_CAPACITOR = register("basic_capacitor", () -> new CapacitorBlock.Basic(getBasicSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_CAPACITOR = register("advanced_capacitor", () -> new CapacitorBlock.Advanced(getAdvancedSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_CAPACITOR = register("elite_capacitor", () -> new CapacitorBlock.Elite(getEliteSettings()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> CREATIVE_CAPACITOR = register("creative_capacitor", () -> new CapacitorBlock.Creative(getCreativeSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> AIRLOCK = register("airlock", () -> new AirlockBlock(getBasicSettings()), block -> new DoubleHighBlockItem(block, AMItems.getSettings()));
	
	public static final DeferredHolder<Block, Block> DRAIN = register("drain", () -> new DrainBlock(getBasicSettings()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> FLUID_PIPE = register("fluid_pipe", () -> new FluidPipeBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1F, 1.5F).sound(SoundType.METAL).dynamicShape()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> ITEM_CONDUIT = register("item_conduit", () -> new ItemConduitBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1F, 1.5F).sound(SoundType.METAL).dynamicShape()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> PRIMITIVE_ENERGY_CABLE = register("primitive_energy_cable", () -> new EnergyCableBlock.Primitive(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1F, 1.5F).sound(SoundType.METAL).dynamicShape()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> BASIC_ENERGY_CABLE = register("basic_energy_cable", () -> new EnergyCableBlock.Basic(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1F, 1.5F).sound(SoundType.METAL).dynamicShape()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ADVANCED_ENERGY_CABLE = register("advanced_energy_cable", () -> new EnergyCableBlock.Advanced(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1F, 1.5F).sound(SoundType.METAL).dynamicShape()), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ELITE_ENERGY_CABLE = register("elite_energy_cable", () -> new EnergyCableBlock.Elite(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1F, 1.5F).sound(SoundType.METAL).dynamicShape()), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> ROCKET_CONTROLLER = register("rocket_controller", () -> new RocketControllerBlock(getAdvancedSettings().lightLevel(s -> 15)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> STATION_CONTROLLER = register("station_controller", () -> new StationControllerBlock(getAdvancedSettings().lightLevel(s -> 15)), AMItems.getSettings());
	
	public static final DeferredHolder<Block, Block> ROCKET_WALL = register("rocket_wall", () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.0F, 4.0F).sound(SoundType.METAL).lightLevel(s -> 15)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ROCKET_WINDOW = register("rocket_window", () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.0F, 4.0F).sound(SoundType.METAL).lightLevel(s -> 15)), AMItems.getSettings());
	public static final DeferredHolder<Block, Block> ROCKET_DOOR = register("rocket_door", () -> new RocketDoorBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.0F, 4.0F).sound(SoundType.METAL).lightLevel(s -> 15)), AMItems.getSettings());
	
	public static void init() {
		BLOCKS.register(AMCommon.modEventBus());
		ITEMS.register(AMCommon.modEventBus());
	}
	
	/**
	 * @param name     Name of block instance to be registered
	 * @param block    Block instance to be registered
	 * @param settings Item.Settings of BlockItem of Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> DeferredHolder<Block, T> register(String name, Supplier<T> block, Item.Properties settings) {
		return register(name, block, b -> new BlockItem(b, settings));
	}
	
	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @param item  BlockItem instance of Block to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> DeferredHolder<Block, T> register(String name, Supplier<T> block, Function<T, BlockItem> item) {
		var b = register(AMCommon.id(name), block);
		if (item != null) {
			ITEMS.register(b.getId().getPath(), () -> {
				var t = b.get();
				var blockItem = item.apply(t);
				Item.BY_BLOCK.put(t, blockItem);
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
	public static <T extends Block> DeferredHolder<Block, T> register(String name, Supplier<T> block) {
		return register(AMCommon.id(name), block);
	}
	
	/**
	 * @param name  Identifier of block instance to be registered
	 * @param block Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> DeferredHolder<Block, T> register(ResourceLocation name, Supplier<T> block) {
		return BLOCKS.register(name.getPath(), block);
	}
	
	public static BlockBehaviour.Properties getPrimitiveSettings() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(4, 6).sound(SoundType.METAL);
	}
	
	public static BlockBehaviour.Properties getBasicSettings() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).requiresCorrectToolForDrops().strength(6, 6).sound(SoundType.METAL);
	}
	
	public static BlockBehaviour.Properties getAdvancedSettings() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(8, 6).sound(SoundType.METAL);
	}
	
	public static BlockBehaviour.Properties getEliteSettings() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(8, 100).sound(SoundType.METAL);
	}
	
	public static BlockBehaviour.Properties getCreativeSettings() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).noLootTable().strength(-1.0F, 3600000.8F).sound(SoundType.METAL);
	}
}
