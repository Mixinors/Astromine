package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class AstromineBlockEntityTypes {
	public static BlockEntityType<HolographicBridgeProjectorBlockEntity> HOLOGRAPHIC_BRIDGE = register("holographic_bridge", HolographicBridgeProjectorBlockEntity::new, AstromineBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR);

	public static BlockEntityType<VentBlockEntity> VENT = register("vent", VentBlockEntity::new, AstromineBlocks.VENT);

	public static BlockEntityType<TankBlockEntity> FLUID_TANK = register("tank", TankBlockEntity::new, AstromineBlocks.FLUID_TANK);

	public static BlockEntityType<LiquidGeneratorBlockEntity> LIQUID_GENERATOR = register("liquid_generator", LiquidGeneratorBlockEntity::new, AstromineBlocks.LIQUID_GENERATOR);

	public static BlockEntityType<CreativeTankBlockEntity> CREATIVE_TANK = register("creative_tank", CreativeTankBlockEntity::new, AstromineBlocks.CREATIVE_TANK);
	public static BlockEntityType<CreativeCapacitorBlockEntity> CREATIVE_CAPACITOR = register("creative_capacitor", CreativeCapacitorBlockEntity::new, AstromineBlocks.CREATIVE_CAPACITOR);
	public static BlockEntityType<CreativeBufferBlockEntity> CREATIVE_BUFFER = register("creative_buffer", CreativeBufferBlockEntity::new, AstromineBlocks.CREATIVE_BUFFER);

	public static BlockEntityType<SorterBlockEntity> SORTER = register("sorter", SorterBlockEntity::new, AstromineBlocks.SORTER);
	public static BlockEntityType<SmelterBlockEntity> SMELTER = register("smelter", SmelterBlockEntity::new, AstromineBlocks.SMELTER);

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
