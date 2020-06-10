package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineBlocks {
	public static void initialize() {
		// NO-OP
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
		T b = register(new Identifier(AstromineCommon.MOD_ID, name), block);
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
		return register(new Identifier(AstromineCommon.MOD_ID, name), block);
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
