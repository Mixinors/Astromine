package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.AstromineOreBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineBlocks {
	public static Block ASTERITE_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(40, 1000));
	public static Block METITE_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100));
	public static Block STELLUM_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 80));
	public static Block UNIVITE_ORE = new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(100, 2000));

	public static void initialize() {
		ASTERITE_ORE = register("asterite_ore", ASTERITE_ORE, AstromineItems.BASIC_SETTINGS);
		METITE_ORE = register("metite_ore", METITE_ORE, AstromineItems.BASIC_SETTINGS);
		STELLUM_ORE = register("stellum_ore", STELLUM_ORE, AstromineItems.BASIC_SETTINGS);
		UNIVITE_ORE = register("univite_ore", UNIVITE_ORE, AstromineItems.BASIC_SETTINGS);
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
		T b = register(AstromineCommon.id(name), block);
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
		return register(AstromineCommon.id(name), block);
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
