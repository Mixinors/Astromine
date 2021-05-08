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
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.AMCommon;

public class AMBlocks {
	public static final Block ASTEROID_STONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(1.5F, 3F)), AMItems.getBasicSettings().fireproof());
	public static final Block BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", new MagmaBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500).lightLevel((state) -> 3).ticksRandomly().allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)), AMItems.getBasicSettings().fireproof());
	
	public static final Block SMOOTH_ASTEROID_STONE = register("smooth_asteroid_stone", new Block(FabricBlockSettings.copyOf(ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block POLISHED_ASTEROID_STONE = register("polished_asteroid_stone", new Block(FabricBlockSettings.copyOf(ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_STONE_BRICKS = register("asteroid_stone_bricks", new Block(FabricBlockSettings.copyOf(ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_STONE_SLAB = register("asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block SMOOTH_ASTEROID_STONE_SLAB = register("smooth_asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(SMOOTH_ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block POLISHED_ASTEROID_STONE_SLAB = register("polished_asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(POLISHED_ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_STONE_BRICK_SLAB = register("asteroid_stone_brick_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE_BRICKS)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", new StairsBlock(ASTEROID_STONE.getDefaultState(), FabricBlockSettings.copyOf(ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block SMOOTH_ASTEROID_STONE_STAIRS = register("smooth_asteroid_stone_stairs", new StairsBlock(SMOOTH_ASTEROID_STONE.getDefaultState(), FabricBlockSettings.copyOf(SMOOTH_ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block POLISHED_ASTEROID_STONE_STAIRS = register("polished_asteroid_stone_stairs", new StairsBlock(POLISHED_ASTEROID_STONE.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_STONE_BRICK_STAIRS = register("asteroid_stone_brick_stairs", new StairsBlock(ASTEROID_STONE_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(ASTEROID_STONE_BRICKS)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_STONE_WALL = register("asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block SMOOTH_ASTEROID_STONE_WALL = register("smooth_asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(SMOOTH_ASTEROID_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_STONE_BRICK_WALL = register("asteroid_stone_brick_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE_BRICKS)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_METITE_ORE = register("asteroid_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_ASTERITE_ORE = register("asteroid_asterite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(40, 1000).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STELLUM_ORE = register("asteroid_stellum_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 80).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GALAXIUM_ORE = register("asteroid_galaxium_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(80, 1300).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_COPPER_ORE = register("asteroid_copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_TIN_ORE = register("asteroid_tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_SILVER_ORE = register("asteroid_silver_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_LEAD_ORE = register("asteroid_lead_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_COAL_ORE = register("asteroid_coal_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_IRON_ORE = register("asteroid_iron_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GOLD_ORE = register("asteroid_gold_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	
	public static final Block ALTAR_PEDESTAL = register("altar_pedestal", new AltarPedestalBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	public static final Block ALTAR = register("altar", new AltarBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	
	public static final Block SPACE_SLIME_BLOCK = register("space_slime_block", new SlimeBlock(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK).materialColor(MaterialColor.PURPLE)), AMItems.getBasicSettings());
	
	public static final Block COPPER_ORE = register("copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	public static final Block TIN_ORE = register("tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	public static final Block SILVER_ORE = register("silver_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	public static final Block LEAD_ORE = register("lead_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3, 3).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings());
	
	public static final Block METEOR_METITE_ORE = register("meteor_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block METITE_BLOCK = register("metite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PINK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(8, 100).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block ASTERITE_BLOCK = register("asterite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 1000).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block STELLUM_BLOCK = register("stellum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(10, 80).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings().fireproof());
	public static final Block GALAXIUM_BLOCK = register("galaxium_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PURPLE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(50, 1300).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block UNIVITE_BLOCK = register("univite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.WHITE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 6).strength(80, 2000).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings().fireproof());
	
	
	public static final Block COPPER_BLOCK = register("copper_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block TIN_BLOCK = register("tin_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIGHT_GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block SILVER_BLOCK = register("silver_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIGHT_GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block LEAD_BLOCK = register("lead_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE_TERRACOTTA).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 8).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final Block BRONZE_BLOCK = register("bronze_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE_TERRACOTTA).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block STEEL_BLOCK = register("steel_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(8, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block ELECTRUM_BLOCK = register("electrum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block FOOLS_GOLD_BLOCK = register("fools_gold_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block METEORIC_STEEL_BLOCK = register("meteoric_steel_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(30, 1500)), AMItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_SLAB = register("meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_STAIRS = register("meteor_stone_stairs", new StairsBlock(METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_WALL = register("meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block SMOOTH_METEOR_STONE = register("smooth_meteor_stone", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block SMOOTH_METEOR_STONE_SLAB = register("smooth_meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block SMOOTH_METEOR_STONE_STAIRS = register("smooth_meteor_stone_stairs", new StairsBlock(SMOOTH_METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block SMOOTH_METEOR_STONE_WALL = register("smooth_meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(SMOOTH_METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block POLISHED_METEOR_STONE = register("polished_meteor_stone", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block POLISHED_METEOR_STONE_SLAB = register("polished_meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(POLISHED_METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block POLISHED_METEOR_STONE_STAIRS = register("polished_meteor_stone_stairs", new StairsBlock(POLISHED_METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(POLISHED_METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	
	public static final Block METEOR_STONE_BRICKS = register("meteor_stone_bricks", new Block(FabricBlockSettings.copyOf(METEOR_STONE)), AMItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_BRICK_SLAB = register("meteor_stone_brick_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AMItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_BRICK_STAIRS = register("meteor_stone_brick_stairs", new StairsBlock(METEOR_STONE_BRICKS.getDefaultState(), FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AMItems.getBasicSettings().fireproof());
	public static final Block METEOR_STONE_BRICK_WALL = register("meteor_stone_brick_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE_BRICKS)), AMItems.getBasicSettings().fireproof());
	
	public static final Block HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", new HoloBridgeProjectorBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", new HoloBridgeInvisibleBlock(FabricBlockSettings.of(HoloBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().lightLevel(15).allowsSpawning((a, b, c, d) -> false)));
	
	public static final Block VENT = register("vent", new VentBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_TANK = register("primitive_tank", new TankBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_TANK = register("basic_tank", new TankBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_TANK = register("advanced_tank", new TankBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_TANK = register("elite_tank", new TankBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	public static final Block CREATIVE_TANK = register("creative_tank", new TankBlock.Creative(getCreativeSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", new SolidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_SOLID_GENERATOR = register("basic_solid_generator", new SolidGeneratorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", new SolidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_SOLID_GENERATOR = register("elite_solid_generator", new SolidGeneratorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_LIQUID_GENERATOR = register("primitive_fluid_generator", new FluidGeneratorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_LIQUID_GENERATOR = register("basic_fluid_generator", new FluidGeneratorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_LIQUID_GENERATOR = register("advanced_fluid_generator", new FluidGeneratorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_LIQUID_GENERATOR = register("elite_fluid_generator", new FluidGeneratorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_ELECTRIC_FURNACE = register("primitive_electric_furnace", new ElectricFurnaceBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_ELECTRIC_FURNACE = register("basic_electric_furnace", new ElectricFurnaceBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_ELECTRIC_FURNACE = register("advanced_electric_furnace", new ElectricFurnaceBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_ELECTRIC_FURNACE = register("elite_electric_furnace", new ElectricFurnaceBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", new AlloySmelterBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", new AlloySmelterBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", new AlloySmelterBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", new AlloySmelterBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_TRITURATOR = register("primitive_triturator", new TrituratorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_TRITURATOR = register("basic_triturator", new TrituratorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_TRITURATOR = register("advanced_triturator", new TrituratorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_TRITURATOR = register("elite_triturator", new TrituratorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_PRESSER = register("primitive_press", new PressBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_PRESSER = register("basic_press", new PressBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_PRESSER = register("advanced_press", new PressBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_PRESSER = register("elite_press", new PressBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_WIREMILL = register("primitive_wire_mill", new WireMillBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_WIREMILL = register("basic_wire_mill", new WireMillBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_WIREMILL = register("advanced_wire_mill", new WireMillBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_WIREMILL = register("elite_wire_mill", new WireMillBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", new ElectrolyzerBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_ELECTROLYZER = register("basic_electrolyzer", new ElectrolyzerBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", new ElectrolyzerBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_ELECTROLYZER = register("elite_electrolyzer", new ElectrolyzerBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_REFINERY = register("primitive_refinery", new RefineryBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_REFINERY = register("basic_refinery", new RefineryBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_REFINERY = register("advanced_refinery", new RefineryBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_REFINERY = register("elite_refinery", new RefineryBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", new FluidMixerBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_FLUID_MIXER = register("basic_fluid_mixer", new FluidMixerBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", new FluidMixerBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_FLUID_MIXER = register("elite_fluid_mixer", new FluidMixerBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_SOLIDIFIER = register("primitive_solidifier", new SolidifierBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_SOLIDIFIER = register("basic_solidifier", new SolidifierBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_SOLIDIFIER = register("advanced_solidifier", new SolidifierBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_SOLIDIFIER = register("elite_solidifier", new SolidifierBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_MELTER = register("primitive_melter", new MelterBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_MELTER = register("basic_melter", new MelterBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_MELTER = register("advanced_melter", new MelterBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_MELTER = register("elite_melter", new MelterBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_BUFFER = register("primitive_buffer", new BufferBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_BUFFER = register("basic_buffer", new BufferBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_BUFFER = register("advanced_buffer", new BufferBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_BUFFER = register("elite_buffer", new BufferBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	public static final Block CREATIVE_BUFFER = register("creative_buffer", new BufferBlock.Creative(getCreativeSettings()), AMItems.getBasicSettings());
	
	public static final Block FLUID_EXTRACTOR = register("fluid_collector", new FluidCollectorBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block FLUID_INSERTER = register("fluid_placer", new FluidPlacerBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block BLOCK_BREAKER = register("block_breaker", new BlockBreakerBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block BLOCK_PLACER = register("block_placer", new BlockPlacerBlock(getAdvancedSettings()), AMItems.getBasicSettings());
	
	public static final Block NUCLEAR_WARHEAD = register("nuclear_warhead", new NuclearWarheadBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(1F, 4F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_CAPACITOR = register("primitive_capacitor", new CapacitorBlock.Primitive(getPrimitiveSettings()), AMItems.getBasicSettings());
	public static final Block BASIC_CAPACITOR = register("basic_capacitor", new CapacitorBlock.Basic(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block ADVANCED_CAPACITOR = register("advanced_capacitor", new CapacitorBlock.Advanced(getAdvancedSettings()), AMItems.getBasicSettings());
	public static final Block ELITE_CAPACITOR = register("elite_capacitor", new CapacitorBlock.Elite(getEliteSettings()), AMItems.getBasicSettings());
	public static final Block CREATIVE_CAPACITOR = register("creative_capacitor", new CapacitorBlock.Creative(getCreativeSettings()), AMItems.getBasicSettings());
	
	public static final Block AIRLOCK = new AirlockBlock(getBasicSettings());
	
	public static final Block ALTERNATOR = register("alternator", new AlternatorBlock(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block SPLITTER = register("splitter", new SplitterBlock(getBasicSettings()), AMItems.getBasicSettings());
	public static final Block SHREDDER = register("shredder", new ShredderBlock(getBasicSettings().ticksRandomly()), AMItems.getBasicSettings());
	
	public static final Block DRAIN = register("drain", new DrainBlock(getBasicSettings()), AMItems.getBasicSettings());
	
	public static final Block INSERTER = register("inserter", new InserterBlock("normal", AMConfig.get().inserterSpeed, getBasicSettings().nonOpaque()), AMItems.getBasicSettings());
	public static final Block FAST_INSERTER = register("fast_inserter", new InserterBlock("fast", AMConfig.get().fastInserterSpeed, getBasicSettings().nonOpaque()), AMItems.getBasicSettings());
	
	public static final Block BASIC_CONVEYOR = register("basic_conveyor", new ConveyorBlock(getBasicSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().basicConveyorSpeed), AMItems.getBasicSettings());
	public static final Block BASIC_VERTICAL_CONVEYOR = register("basic_vertical_conveyor", new VerticalConveyorBlock(getBasicSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().basicConveyorSpeed), AMItems.getBasicSettings());
	public static final Block BASIC_DOWNWARD_VERTICAL_CONVEYOR = register("basic_downward_vertical_conveyor", new DownwardVerticalConveyorBlock(getBasicSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().basicConveyorSpeed), AMItems.getBasicSettings());
	public static final Block ADVANCED_CONVEYOR = register("advanced_conveyor", new ConveyorBlock(getAdvancedSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().advancedConveyorSpeed), AMItems.getBasicSettings());
	public static final Block ADVANCED_VERTICAL_CONVEYOR = register("advanced_vertical_conveyor", new VerticalConveyorBlock(getAdvancedSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().advancedConveyorSpeed), AMItems.getBasicSettings());
	public static final Block ADVANCED_DOWNWARD_VERTICAL_CONVEYOR = register("advanced_downward_vertical_conveyor", new DownwardVerticalConveyorBlock(getAdvancedSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().advancedConveyorSpeed), AMItems.getBasicSettings());
	public static final Block ELITE_CONVEYOR = register("elite_conveyor", new ConveyorBlock(getEliteSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().eliteConveyorSpeed), AMItems.getBasicSettings());
	public static final Block ELITE_VERTICAL_CONVEYOR = register("elite_vertical_conveyor", new VerticalConveyorBlock(getEliteSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().eliteConveyorSpeed), AMItems.getBasicSettings());
	public static final Block ELITE_DOWNWARD_VERTICAL_CONVEYOR = register("elite_downward_vertical_conveyor", new DownwardVerticalConveyorBlock(getEliteSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), AMConfig.get().eliteConveyorSpeed), AMItems.getBasicSettings());
	
	public static final Block CATWALK = register("catwalk", new CatwalkBlock(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	public static final Block CATWALK_STAIRS = register("catwalk_stairs", new CatwalkStairsBlock(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.METAL).nonOpaque()), AMItems.getBasicSettings());
	
	public static final Block FLUID_CABLE = register("fluid_pipe", new FluidPipeBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static final Block PRIMITIVE_ENERGY_CABLE = register("primitive_energy_cable", new EnergyCableBlock(AMConfig.get().primitiveEnergyCableEnergy, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block BASIC_ENERGY_CABLE = register("basic_energy_cable", new EnergyCableBlock(AMConfig.get().basicEnergyCableEnergy, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block ADVANCED_ENERGY_CABLE = register("advanced_energy_cable", new EnergyCableBlock(AMConfig.get().advancedEnergyCableEnergy, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	public static final Block ELITE_ENERGY_CABLE = register("elite_energy_cable", new EnergyCableBlock(AMConfig.get().eliteEnergyCableEnergy, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AMItems.getBasicSettings());
	
	public static void init() {
		register("airlock", AIRLOCK, new TallBlockItem(AIRLOCK, AMItems.getBasicSettings()));
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
	public static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new BlockItem(block, settings));
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
	public static <T extends Block> T register(String name, T block, BlockItem item) {
		T b = register(AMCommon.id(name), block);
		if (item != null) {
			Item.BLOCK_ITEMS.put(block, item);
			AMItems.register(name, item);
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
	public static <T extends Block> T register(String name, T block) {
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
