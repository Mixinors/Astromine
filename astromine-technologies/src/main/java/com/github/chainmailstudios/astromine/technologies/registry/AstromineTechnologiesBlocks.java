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

package com.github.chainmailstudios.astromine.technologies.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.technologies.common.block.*;

public class AstromineTechnologiesBlocks extends AstromineBlocks {
	public static final Block HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", new HolographicBridgeProjectorBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", new HolographicBridgeInvisibleBlock(FabricBlockSettings.of(HolographicBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().lightLevel(15).allowsSpawning((a, b, c, d) -> false)));

	public static final Block VENT = register("vent", new VentBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_TANK = register("primitive_tank", new TankBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_TANK = register("basic_tank", new TankBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_TANK = register("advanced_tank", new TankBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_TANK = register("elite_tank", new TankBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block CREATIVE_TANK = register("creative_tank", new TankBlock.Creative(getCreativeSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", new SolidGeneratorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_SOLID_GENERATOR = register("basic_solid_generator", new SolidGeneratorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", new SolidGeneratorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_SOLID_GENERATOR = register("elite_solid_generator", new SolidGeneratorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_LIQUID_GENERATOR = register("primitive_fluid_generator", new FluidGeneratorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_LIQUID_GENERATOR = register("basic_fluid_generator", new FluidGeneratorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_LIQUID_GENERATOR = register("advanced_fluid_generator", new FluidGeneratorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_LIQUID_GENERATOR = register("elite_fluid_generator", new FluidGeneratorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_ELECTRIC_FURNACE = register("primitive_electric_furnace", new ElectricFurnaceBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_ELECTRIC_FURNACE = register("basic_electric_furnace", new ElectricFurnaceBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_ELECTRIC_FURNACE = register("advanced_electric_furnace", new ElectricFurnaceBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_ELECTRIC_FURNACE = register("elite_electric_furnace", new ElectricFurnaceBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", new AlloySmelterBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", new AlloySmelterBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", new AlloySmelterBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", new AlloySmelterBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_TRITURATOR = register("primitive_triturator", new TrituratorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_TRITURATOR = register("basic_triturator", new TrituratorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_TRITURATOR = register("advanced_triturator", new TrituratorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_TRITURATOR = register("elite_triturator", new TrituratorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_PRESSER = register("primitive_press", new PressBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_PRESSER = register("basic_press", new PressBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_PRESSER = register("advanced_press", new PressBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_PRESSER = register("elite_press", new PressBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_WIREMILL = register("primitive_wire_mill", new WireMillBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_WIREMILL = register("basic_wire_mill", new WireMillBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_WIREMILL = register("advanced_wire_mill", new WireMillBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_WIREMILL = register("elite_wire_mill", new WireMillBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", new ElectrolyzerBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_ELECTROLYZER = register("basic_electrolyzer", new ElectrolyzerBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", new ElectrolyzerBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_ELECTROLYZER = register("elite_electrolyzer", new ElectrolyzerBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_REFINERY = register("primitive_refinery", new RefineryBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_REFINERY = register("basic_refinery", new RefineryBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_REFINERY = register("advanced_refinery", new RefineryBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_REFINERY = register("elite_refinery", new RefineryBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", new FluidMixerBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_FLUID_MIXER = register("basic_fluid_mixer", new FluidMixerBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", new FluidMixerBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_FLUID_MIXER = register("elite_fluid_mixer", new FluidMixerBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_SOLIDIFIER = register("primitive_solidifier", new SolidifierBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_SOLIDIFIER = register("basic_solidifier", new SolidifierBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_SOLIDIFIER = register("advanced_solidifier", new SolidifierBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_SOLIDIFIER = register("elite_solidifier", new SolidifierBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_MELTER = register("primitive_melter", new MelterBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_MELTER = register("basic_melter", new MelterBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_MELTER = register("advanced_melter", new MelterBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_MELTER = register("elite_melter", new MelterBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_BUFFER = register("primitive_buffer", new BufferBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_BUFFER = register("basic_buffer", new BufferBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_BUFFER = register("advanced_buffer", new BufferBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_BUFFER = register("elite_buffer", new BufferBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block CREATIVE_BUFFER = register("creative_buffer", new BufferBlock.Creative(getCreativeSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block FLUID_EXTRACTOR = register("fluid_collector", new FluidCollectorBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block FLUID_INSERTER = register("fluid_placer", new FluidPlacerBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BLOCK_BREAKER = register("block_breaker", new BlockBreakerBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BLOCK_PLACER = register("block_placer", new BlockPlacerBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block FLUID_PUMP = register("fluid_pump", new FluidPumpBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block PUMP_PIPE = register("pump_pipe", new PumpPipeBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block NUCLEAR_WARHEAD = register("nuclear_warhead", new NuclearWarheadBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(1F, 4F).sounds(BlockSoundGroup.METAL)), AstromineTechnologiesItems
		.getBasicSettings());

	public static final Block PRIMITIVE_CAPACITOR = register("primitive_capacitor", new CapacitorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_CAPACITOR = register("basic_capacitor", new CapacitorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_CAPACITOR = register("advanced_capacitor", new CapacitorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_CAPACITOR = register("elite_capacitor", new CapacitorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block CREATIVE_CAPACITOR = register("creative_capacitor", new CapacitorBlock.Creative(getCreativeSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block AIRLOCK = new AirlockBlock(getBasicSettings());

	public static void initialize() {
		register("airlock", AIRLOCK, new TallBlockItem(AIRLOCK, AstromineTechnologiesItems.getBasicSettings()));
	}
}
