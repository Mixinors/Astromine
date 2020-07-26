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

package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.conveyor.entity.*;
import com.github.chainmailstudios.astromine.common.block.entity.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class AstromineBlockEntityTypes {
	public static final BlockEntityType<HolographicBridgeProjectorBlockEntity> HOLOGRAPHIC_BRIDGE = register("holographic_bridge", HolographicBridgeProjectorBlockEntity::new, AstromineBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR);

	public static final BlockEntityType<VentBlockEntity> VENT = register("vent", VentBlockEntity::new, AstromineBlocks.VENT);

	public static final BlockEntityType<TankBlockEntity> FLUID_TANK = register("tank", TankBlockEntity::new, AstromineBlocks.FLUID_TANK);

	public static final BlockEntityType<SolidGeneratorBlockEntity.Primitive> PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", SolidGeneratorBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_SOLID_GENERATOR);
	public static final BlockEntityType<SolidGeneratorBlockEntity.Basic> BASIC_SOLID_GENERATOR = register("basic_solid_generator", SolidGeneratorBlockEntity.Basic::new, AstromineBlocks.BASIC_SOLID_GENERATOR);
	public static final BlockEntityType<SolidGeneratorBlockEntity.Advanced> ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", SolidGeneratorBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_SOLID_GENERATOR);
	public static final BlockEntityType<SolidGeneratorBlockEntity.Elite> ELITE_SOLID_GENERATOR = register("elite_solid_generator", SolidGeneratorBlockEntity.Elite::new, AstromineBlocks.ELITE_SOLID_GENERATOR);

	public static final BlockEntityType<LiquidGeneratorBlockEntity.Primitive> PRIMITIVE_LIQUID_GENERATOR = register("primitive_liquid_generator", LiquidGeneratorBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_LIQUID_GENERATOR);
	public static final BlockEntityType<LiquidGeneratorBlockEntity.Basic> BASIC_LIQUID_GENERATOR = register("basic_liquid_generator", LiquidGeneratorBlockEntity.Basic::new, AstromineBlocks.BASIC_LIQUID_GENERATOR);
	public static final BlockEntityType<LiquidGeneratorBlockEntity.Advanced> ADVANCED_LIQUID_GENERATOR = register("advanced_liquid_generator", LiquidGeneratorBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_LIQUID_GENERATOR);
	public static final BlockEntityType<LiquidGeneratorBlockEntity.Elite> ELITE_LIQUID_GENERATOR = register("elite_liquid_generator", LiquidGeneratorBlockEntity.Elite::new, AstromineBlocks.ELITE_LIQUID_GENERATOR);

	public static final BlockEntityType<ElectricSmelterBlockEntity.Primitive> PRIMITIVE_ELECTRIC_SMELTER = register("primitive_electric_smelter", ElectricSmelterBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_ELECTRIC_SMELTER);
	public static final BlockEntityType<ElectricSmelterBlockEntity.Basic> BASIC_ELECTRIC_SMELTER = register("basic_electric_smelter", ElectricSmelterBlockEntity.Basic::new, AstromineBlocks.BASIC_ELECTRIC_SMELTER);
	public static final BlockEntityType<ElectricSmelterBlockEntity.Advanced> ADVANCED_ELECTRIC_SMELTER = register("advanced_electric_smelter", ElectricSmelterBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_ELECTRIC_SMELTER);
	public static final BlockEntityType<ElectricSmelterBlockEntity.Elite> ELITE_ELECTRIC_SMELTER = register("elite_electric_smelter", ElectricSmelterBlockEntity.Elite::new, AstromineBlocks.ELITE_ELECTRIC_SMELTER);

	public static final BlockEntityType<AlloySmelterBlockEntity.Primitive> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", AlloySmelterBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_ALLOY_SMELTER);
	public static final BlockEntityType<AlloySmelterBlockEntity.Basic> BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", AlloySmelterBlockEntity.Basic::new, AstromineBlocks.BASIC_ALLOY_SMELTER);
	public static final BlockEntityType<AlloySmelterBlockEntity.Advanced> ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", AlloySmelterBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_ALLOY_SMELTER);
	public static final BlockEntityType<AlloySmelterBlockEntity.Elite> ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", AlloySmelterBlockEntity.Elite::new, AstromineBlocks.ELITE_ALLOY_SMELTER);

	public static final BlockEntityType<TrituratorBlockEntity.Primitive> PRIMITIVE_TRITURATOR = register("primitive_triturator", TrituratorBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_TRITURATOR);
	public static final BlockEntityType<TrituratorBlockEntity.Basic> BASIC_TRITURATOR = register("basic_triturator", TrituratorBlockEntity.Basic::new, AstromineBlocks.BASIC_TRITURATOR);
	public static final BlockEntityType<TrituratorBlockEntity.Advanced> ADVANCED_TRITURATOR = register("advanced_triturator", TrituratorBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_TRITURATOR);
	public static final BlockEntityType<TrituratorBlockEntity.Elite> ELITE_TRITURATOR = register("elite_triturator", TrituratorBlockEntity.Elite::new, AstromineBlocks.ELITE_TRITURATOR);

	public static final BlockEntityType<PresserBlockEntity.Primitive> PRIMITIVE_PRESSER = register("primitive_presser", PresserBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_PRESSER);
	public static final BlockEntityType<PresserBlockEntity.Basic> BASIC_PRESSER = register("basic_presser", PresserBlockEntity.Basic::new, AstromineBlocks.BASIC_PRESSER);
	public static final BlockEntityType<PresserBlockEntity.Advanced> ADVANCED_PRESSER = register("advanced_presser", PresserBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_PRESSER);
	public static final BlockEntityType<PresserBlockEntity.Elite> ELITE_PRESSER = register("elite_presser", PresserBlockEntity.Elite::new, AstromineBlocks.ELITE_PRESSER);

	public static final BlockEntityType<ElectrolyzerBlockEntity.Primitive> PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", ElectrolyzerBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_ELECTROLYZER);
	public static final BlockEntityType<ElectrolyzerBlockEntity.Basic> BASIC_ELECTROLYZER = register("basic_electrolyzer", ElectrolyzerBlockEntity.Basic::new, AstromineBlocks.BASIC_ELECTROLYZER);
	public static final BlockEntityType<ElectrolyzerBlockEntity.Advanced> ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", ElectrolyzerBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_ELECTROLYZER);
	public static final BlockEntityType<ElectrolyzerBlockEntity.Elite> ELITE_ELECTROLYZER = register("elite_electrolyzer", ElectrolyzerBlockEntity.Elite::new, AstromineBlocks.ELITE_ELECTROLYZER);

	public static final BlockEntityType<FluidMixerBlockEntity.Primitive> PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", FluidMixerBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_FLUID_MIXER);
	public static final BlockEntityType<FluidMixerBlockEntity.Basic> BASIC_FLUID_MIXER = register("basic_fluid_mixer", FluidMixerBlockEntity.Basic::new, AstromineBlocks.BASIC_FLUID_MIXER);
	public static final BlockEntityType<FluidMixerBlockEntity.Advanced> ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", FluidMixerBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_FLUID_MIXER);
	public static final BlockEntityType<FluidMixerBlockEntity.Elite> ELITE_FLUID_MIXER = register("elite_fluid_mixer", FluidMixerBlockEntity.Elite::new, AstromineBlocks.ELITE_FLUID_MIXER);

	public static final BlockEntityType<CreativeTankBlockEntity> CREATIVE_TANK = register("creative_tank", CreativeTankBlockEntity::new, AstromineBlocks.CREATIVE_TANK);
	public static final BlockEntityType<CreativeCapacitorBlockEntity> CREATIVE_CAPACITOR = register("creative_capacitor", CreativeCapacitorBlockEntity::new, AstromineBlocks.CREATIVE_CAPACITOR);
	public static final BlockEntityType<CreativeBufferBlockEntity> CREATIVE_BUFFER = register("creative_buffer", CreativeBufferBlockEntity::new, AstromineBlocks.CREATIVE_BUFFER);

	public static final BlockEntityType<FluidExtractorBlockEntity> FLUID_EXTRACTOR = register("fluid_extractor", FluidExtractorBlockEntity::new, AstromineBlocks.FLUID_EXTRACTOR);
	public static final BlockEntityType<FluidInserterBlockEntity> FLUID_INSERTER = register("fluid_inserter", FluidInserterBlockEntity::new, AstromineBlocks.FLUID_INSERTER);

	public static final BlockEntityType<BlockBreakerBlockEntity> BLOCK_BREAKER = register("block_breaker", BlockBreakerBlockEntity::new, AstromineBlocks.BLOCK_BREAKER);
	public static final BlockEntityType<BlockPlacerBlockEntity> BLOCK_PLACER = register("block_placer", BlockPlacerBlockEntity::new, AstromineBlocks.BLOCK_PLACER);

	public static final BlockEntityType<BufferBlockEntity> BUFFER = register("buffer", BufferBlockEntity::new, AstromineBlocks.BASIC_BUFFER, AstromineBlocks.ADVANCED_BUFFER, AstromineBlocks.ELITE_BUFFER);

	public static BlockEntityType<DoubleMachineBlockEntity> ALTERNATOR = register("alternator", AlternatorBlockEntity::new, AstromineBlocks.ALTERNATOR);
	public static BlockEntityType<DoubleMachineBlockEntity> SPLITTER = register("splitter", SplitterBlockEntity::new, AstromineBlocks.SPLITTER);
	public static BlockEntityType<IncineratorBlockEntity> INCINERATOR = register("incinerator", IncineratorBlockEntity::new, AstromineBlocks.INCINERATOR);
	public static BlockEntityType<InserterBlockEntity> INSERTER = register("inserter", InserterBlockEntity::new, AstromineBlocks.INSERTER, AstromineBlocks.FAST_INSERTER);

	public static BlockEntityType<ConveyorBlockEntity> CONVEYOR = register("conveyor", ConveyorBlockEntity::new, AstromineBlocks.BASIC_CONVEYOR, AstromineBlocks.ADVANCED_CONVEYOR, AstromineBlocks.ELITE_CONVEYOR);
	public static BlockEntityType<VerticalConveyorBlockEntity> VERTICAL_CONVEYOR = register("vertical_conveyor", VerticalConveyorBlockEntity::new, AstromineBlocks.BASIC_VERTICAL_CONVEYOR, AstromineBlocks.ADVANCED_VERTICAL_CONVEYOR, AstromineBlocks.ELITE_VERTICAL_CONVEYOR);
	public static BlockEntityType<DownVerticalConveyorBlockEntity> DOWNWARD_VERTICAL_CONVEYOR = register("downward_vertical_conveyor", DownVerticalConveyorBlockEntity::new, AstromineBlocks.BASIC_DOWNWARD_VERTICAL_CONVEYOR, AstromineBlocks.ADVANCED_DOWNWARD_VERTICAL_CONVEYOR, AstromineBlocks.ELITE_DOWNWARD_VERTICAL_CONVEYOR);

	public static void initialize() {
		// Unused.
	}

	/**
	 * @param name
	 *        Name of BlockEntityType instance to be registered
	 * @param supplier
	 *        Supplier of BlockEntity to use for BlockEntityType
	 * @param supportedBlocks
	 *        Blocks the BlockEntity can be attached to
	 * @return Registered BlockEntityType
	 */
	public static <B extends BlockEntity> BlockEntityType<B> register(String name, Supplier<B> supplier, Block... supportedBlocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, AstromineCommon.identifier(name), BlockEntityType.Builder.create(supplier, supportedBlocks).build(null));
	}
}
