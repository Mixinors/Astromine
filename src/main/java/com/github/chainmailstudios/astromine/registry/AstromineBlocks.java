package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeInvisibleBlock;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeProjectorBlock;
import com.github.chainmailstudios.astromine.common.block.NuclearWeaponBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineBlocks {
	public static final Block HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", new HolographicBridgeProjectorBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(4, 16).sounds(BlockSoundGroup.METAL)), AstromineItems.BASIC_SETTINGS);
	public static final Block HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", new HolographicBridgeInvisibleBlock(FabricBlockSettings.of(Material.AIR).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().lightLevel(15).allowsSpawning((a, b, c, d) -> false)), AstromineItems.BASIC_SETTINGS);

	public static final Block NUCLEAR_WEAPON = register("nuclear_weapon", new NuclearWeaponBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(80, 1300).sounds(BlockSoundGroup.STONE)), AstromineItems.BASIC_SETTINGS);

	public static final Block ASTERITE_ORE = register("asterite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(40, 1000).sounds(BlockSoundGroup.STONE)), AstromineItems.BASIC_SETTINGS);
	public static final Block METITE_ORE = register("metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.BASIC_SETTINGS);
	public static final Block STELLUM_ORE = register("stellum_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 80).sounds(BlockSoundGroup.STONE)), AstromineItems.BASIC_SETTINGS);
	public static final Block GALAXIUM_ORE = register("galaxium_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(80, 1300).sounds(BlockSoundGroup.STONE)), AstromineItems.BASIC_SETTINGS);

	public static final Block ASTERITE_BLOCK = register("asterite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 1000).sounds(BlockSoundGroup.METAL)), AstromineItems.BASIC_SETTINGS);
	public static final Block METITE_BLOCK = register("metite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(8, 100).sounds(BlockSoundGroup.METAL)), AstromineItems.BASIC_SETTINGS);
	public static final Block STELLUM_BLOCK = register("stellum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(10, 80).sounds(BlockSoundGroup.METAL)), AstromineItems.BASIC_SETTINGS);
	public static final Block GALAXIUM_BLOCK = register("galaxium_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIME).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(50, 1300).sounds(BlockSoundGroup.METAL)), AstromineItems.BASIC_SETTINGS);
	public static final Block UNIVITE_BLOCK = register("univite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PURPLE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 6).strength(80, 2000).sounds(BlockSoundGroup.METAL)), AstromineItems.BASIC_SETTINGS);

	public static final Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE)), AstromineItems.BASIC_SETTINGS);
	public static final Block ASTEROID_sTONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE)), AstromineItems.BASIC_SETTINGS);

	public static void initialize() {
		// Unused.
	}

	/**
	 * @param name     Name of block instance to be registered
	 * @param block    Block instance to be registered
	 * @param settings Item.Settings of BlockItem of Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new BlockItem(block, settings));
	}

	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @param item  BlockItem instance of Block to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(String name, T block, BlockItem item) {
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
	static <T extends Block> T register(String name, T block) {
		return register(AstromineCommon.identifier(name), block);
	}

	/**
	 * @param name  Identifier of block instance to be registered
	 * @param block Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(Identifier name, T block) {
		return Registry.register(Registry.BLOCK, name, block);
	}
}
