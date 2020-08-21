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

package com.github.chainmailstudios.astromine.technologies.common.registry;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.technologies.common.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;

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

	public static final Block FLUID_CABLE = register("fluid_cable", new FluidCableBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_ENERGY_CABLE = register("primitive_energy_cable", new EnergyCableBlock(16, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block BASIC_ENERGY_CABLE = register("basic_energy_cable", new EnergyCableBlock(64, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_ENERGY_CABLE = register("advanced_energy_cable", new EnergyCableBlock(384, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ELITE_ENERGY_CABLE = register("elite_energy_cable", new EnergyCableBlock(2048, FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static void initialize() {
		// Unused.
	}
}
