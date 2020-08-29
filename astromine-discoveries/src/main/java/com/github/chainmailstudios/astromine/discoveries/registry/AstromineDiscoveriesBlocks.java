package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.discoveries.common.block.AltarBlock;
import com.github.chainmailstudios.astromine.discoveries.common.block.AltarPedestalBlock;
import com.github.chainmailstudios.astromine.foundations.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;

public class AstromineDiscoveriesBlocks extends AstromineBlocks {
	public static final Block ASTEROID_STONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE = register("moon_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 0).strength(1, 3)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE = register("vulcan_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 4)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_SOIL = register("martian_soil", new Block(FabricBlockSettings.of(Material.SOIL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.SHOVELS, 0).strength(0.5f, 0.75f)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE = register("martian_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 0).strength(1.5f, 6.0f)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", new MagmaBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500).lightLevel((state) -> 3).ticksRandomly().allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)), AstromineDiscoveriesItems.getBasicSettings().fireproof());

	public static final Block ASTEROID_STONE_SLAB = register("asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_SLAB = register("moon_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE_SLAB = register("vulcan_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_SLAB = register("martian_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", new StairsBlock(ASTEROID_STONE.getDefaultState(), FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_STAIRS = register("moon_stone_stairs", new StairsBlock(MOON_STONE.getDefaultState(), FabricBlockSettings.copyOf(MOON_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE_STAIRS = register("vulcan_stone_stairs", new StairsBlock(VULCAN_STONE.getDefaultState(), FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_STAIRS = register("martian_stone_stairs", new StairsBlock(MARTIAN_STONE.getDefaultState(), FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_STONE_WALL = register("asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block MOON_STONE_WALL = register("moon_stone_wall", new WallBlock(FabricBlockSettings.copyOf(MOON_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block VULCAN_STONE_WALL = register("vulcan_stone_wall", new WallBlock(FabricBlockSettings.copyOf(VULCAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());
	public static final Block MARTIAN_STONE_WALL = register("martian_stone_wall", new WallBlock(FabricBlockSettings.copyOf(MARTIAN_STONE)), AstromineDiscoveriesItems.getBasicSettings());

	public static final Block ASTEROID_METITE_ORE = register("asteroid_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_ASTERITE_ORE = register("asteroid_asterite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(40, 1000).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STELLUM_ORE = register("asteroid_stellum_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 80).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GALAXIUM_ORE = register("asteroid_galaxium_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(80, 1300).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());

	public static final Block ASTEROID_COPPER_ORE = register("asteroid_copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_TIN_ORE = register("asteroid_tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_SILVER_ORE = register("asteroid_silver_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_LEAD_ORE = register("asteroid_lead_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());

	public static final Block ASTEROID_COAL_ORE = register("asteroid_coal_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_IRON_ORE = register("asteroid_iron_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GOLD_ORE = register("asteroid_gold_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineDiscoveriesItems.getBasicSettings().fireproof());

	public static final Block ALTAR_PEDESTAL = register("altar_pedestal", new AltarPedestalBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL).nonOpaque()), AstromineFoundationsItems.getBasicSettings());
	public static final Block ALTAR = register("altar", new AltarBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5, 6).sounds(BlockSoundGroup.METAL).nonOpaque()), AstromineFoundationsItems.getBasicSettings());

	public static void initialize() {

	}
}
