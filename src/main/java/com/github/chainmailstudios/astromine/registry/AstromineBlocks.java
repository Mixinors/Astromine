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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.AlloySmelterBlock;
import com.github.chainmailstudios.astromine.common.block.*;
import com.github.chainmailstudios.astromine.common.item.AstromineBlockItem;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineBlocks {
	public static final Block HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", new HolographicBridgeProjectorBlock(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", new HolographicBridgeInvisibleBlock(FabricBlockSettings.of(HolographicBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().lightLevel(15).allowsSpawning((a, b, c, d) -> false)));

	public static final Block VENT = register("vent", new VentBlock(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());

	public static final Block FLUID_TANK = register("tank", new TankBlock(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	
	public static final Block PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", new SolidGeneratorBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_SOLID_GENERATOR = register("basic_solid_generator", new SolidGeneratorBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", new SolidGeneratorBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_SOLID_GENERATOR = register("elite_solid_generator", new SolidGeneratorBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_LIQUID_GENERATOR = register("primitive_liquid_generator", new LiquidGeneratorBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_LIQUID_GENERATOR = register("basic_liquid_generator", new LiquidGeneratorBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_LIQUID_GENERATOR = register("advanced_liquid_generator", new LiquidGeneratorBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_LIQUID_GENERATOR = register("elite_liquid_generator", new LiquidGeneratorBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_ELECTRIC_SMELTER = register("primitive_electric_smelter", new ElectricSmelterBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_ELECTRIC_SMELTER = register("basic_electric_smelter", new ElectricSmelterBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_ELECTRIC_SMELTER = register("advanced_electric_smelter", new ElectricSmelterBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_ELECTRIC_SMELTER = register("elite_electric_smelter", new ElectricSmelterBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", new AlloySmelterBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", new AlloySmelterBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", new AlloySmelterBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", new AlloySmelterBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_TRITURATOR = register("primitive_triturator", new TrituratorBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_TRITURATOR = register("basic_triturator", new TrituratorBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_TRITURATOR = register("advanced_triturator", new TrituratorBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_TRITURATOR = register("elite_triturator", new TrituratorBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_PRESSER = register("primitive_presser", new PresserBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_PRESSER = register("basic_presser", new PresserBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_PRESSER = register("advanced_presser", new PresserBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_PRESSER = register("elite_presser", new PresserBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());
	
	public static final Block PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", new ElectrolyzerBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_ELECTROLYZER = register("basic_electrolyzer", new ElectrolyzerBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", new ElectrolyzerBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_ELECTROLYZER = register("elite_electrolyzer", new ElectrolyzerBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", new FluidMixerBlock.Primitive(AstromineBlocks.getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_FLUID_MIXER = register("basic_fluid_mixer", new FluidMixerBlock.Basic(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", new FluidMixerBlock.Advanced(AstromineBlocks.getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_FLUID_MIXER = register("elite_fluid_mixer", new FluidMixerBlock.Elite(AstromineBlocks.getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block FLUID_CABLE = register("fluid_cable", new FluidCableBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ENERGY_CABLE = register("energy_cable", new EnergyCableBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block NUCLEAR_WARHEAD = register("nuclear_warhead", new NuclearWarheadBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(1F, 4F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block CREATIVE_TANK = register("creative_tank", new CreativeTankBlock(AstromineBlocks.getCreativeSettings()), AstromineItems.getBasicSettings());
	public static final Block CREATIVE_CAPACITOR = register("creative_capacitor", new CreativeCapacitorBlock(AstromineBlocks.getCreativeSettings()), AstromineItems.getBasicSettings());
	public static final Block CREATIVE_BUFFER = register("creative_buffer", new CreativeBufferBlock(AstromineBlocks.getCreativeSettings()), AstromineItems.getBasicSettings());

	public static final Block BASIC_BUFFER = register("basic_buffer", new BufferBlock(BufferType.BASIC, AstromineBlocks.getBasicSettings()),AstromineItems.getBasicSettings());
	public static final Block ADVANCED_BUFFER = register("advanced_buffer", new BufferBlock(BufferType.ADVANCED, AstromineBlocks.getAdvancedSettings()),AstromineItems.getBasicSettings());
	public static final Block ELITE_BUFFER = register("elite_buffer", new BufferBlock(BufferType.ELITE, AstromineBlocks.getEliteSettings()),AstromineItems.getBasicSettings());

	public static final Block FLUID_EXTRACTOR = register("fluid_extractor", new FluidExtractorBlock(AstromineBlocks.getAdvancedSettings()),AstromineItems.getBasicSettings());
	public static final Block FLUID_INSERTER = register("fluid_inserter", new FluidInserterBlock(AstromineBlocks.getAdvancedSettings()),AstromineItems.getBasicSettings());
	public static final Block BLOCK_BREAKER = register("block_breaker", new BlockBreakerBlock(AstromineBlocks.getAdvancedSettings()),AstromineItems.getBasicSettings());
	public static final Block BLOCK_PLACER = register("block_placer", new BlockPlacerBlock(AstromineBlocks.getAdvancedSettings()),AstromineItems.getBasicSettings());

	public static final Block COPPER_ORE = register("copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block TIN_ORE = register("tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());

	public static final Block METEOR_METITE_ORE = register("meteor_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_METITE_ORE = register("asteroid_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_ASTERITE_ORE = register("asteroid_asterite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(40, 1000).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STELLUM_ORE = register("asteroid_stellum_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 80).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GALAXIUM_ORE = register("asteroid_galaxium_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(80, 1300).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());

	public static final Block ASTEROID_COPPER_ORE = register("asteroid_copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_TIN_ORE = register("asteroid_tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());

	public static final Block ASTEROID_COAL_ORE = register("asteroid_coal_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_IRON_ORE = register("asteroid_iron_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GOLD_ORE = register("asteroid_gold_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());

	public static final Block METITE_BLOCK = register("metite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PINK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(8, 100).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ASTERITE_BLOCK = register("asterite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 1000).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block STELLUM_BLOCK = register("stellum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(10, 80).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings().fireproof());
	public static final Block GALAXIUM_BLOCK = register("galaxium_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PURPLE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(50, 1300).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block UNIVITE_BLOCK = register("univite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.WHITE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 6).strength(80, 2000).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings().fireproof());

	public static final Block COPPER_BLOCK = register("copper_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block TIN_BLOCK = register("tin_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIGHT_GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block BRONZE_BLOCK = register("bronze_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE_TERRACOTTA).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block STEEL_BLOCK = register("steel_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(8, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(30, 1500)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE = register("moon_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 0).strength(1, 3)), AstromineItems.getBasicSettings());
	public static final Block MARS_SOIL = register("mars_soil", new Block(FabricBlockSettings.of(Material.SOIL, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 0).strength(0.5f, 0.75f)), AstromineItems.getBasicSettings());
	public static final Block BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", new MagmaBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500).lightLevel((state) -> 3).ticksRandomly().allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)), AstromineItems.getBasicSettings().fireproof());

	public static final Block METEOR_STONE_SLAB = register("meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_SLAB = register("asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_SLAB = register("moon_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineItems.getBasicSettings());

	public static final Block METEOR_STONE_STAIRS = register("meteor_stone_stairs", new StairsBlock(METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", new StairsBlock(ASTEROID_STONE.getDefaultState(), FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_STAIRS = register("moon_stone_stairs", new StairsBlock(MOON_STONE.getDefaultState(), FabricBlockSettings.copyOf(MOON_STONE)), AstromineItems.getBasicSettings());

	public static final Block METEOR_STONE_WALL = register("meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_WALL = register("asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_WALL = register("moon_stone_wall", new WallBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineItems.getBasicSettings());

	public static void initialize() {
		// Unused.
	}

	/**
	* @param name     Name of block instance to be registered
	* @param block    Block instance to be registered
	* @param settings Item.Settings of BlockItem of Block instance to be registered
	* @return Block instance registered
	*/
	public static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new AstromineBlockItem(block, settings));
	}

	/**
	* @param name  Name of block instance to be registered
	* @param block Block instance to be registered
	* @param item  BlockItem instance of Block to be registered
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
	* @param name  Name of block instance to be registered
	* @param block Block instance to be registered
	* @return Block instance registered
	*/
	public static <T extends Block> T register(String name, T block) {
		return register(AstromineCommon.identifier(name), block);
	}

	/**
	* @param name  Identifier of block instance to be registered
	* @param block Block instance to be registered
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
