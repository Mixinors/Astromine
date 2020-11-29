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

package com.github.chainmailstudios.astromine. registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class AstromineBlocks {
	public static void initialize() {

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
	public static <T extends Block> T register(String name, T block, Item.Properties settings) {
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
		T b = register(AstromineCommon.identifier(name), block);
		if (item != null) {
			Item.BY_BLOCK.put(block, item);
			AstromineItems.register(name, item);
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
		return register(AstromineCommon.identifier(name), block);
	}

	/**
	 * @param name
	 *        ResourceLocation of block instance to be registered
	 * @param block
	 *        Block instance to be registered
	 *
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(ResourceLocation name, T block) {
		return Registry.register(Registry.BLOCK, name, block);
	}

	public static BlockBehaviour.Properties getPrimitiveSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_ORANGE).breakByTool(FabricToolTags.PICKAXES, 1).requiresCorrectToolForDrops().strength(4, 6).sound(SoundType.METAL);
	}

	public static BlockBehaviour.Properties getBasicSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.TERRACOTTA_ORANGE).breakByTool(FabricToolTags.PICKAXES, 2).requiresCorrectToolForDrops().strength(6, 6).sound(SoundType.METAL);
	}

	public static BlockBehaviour.Properties getAdvancedSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_GRAY).breakByTool(FabricToolTags.PICKAXES, 2).requiresCorrectToolForDrops().strength(8, 6).sound(SoundType.METAL);
	}

	public static BlockBehaviour.Properties getEliteSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_PINK).breakByTool(FabricToolTags.PICKAXES, 4).requiresCorrectToolForDrops().strength(8, 100).sound(SoundType.METAL);
	}

	public static BlockBehaviour.Properties getCreativeSettings() {
		return FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_LIGHT_GREEN).noDrops().strength(-1.0F, 3600000.8F).sound(SoundType.METAL);
	}
}
