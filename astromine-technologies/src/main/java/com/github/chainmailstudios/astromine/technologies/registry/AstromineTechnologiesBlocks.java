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

import com.github.chainmailstudios.astromine.technologies.common.block.*;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class AstromineTechnologiesBlocks extends AstromineBlocks {
	public static final Block HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", new HolographicBridgeProjectorBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", new HolographicBridgeInvisibleBlock(FabricBlockSettings.of(HolographicBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().lightLevel(15)
		.allowsSpawning((a, b, c, d) -> false)));

	public static final Block VENT = register("vent", new VentBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block TANK = register("tank", new TankBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block CREATIVE_TANK = register("creative_tank", new CreativeTankBlock(getCreativeSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", new SolidGeneratorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_SOLID_GENERATOR = register("basic_solid_generator", new SolidGeneratorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", new SolidGeneratorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_SOLID_GENERATOR = register("elite_solid_generator", new SolidGeneratorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_LIQUID_GENERATOR = register("primitive_liquid_generator", new LiquidGeneratorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_LIQUID_GENERATOR = register("basic_liquid_generator", new LiquidGeneratorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_LIQUID_GENERATOR = register("advanced_liquid_generator", new LiquidGeneratorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_LIQUID_GENERATOR = register("elite_liquid_generator", new LiquidGeneratorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_ELECTRIC_SMELTER = register("primitive_electric_smelter", new ElectricSmelterBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_ELECTRIC_SMELTER = register("basic_electric_smelter", new ElectricSmelterBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_ELECTRIC_SMELTER = register("advanced_electric_smelter", new ElectricSmelterBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_ELECTRIC_SMELTER = register("elite_electric_smelter", new ElectricSmelterBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", new AlloySmelterBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", new AlloySmelterBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", new AlloySmelterBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", new AlloySmelterBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_TRITURATOR = register("primitive_triturator", new TrituratorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_TRITURATOR = register("basic_triturator", new TrituratorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_TRITURATOR = register("advanced_triturator", new TrituratorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_TRITURATOR = register("elite_triturator", new TrituratorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_PRESSER = register("primitive_presser", new PresserBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_PRESSER = register("basic_presser", new PresserBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_PRESSER = register("advanced_presser", new PresserBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_PRESSER = register("elite_presser", new PresserBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", new ElectrolyzerBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_ELECTROLYZER = register("basic_electrolyzer", new ElectrolyzerBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", new ElectrolyzerBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_ELECTROLYZER = register("elite_electrolyzer", new ElectrolyzerBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", new FluidMixerBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_FLUID_MIXER = register("basic_fluid_mixer", new FluidMixerBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", new FluidMixerBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_FLUID_MIXER = register("elite_fluid_mixer", new FluidMixerBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block BASIC_BUFFER = register("basic_buffer", new BufferBlock(BufferType.BASIC, getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_BUFFER = register("advanced_buffer", new BufferBlock(BufferType.ADVANCED, getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_BUFFER = register("elite_buffer", new BufferBlock(BufferType.ELITE, getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block CREATIVE_BUFFER = register("creative_buffer", new CreativeBufferBlock(getCreativeSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block FLUID_EXTRACTOR = register("fluid_extractor", new FluidExtractorBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block FLUID_INSERTER = register("fluid_inserter", new FluidInserterBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BLOCK_BREAKER = register("block_breaker", new BlockBreakerBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BLOCK_PLACER = register("block_placer", new BlockPlacerBlock(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static final Block NUCLEAR_WARHEAD = register("nuclear_warhead", new NuclearWarheadBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(1F, 4F).sounds(BlockSoundGroup.METAL)), AstromineTechnologiesItems.getBasicSettings());

	public static final Block PRIMITIVE_CAPACITOR = register("primitive_capacitor", new CapacitorBlock.Primitive(getPrimitiveSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block BASIC_CAPACITOR = register("basic_capacitor", new CapacitorBlock.Basic(getBasicSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ADVANCED_CAPACITOR = register("advanced_capacitor", new CapacitorBlock.Advanced(getAdvancedSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block ELITE_CAPACITOR = register("elite_capacitor", new CapacitorBlock.Elite(getEliteSettings()), AstromineTechnologiesItems.getBasicSettings());
	public static final Block CREATIVE_CAPACITOR = register("creative_capacitor", new CapacitorBlock.Creative(getCreativeSettings()), AstromineTechnologiesItems.getBasicSettings());

	public static void initialize() {

	}
}
