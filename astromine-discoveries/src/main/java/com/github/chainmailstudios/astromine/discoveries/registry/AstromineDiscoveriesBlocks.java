package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;

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

	public static void initialize() {

	}
}
