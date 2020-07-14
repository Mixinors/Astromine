package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.block.entity.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;

import java.util.function.Supplier;

public class AstromineBlockEntityTypes {
	public static final BlockEntityType<HolographicBridgeProjectorBlockEntity> HOLOGRAPHIC_BRIDGE = register("holographic_bridge", HolographicBridgeProjectorBlockEntity::new, AstromineBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR);

	public static final BlockEntityType<VentBlockEntity> VENT = register("vent", VentBlockEntity::new, AstromineBlocks.VENT);

	public static final BlockEntityType<TankBlockEntity> FLUID_TANK = register("tank", TankBlockEntity::new, AstromineBlocks.FLUID_TANK);

	public static final BlockEntityType<LiquidGeneratorBlockEntity> LIQUID_GENERATOR = register("liquid_generator", LiquidGeneratorBlockEntity::new, AstromineBlocks.LIQUID_GENERATOR);
	public static final BlockEntityType<SolidGeneratorBlockEntity> SOLID_GENERATOR = register("solid_generator", SolidGeneratorBlockEntity::new, AstromineBlocks.SOLID_GENERATOR);

	public static final BlockEntityType<CreativeTankBlockEntity> CREATIVE_TANK = register("creative_tank", CreativeTankBlockEntity::new, AstromineBlocks.CREATIVE_TANK);
	public static final BlockEntityType<CreativeCapacitorBlockEntity> CREATIVE_CAPACITOR = register("creative_capacitor", CreativeCapacitorBlockEntity::new, AstromineBlocks.CREATIVE_CAPACITOR);
	public static final BlockEntityType<CreativeBufferBlockEntity> CREATIVE_BUFFER = register("creative_buffer", CreativeBufferBlockEntity::new, AstromineBlocks.CREATIVE_BUFFER);

	public static final BlockEntityType<SorterBlockEntity> SORTER = register("sorter", SorterBlockEntity::new, AstromineBlocks.SORTER);
	public static final BlockEntityType<PresserBlockEntity> PRESSER = register("presser", PresserBlockEntity::new, AstromineBlocks.PRESSER);
	public static final BlockEntityType<ElectricSmelterBlockEntity> ELECTRIC_SMELTER = register("electric_smelter", ElectricSmelterBlockEntity::new, AstromineBlocks.ELECTRIC_SMELTER);
	public static final BlockEntityType<FluidExtractorBlockEntity> FLUID_EXTRACTOR = register("fluid_extractor", FluidExtractorBlockEntity::new, AstromineBlocks.FLUID_EXTRACTOR);
	public static final BlockEntityType<ElectrolyzerBlockEntity> ELECTROLYZER = register("electrolyzer", ElectrolyzerBlockEntity::new, AstromineBlocks.ELECTROLYZER);
	public static final BlockEntityType<FluidMixerBlockEntity> FLUID_MIXER = register("fluid_mixer", FluidMixerBlockEntity::new, AstromineBlocks.FLUID_MIXER);

	public static void initialize() {
		// Unused.
	}

	/**
	 * @param name            Name of BlockEntityType instance to be registered
	 * @param supplier        Supplier of BlockEntity to use for BlockEntityType
	 * @param supportedBlocks Blocks the BlockEntity can be attached to
	 * @return Registered BlockEntityType
	 */
	public static <B extends BlockEntity> BlockEntityType<B> register(String name, Supplier<B> supplier, Block... supportedBlocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, AstromineCommon.identifier(name), BlockEntityType.Builder.create(supplier, supportedBlocks).build(null));
	}
}
