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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.block.*;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.RegistrySupplier;
import me.shedaniel.architectury.registry.ToolType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.AMCommon;

import java.util.function.Function;
import java.util.function.Supplier;

public class AMBlocks {
	public static final RegistrySupplier<Block> ASTEROID_STONE = register("asteroid_stone", () -> new Block(BlockProperties.of(Material.STONE, MapColor.GRAY).tool(ToolType.PICKAXE, 3).requiresTool().strength(1.5F, 3F)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", () -> new MagmaBlock(BlockProperties.of(Material.STONE, MapColor.GRAY).tool(ToolType.PICKAXE, 3).requiresTool().strength(50, 1500).luminance((state) -> 3).ticksRandomly().allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE = register("smooth_asteroid_stone", () -> new Block(BlockProperties.copy(ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> POLISHED_ASTEROID_STONE = register("polished_asteroid_stone", () -> new Block(BlockProperties.copy(ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICKS = register("asteroid_stone_bricks", () -> new Block(BlockProperties.copy(ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_SLAB = register("asteroid_stone_slab", () -> new SlabBlock(BlockProperties.copy(ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE_SLAB = register("smooth_asteroid_stone_slab", () -> new SlabBlock(BlockProperties.copy(SMOOTH_ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> POLISHED_ASTEROID_STONE_SLAB = register("polished_asteroid_stone_slab", () -> new SlabBlock(BlockProperties.copy(POLISHED_ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICK_SLAB = register("asteroid_stone_brick_slab", () -> new SlabBlock(BlockProperties.copy(ASTEROID_STONE_BRICKS.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", () -> new StairsBlock(ASTEROID_STONE.get().getDefaultState(), BlockProperties.copy(ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE_STAIRS = register("smooth_asteroid_stone_stairs", () -> new StairsBlock(SMOOTH_ASTEROID_STONE.get().getDefaultState(), BlockProperties.copy(SMOOTH_ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> POLISHED_ASTEROID_STONE_STAIRS = register("polished_asteroid_stone_stairs", () -> new StairsBlock(POLISHED_ASTEROID_STONE.get().getDefaultState(), BlockProperties.copy(POLISHED_ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICK_STAIRS = register("asteroid_stone_brick_stairs", () -> new StairsBlock(ASTEROID_STONE_BRICKS.get().getDefaultState(), BlockProperties.copy(ASTEROID_STONE_BRICKS.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_WALL = register("asteroid_stone_wall", () -> new WallBlock(BlockProperties.copy(ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> SMOOTH_ASTEROID_STONE_WALL = register("smooth_asteroid_stone_wall", () -> new WallBlock(BlockProperties.copy(SMOOTH_ASTEROID_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_STONE_BRICK_WALL = register("asteroid_stone_brick_wall", () -> new WallBlock(BlockProperties.copy(ASTEROID_STONE_BRICKS.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_METITE_ORE = register("asteroid_metite_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_ASTERITE_ORE = register("asteroid_asterite_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(40, 1000).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_STELLUM_ORE = register("asteroid_stellum_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(25, 80).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_GALAXIUM_ORE = register("asteroid_galaxium_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 5).requiresTool().strength(80, 1300).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_COPPER_ORE = register("asteroid_copper_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_TIN_ORE = register("asteroid_tin_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_SILVER_ORE = register("asteroid_silver_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_LEAD_ORE = register("asteroid_lead_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> ASTEROID_COAL_ORE = register("asteroid_coal_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_IRON_ORE = register("asteroid_iron_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_GOLD_ORE = register("asteroid_gold_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	
	public static final RegistrySupplier<Block> ALTAR_PEDESTAL = register("altar_pedestal", () -> new AltarPedestalBlock(BlockProperties.of(Material.METAL, MapColor.GOLD).tool(ToolType.PICKAXE, 2).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ALTAR = register("altar", () -> new AltarBlock(BlockProperties.of(Material.METAL, MapColor.GOLD).tool(ToolType.PICKAXE, 2).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> SPACE_SLIME_BLOCK = register("space_slime_block", () -> new SlimeBlock(BlockProperties.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.PURPLE)), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> COPPER_ORE = register("copper_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 1).requiresTool().strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> TIN_ORE = register("tin_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 1).requiresTool().strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> SILVER_ORE = register("silver_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 2).requiresTool().strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> LEAD_ORE = register("lead_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 2).requiresTool().strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> METEOR_METITE_ORE = register("meteor_metite_ore", () -> new AstromineOreBlock(BlockProperties.of(Material.STONE).tool(ToolType.PICKAXE, 4).requiresTool().strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> METITE_BLOCK = register("metite_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.PINK).tool(ToolType.PICKAXE, 4).requiresTool().strength(8, 100).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ASTERITE_BLOCK = register("asterite_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.RED).tool(ToolType.PICKAXE, 4).requiresTool().strength(25, 1000).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> STELLUM_BLOCK = register("stellum_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.ORANGE).tool(ToolType.PICKAXE, 4).requiresTool().strength(10, 80).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> GALAXIUM_BLOCK = register("galaxium_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.PURPLE).tool(ToolType.PICKAXE, 5).requiresTool().strength(50, 1300).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> UNIVITE_BLOCK = register("univite_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.WHITE).tool(ToolType.PICKAXE, 6).requiresTool().strength(80, 2000).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings().fireproof());
	
	
	public static final RegistrySupplier<Block> COPPER_BLOCK = register("copper_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.ORANGE).tool(ToolType.PICKAXE, 1).requiresTool().strength(4, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> TIN_BLOCK = register("tin_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.LIGHT_GRAY).tool(ToolType.PICKAXE, 1).requiresTool().strength(4, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> SILVER_BLOCK = register("silver_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.LIGHT_GRAY).tool(ToolType.PICKAXE, 2).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> LEAD_BLOCK = register("lead_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.TERRACOTTA_BLUE).tool(ToolType.PICKAXE, 2).requiresTool().strength(6, 8).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> BRONZE_BLOCK = register("bronze_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.TERRACOTTA_ORANGE).tool(ToolType.PICKAXE, 2).requiresTool().strength(6, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> STEEL_BLOCK = register("steel_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.GRAY).tool(ToolType.PICKAXE, 2).requiresTool().strength(8, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELECTRUM_BLOCK = register("electrum_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.YELLOW).tool(ToolType.PICKAXE, 2).requiresTool().strength(6, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> FOOLS_GOLD_BLOCK = register("fools_gold_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.GOLD).tool(ToolType.PICKAXE, 2).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> METEORIC_STEEL_BLOCK = register("meteoric_steel_block", () -> new Block(BlockProperties.of(Material.METAL, MapColor.GOLD).tool(ToolType.PICKAXE, 2).requiresTool().strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> METEOR_STONE = register("meteor_stone", () -> new Block(BlockProperties.of(Material.STONE, MapColor.BLACK).tool(ToolType.PICKAXE, 3).requiresTool().strength(30, 1500)), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_SLAB = register("meteor_stone_slab", () -> new SlabBlock(BlockProperties.copy(METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_STAIRS = register("meteor_stone_stairs", () -> new StairsBlock(METEOR_STONE.get().getDefaultState(), BlockProperties.copy(METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_WALL = register("meteor_stone_wall", () -> new WallBlock(BlockProperties.copy(METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE = register("smooth_meteor_stone", () -> new Block(BlockProperties.copy(METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE_SLAB = register("smooth_meteor_stone_slab", () -> new SlabBlock(BlockProperties.copy(SMOOTH_METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE_STAIRS = register("smooth_meteor_stone_stairs", () -> new StairsBlock(SMOOTH_METEOR_STONE.get().getDefaultState(), BlockProperties.copy(SMOOTH_METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> SMOOTH_METEOR_STONE_WALL = register("smooth_meteor_stone_wall", () -> new WallBlock(BlockProperties.copy(SMOOTH_METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> POLISHED_METEOR_STONE = register("polished_meteor_stone", () -> new Block(BlockProperties.copy(METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> POLISHED_METEOR_STONE_SLAB = register("polished_meteor_stone_slab", () -> new SlabBlock(BlockProperties.copy(POLISHED_METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> POLISHED_METEOR_STONE_STAIRS = register("polished_meteor_stone_stairs", () -> new StairsBlock(POLISHED_METEOR_STONE.get().getDefaultState(), BlockProperties.copy(POLISHED_METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> METEOR_STONE_BRICKS = register("meteor_stone_bricks", () -> new Block(BlockProperties.copy(METEOR_STONE.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_BRICK_SLAB = register("meteor_stone_brick_slab", () -> new SlabBlock(BlockProperties.copy(METEOR_STONE_BRICKS.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_BRICK_STAIRS = register("meteor_stone_brick_stairs", () -> new StairsBlock(METEOR_STONE_BRICKS.get().getDefaultState(), BlockProperties.copy(METEOR_STONE_BRICKS.get())), AMItems.getBasicSettings().fireproof());
	public static final RegistrySupplier<Block> METEOR_STONE_BRICK_WALL = register("meteor_stone_brick_wall", () -> new WallBlock(BlockProperties.copy(METEOR_STONE_BRICKS.get())), AMItems.getBasicSettings().fireproof());
	
	public static final RegistrySupplier<Block> HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", () -> new HoloBridgeProjectorBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", () -> new HoloBridgeInvisibleBlock(BlockProperties.of(HoloBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().luminance($ -> 15).allowsSpawning((a, b, c, d) -> false)));
	
	public static final RegistrySupplier<Block> VENT = register("vent", () -> new VentBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_TANK = register("primitive_tank", () -> new TankBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_TANK = register("basic_tank", () -> new TankBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_TANK = register("advanced_tank", () -> new TankBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_TANK = register("elite_tank", () -> new TankBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> CREATIVE_TANK = register("creative_tank", () -> new TankBlock.Creative(getCreativeSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", () -> new SolidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_SOLID_GENERATOR = register("basic_solid_generator", () -> new SolidGeneratorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", () -> new SolidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_SOLID_GENERATOR = register("elite_solid_generator", () -> new SolidGeneratorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_LIQUID_GENERATOR = register("primitive_fluid_generator", () -> new FluidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_LIQUID_GENERATOR = register("basic_fluid_generator", () -> new FluidGeneratorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_LIQUID_GENERATOR = register("advanced_fluid_generator", () -> new FluidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_LIQUID_GENERATOR = register("elite_fluid_generator", () -> new FluidGeneratorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ELECTRIC_FURNACE = register("primitive_electric_furnace", () -> new ElectricFurnaceBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_ELECTRIC_FURNACE = register("basic_electric_furnace", () -> new ElectricFurnaceBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_ELECTRIC_FURNACE = register("advanced_electric_furnace", () -> new ElectricFurnaceBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_ELECTRIC_FURNACE = register("elite_electric_furnace", () -> new ElectricFurnaceBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", () -> new AlloySmelterBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", () -> new AlloySmelterBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", () -> new AlloySmelterBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", () -> new AlloySmelterBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_TRITURATOR = register("primitive_triturator", () -> new TrituratorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_TRITURATOR = register("basic_triturator", () -> new TrituratorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_TRITURATOR = register("advanced_triturator", () -> new TrituratorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_TRITURATOR = register("elite_triturator", () -> new TrituratorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_PRESSER = register("primitive_press", () -> new PressBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_PRESSER = register("basic_press", () -> new PressBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_PRESSER = register("advanced_press", () -> new PressBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_PRESSER = register("elite_press", () -> new PressBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_WIREMILL = register("primitive_wire_mill", () -> new WireMillBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_WIREMILL = register("basic_wire_mill", () -> new WireMillBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_WIREMILL = register("advanced_wire_mill", () -> new WireMillBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_WIREMILL = register("elite_wire_mill", () -> new WireMillBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", () -> new ElectrolyzerBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_ELECTROLYZER = register("basic_electrolyzer", () -> new ElectrolyzerBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", () -> new ElectrolyzerBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_ELECTROLYZER = register("elite_electrolyzer", () -> new ElectrolyzerBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_REFINERY = register("primitive_refinery", () -> new RefineryBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_REFINERY = register("basic_refinery", () -> new RefineryBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_REFINERY = register("advanced_refinery", () -> new RefineryBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_REFINERY = register("elite_refinery", () -> new RefineryBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", () -> new FluidMixerBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_FLUID_MIXER = register("basic_fluid_mixer", () -> new FluidMixerBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", () -> new FluidMixerBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_FLUID_MIXER = register("elite_fluid_mixer", () -> new FluidMixerBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_SOLIDIFIER = register("primitive_solidifier", () -> new SolidifierBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_SOLIDIFIER = register("basic_solidifier", () -> new SolidifierBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_SOLIDIFIER = register("advanced_solidifier", () -> new SolidifierBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_SOLIDIFIER = register("elite_solidifier", () -> new SolidifierBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_MELTER = register("primitive_melter", () -> new MelterBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_MELTER = register("basic_melter", () -> new MelterBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_MELTER = register("advanced_melter", () -> new MelterBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_MELTER = register("elite_melter", () -> new MelterBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_BUFFER = register("primitive_buffer", () -> new BufferBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_BUFFER = register("basic_buffer", () -> new BufferBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_BUFFER = register("advanced_buffer", () -> new BufferBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_BUFFER = register("elite_buffer", () -> new BufferBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> CREATIVE_BUFFER = register("creative_buffer", () -> new BufferBlock.Creative(getCreativeSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> FLUID_EXTRACTOR = register("fluid_collector", () -> new FluidCollectorBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> FLUID_INSERTER = register("fluid_placer", () -> new FluidPlacerBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BLOCK_BREAKER = register("block_breaker", () -> new BlockBreakerBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BLOCK_PLACER = register("block_placer", () -> new BlockPlacerBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> NUCLEAR_WARHEAD = register("nuclear_warhead", () -> new NuclearWarheadBlock(BlockProperties.of(Material.METAL).tool(ToolType.PICKAXE, 5).requiresTool().strength(1F, 4F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_CAPACITOR = register("primitive_capacitor", () -> new CapacitorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_CAPACITOR = register("basic_capacitor", () -> new CapacitorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_CAPACITOR = register("advanced_capacitor", () -> new CapacitorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_CAPACITOR = register("elite_capacitor", () -> new CapacitorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> CREATIVE_CAPACITOR = register("creative_capacitor", () -> new CapacitorBlock.Creative(getCreativeSettings()), AMItems.getBasicSettings());
	
	public static final Block AIRLOCK = new AirlockBlock(getBasicSettings());
	
	public static final RegistrySupplier<Block> DRAIN = register("drain", () -> new DrainBlock(getBasicSettings()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> CATWALK = register("catwalk", () -> new CatwalkBlock(BlockProperties.copy(Blocks.STONE).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> CATWALK_STAIRS = register("catwalk_stairs", () -> new CatwalkStairsBlock(BlockProperties.copy(Blocks.STONE).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> FLUID_CABLE = register("fluid_pipe", () -> new FluidPipeBlock(BlockProperties.of(Material.METAL).tool(ToolType.PICKAXE, 1).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final RegistrySupplier<Block> PRIMITIVE_ENERGY_CABLE = register("primitive_energy_cable", () -> new EnergyCableBlock(AMConfig.get().primitiveEnergyCableEnergy, BlockProperties.of(Material.METAL).tool(ToolType.PICKAXE, 1).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> BASIC_ENERGY_CABLE = register("basic_energy_cable", () -> new EnergyCableBlock(AMConfig.get().basicEnergyCableEnergy, BlockProperties.of(Material.METAL).tool(ToolType.PICKAXE, 1).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ADVANCED_ENERGY_CABLE = register("advanced_energy_cable", () -> new EnergyCableBlock(AMConfig.get().advancedEnergyCableEnergy, BlockProperties.of(Material.METAL).tool(ToolType.PICKAXE, 1).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final RegistrySupplier<Block> ELITE_ENERGY_CABLE = register("elite_energy_cable", () -> new EnergyCableBlock(AMConfig.get().eliteEnergyCableEnergy, BlockProperties.of(Material.METAL).tool(ToolType.PICKAXE, 1).requiresTool().strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static void init() {
		register("airlock", () -> AIRLOCK, block -> new TallBlockItem(block, AMItems.getBasicSettings()));
	}

	/**
	 * @param name
	 *        Name of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 * @param settings
	 *        Item.Settings of BlockItem of Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block, Item.Settings settings) {
		return register(name, block, b -> new BlockItem(b, settings));
	}

	/**
	 * @param name
	 *        Name of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 * @param item
	 *        BlockItem instance of Block to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block, Function<T, BlockItem> item) {
		RegistrySupplier<T> b = register(AMCommon.id(name), block);
		if (item != null) {
			AMCommon.registry(Registry.ITEM_KEY).register(b.getId(), () -> {
				T t = b.get();
				BlockItem blockItem = item.apply(t);
				Item.BLOCK_ITEMS.put(t, blockItem);
				return blockItem;
			});
		}
		return b;
	}

	/**
	 * @param name
	 *        Name of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block) {
		return register(AMCommon.id(name), block);
	}

	/**
	 * @param name
	 *        Identifier of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> RegistrySupplier<T> register(Identifier name, Supplier<T> block) {
		return AMCommon.registry(Registry.BLOCK_KEY).registerSupplied(name, block);
	}

	public static AbstractBlock.Settings getPrimitiveSettings() {
		return BlockProperties.of(Material.METAL, MapColor.ORANGE).tool(ToolType.PICKAXE, 1).requiresTool().strength(4, 6).sounds(BlockSoundGroup.METAL);
	}

	public static AbstractBlock.Settings getBasicSettings() {
		return BlockProperties.of(Material.METAL, MapColor.TERRACOTTA_ORANGE).tool(ToolType.PICKAXE, 2).requiresTool().strength(6, 6).sounds(BlockSoundGroup.METAL);
	}

	public static AbstractBlock.Settings getAdvancedSettings() {
		return BlockProperties.of(Material.METAL, MapColor.GRAY).tool(ToolType.PICKAXE, 2).requiresTool().strength(8, 6).sounds(BlockSoundGroup.METAL);
	}

	public static AbstractBlock.Settings getEliteSettings() {
		return BlockProperties.of(Material.METAL, MapColor.PINK).tool(ToolType.PICKAXE, 4).requiresTool().strength(8, 100).sounds(BlockSoundGroup.METAL);
	}

	public static AbstractBlock.Settings getCreativeSettings() {
		return BlockProperties.of(Material.METAL, MapColor.LIME).dropsNothing().strength(-1.0F, 3600000.8F).sounds(BlockSoundGroup.METAL);
	}
}
