package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.common.block.*;
import com.github.chainmailstudios.astromine.common.block.AlloySmelterBlock;
import com.github.chainmailstudios.astromine.common.block.BlockBreakerBlock;
import com.github.chainmailstudios.astromine.common.block.BlockPlacerBlock;
import com.github.chainmailstudios.astromine.common.block.BufferBlock;
import com.github.chainmailstudios.astromine.common.block.CapacitorBlock;
import com.github.chainmailstudios.astromine.common.block.CreativeBufferBlock;
import com.github.chainmailstudios.astromine.common.block.CreativeTankBlock;
import com.github.chainmailstudios.astromine.common.block.ElectricSmelterBlock;
import com.github.chainmailstudios.astromine.common.block.ElectrolyzerBlock;
import com.github.chainmailstudios.astromine.common.block.FluidExtractorBlock;
import com.github.chainmailstudios.astromine.common.block.FluidInserterBlock;
import com.github.chainmailstudios.astromine.common.block.FluidMixerBlock;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeInvisibleBlock;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeProjectorBlock;
import com.github.chainmailstudios.astromine.common.block.LiquidGeneratorBlock;
import com.github.chainmailstudios.astromine.common.block.NuclearWarheadBlock;
import com.github.chainmailstudios.astromine.common.block.PresserBlock;
import com.github.chainmailstudios.astromine.common.block.SolidGeneratorBlock;
import com.github.chainmailstudios.astromine.common.block.TankBlock;
import com.github.chainmailstudios.astromine.common.block.TrituratorBlock;
import com.github.chainmailstudios.astromine.common.block.VentBlock;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class AstromineTechnologiesBlocks extends AstromineBlocks {
	public static final Block HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", new HolographicBridgeProjectorBlock(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", new HolographicBridgeInvisibleBlock(FabricBlockSettings.of(HolographicBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().lightLevel(15).allowsSpawning((a, b, c, d) -> false)));

	public static final Block VENT = register("vent", new VentBlock(getAdvancedSettings()), AstromineItems.getBasicSettings());

	public static final Block TANK = register("tank", new TankBlock(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block CREATIVE_TANK = register("creative_tank", new CreativeTankBlock(getCreativeSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", new SolidGeneratorBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_SOLID_GENERATOR = register("basic_solid_generator", new SolidGeneratorBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", new SolidGeneratorBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_SOLID_GENERATOR = register("elite_solid_generator", new SolidGeneratorBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_LIQUID_GENERATOR = register("primitive_liquid_generator", new LiquidGeneratorBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_LIQUID_GENERATOR = register("basic_liquid_generator", new LiquidGeneratorBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_LIQUID_GENERATOR = register("advanced_liquid_generator", new LiquidGeneratorBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_LIQUID_GENERATOR = register("elite_liquid_generator", new LiquidGeneratorBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_ELECTRIC_SMELTER = register("primitive_electric_smelter", new ElectricSmelterBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_ELECTRIC_SMELTER = register("basic_electric_smelter", new ElectricSmelterBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_ELECTRIC_SMELTER = register("advanced_electric_smelter", new ElectricSmelterBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_ELECTRIC_SMELTER = register("elite_electric_smelter", new ElectricSmelterBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", new AlloySmelterBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", new AlloySmelterBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", new AlloySmelterBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", new AlloySmelterBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_TRITURATOR = register("primitive_triturator", new TrituratorBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_TRITURATOR = register("basic_triturator", new TrituratorBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_TRITURATOR = register("advanced_triturator", new TrituratorBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_TRITURATOR = register("elite_triturator", new TrituratorBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_PRESSER = register("primitive_presser", new PresserBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_PRESSER = register("basic_presser", new PresserBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_PRESSER = register("advanced_presser", new PresserBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_PRESSER = register("elite_presser", new PresserBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", new ElectrolyzerBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_ELECTROLYZER = register("basic_electrolyzer", new ElectrolyzerBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", new ElectrolyzerBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_ELECTROLYZER = register("elite_electrolyzer", new ElectrolyzerBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", new FluidMixerBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_FLUID_MIXER = register("basic_fluid_mixer", new FluidMixerBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", new FluidMixerBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_FLUID_MIXER = register("elite_fluid_mixer", new FluidMixerBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());

	public static final Block BASIC_BUFFER = register("basic_buffer", new BufferBlock(BufferType.BASIC, getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_BUFFER = register("advanced_buffer", new BufferBlock(BufferType.ADVANCED, getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_BUFFER = register("elite_buffer", new BufferBlock(BufferType.ELITE, getEliteSettings()), AstromineItems.getBasicSettings());
	public static final Block CREATIVE_BUFFER = register("creative_buffer", new CreativeBufferBlock(getCreativeSettings()), AstromineItems.getBasicSettings());

	public static final Block FLUID_EXTRACTOR = register("fluid_extractor", new FluidExtractorBlock(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block FLUID_INSERTER = register("fluid_inserter", new FluidInserterBlock(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block BLOCK_BREAKER = register("block_breaker", new BlockBreakerBlock(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block BLOCK_PLACER = register("block_placer", new BlockPlacerBlock(getAdvancedSettings()), AstromineItems.getBasicSettings());

	public static final Block NUCLEAR_WARHEAD = register("nuclear_warhead", new NuclearWarheadBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(1F, 4F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	
	public static final Block PRIMITIVE_CAPACITOR = register("primitive_capacitor", new CapacitorBlock.Primitive(getPrimitiveSettings()), AstromineItems.getBasicSettings());
	public static final Block BASIC_CAPACITOR = register("basic_capacitor", new CapacitorBlock.Basic(getBasicSettings()), AstromineItems.getBasicSettings());
	public static final Block ADVANCED_CAPACITOR = register("advanced_capacitor", new CapacitorBlock.Advanced(getAdvancedSettings()), AstromineItems.getBasicSettings());
	public static final Block ELITE_CAPACITOR = register("elite_capacitor", new CapacitorBlock.Elite(getEliteSettings()), AstromineItems.getBasicSettings());
	public static final Block CREATIVE_CAPACITOR = register("creative_capacitor", new CapacitorBlock.Creative(getCreativeSettings()), AstromineItems.getBasicSettings());
	
	public static void initialize() {

	}
}
