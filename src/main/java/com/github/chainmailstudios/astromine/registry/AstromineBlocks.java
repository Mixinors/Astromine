package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.AstromineOreBlock;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

public class AstromineBlocks {
	public static Block ASTERITE_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(40, 1000).sounds(BlockSoundGroup.STONE));
	public static Block METITE_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE));
	public static Block STELLUM_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 80).sounds(BlockSoundGroup.STONE));
	public static Block GALAXIUM_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(80, 1300).sounds(BlockSoundGroup.STONE));

	public static Block ASTERITE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 1000).sounds(BlockSoundGroup.METAL));
	public static Block METITE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(8, 100).sounds(BlockSoundGroup.METAL));
	public static Block STELLUM_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(10, 80).sounds(BlockSoundGroup.METAL));
	public static Block GALAXIUM_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(50, 1300).sounds(BlockSoundGroup.METAL));
	public static Block UNIVITE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PURPLE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 6).strength(80, 2000).sounds(BlockSoundGroup.METAL));

	public static Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE)), AstromineItems.BASIC_SETTINGS);
	public static Block ASTEROID_sTONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE)), AstromineItems.BASIC_SETTINGS);

	public static void initialize() {
		ASTERITE_ORE = register("asterite_ore", ASTERITE_ORE, AstromineItems.BASIC_SETTINGS);
		METITE_ORE = register("metite_ore", METITE_ORE, AstromineItems.BASIC_SETTINGS);
		STELLUM_ORE = register("stellum_ore", STELLUM_ORE, AstromineItems.BASIC_SETTINGS);
		GALAXIUM_ORE = register("galaxium_ore", GALAXIUM_ORE, AstromineItems.BASIC_SETTINGS);

		ASTERITE_BLOCK = register("asterite_block", ASTERITE_BLOCK, AstromineItems.BASIC_SETTINGS);
		METITE_BLOCK = register("metite_block", METITE_BLOCK, AstromineItems.BASIC_SETTINGS);
		STELLUM_BLOCK = register("stellum_block", STELLUM_BLOCK, AstromineItems.BASIC_SETTINGS);
		GALAXIUM_BLOCK = register("galaxium_block", GALAXIUM_BLOCK, AstromineItems.BASIC_SETTINGS);
		UNIVITE_BLOCK = register("univite_block", UNIVITE_BLOCK, AstromineItems.BASIC_SETTINGS);
	}

	/**
	 * @param name Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @param settings Item.Settings of BlockItem of Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new BlockItem(block, settings));
	}

	/**
	 * @param name Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @param item BlockItem instance of Block to be registered
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
	 * @param name Identifier of block instance to be registered
	 * @param block Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(Identifier name, T block) {
		return Registry.register(Registry.BLOCK, name, block);
	}

	/**
	 * @param name Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(String name, T block) {
		return register(AstromineCommon.identifier(name), block);
	}
}
