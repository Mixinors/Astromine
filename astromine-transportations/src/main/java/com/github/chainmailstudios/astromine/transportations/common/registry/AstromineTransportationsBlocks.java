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

package com.github.chainmailstudios.astromine.transportations.common.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.AstromineBlockItem;
import com.github.chainmailstudios.astromine.common.item.AstromineEnergyBlockItem;
import com.github.chainmailstudios.astromine.common.utilities.EnergyCapacityProvider;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.transportations.common.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineTransportationsBlocks extends AstromineBlocks {
	public static final Block ALTERNATOR = register("alternator", new AlternatorBlock(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block SPLITTER = register("splitter", new SplitterBlock(AstromineBlocks.getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block INCINERATOR = register("incinerator", new IncineratorBlock(AstromineBlocks.getBasicSettings().ticksRandomly()), AstromineItems.getBasicSettings());

	public static final Block INSERTER = register("inserter", new InserterBlock("normal", 16, getBasicSettings().nonOpaque()), AstromineItems.getBasicSettings());
	public static final Block FAST_INSERTER = register("fast_inserter", new InserterBlock("fast", 8, getBasicSettings().nonOpaque()), AstromineItems.getBasicSettings());

	public static final Block BASIC_CONVEYOR = register("basic_conveyor", new ConveyorBlock(getBasicSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 16), AstromineItems.getBasicSettings());
	public static final Block BASIC_VERTICAL_CONVEYOR = register("basic_vertical_conveyor", new VerticalConveyorBlock(getBasicSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 16), AstromineItems.getBasicSettings());
	public static final Block BASIC_DOWNWARD_VERTICAL_CONVEYOR = register("basic_downward_vertical_conveyor", new DownwardVerticalConveyorBlock(getBasicSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 16), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_CONVEYOR = register("advanced_conveyor", new ConveyorBlock(getAdvancedSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 8), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_VERTICAL_CONVEYOR = register("advanced_vertical_conveyor", new VerticalConveyorBlock(getAdvancedSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 8), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_DOWNWARD_VERTICAL_CONVEYOR = register("advanced_downward_vertical_conveyor", new DownwardVerticalConveyorBlock(getAdvancedSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 8), AstromineItems.getBasicSettings());
	public static final Block ELITE_CONVEYOR = register("elite_conveyor", new ConveyorBlock(getEliteSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 4), AstromineItems.getBasicSettings());
	public static final Block ELITE_VERTICAL_CONVEYOR = register("elite_vertical_conveyor", new VerticalConveyorBlock(getEliteSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 4), AstromineItems.getBasicSettings());
	public static final Block ELITE_DOWNWARD_VERTICAL_CONVEYOR = register("elite_downward_vertical_conveyor", new DownwardVerticalConveyorBlock(getEliteSettings().sounds(BlockSoundGroup.METAL).nonOpaque(), 4), AstromineItems.getBasicSettings());

	public static final Block CATWALK = register("catwalk", new CatwalkBlock(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.METAL).nonOpaque()), AstromineItems.getBasicSettings());
	public static final Block CATWALK_STAIRS = register("catwalk_stairs", new CatwalkStairsBlock(FabricBlockSettings.copyOf(Blocks.STONE).sounds(BlockSoundGroup.METAL).nonOpaque()), AstromineItems.getBasicSettings());

	public static void initialize() {
		// Unused.
	}
}
