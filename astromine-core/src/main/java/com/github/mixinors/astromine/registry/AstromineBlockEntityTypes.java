/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.registry;

import com.github.mixinors.astromine.common.block.entity.*;
import com.github.mixinors.astromine.common.block.entity.base.AbstractConveyableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.AstromineCommon;

import java.util.function.Supplier;

public class AstromineBlockEntityTypes {
	public static final BlockEntityType<AltarPedestalBlockEntity> ALTAR_PEDESTAL = register("altar_pedestal", AltarPedestalBlockEntity::new, AstromineBlocks.ALTAR_PEDESTAL);
	public static final BlockEntityType<AltarBlockEntity> ALTAR = register("altar", AltarBlockEntity::new, AstromineBlocks.ALTAR);
	
	public static final BlockEntityType<HoloBridgeProjectorBlockEntity> HOLOGRAPHIC_BRIDGE = register("holographic_bridge", HoloBridgeProjectorBlockEntity::new, AstromineBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR);
	
	public static final BlockEntityType<VentBlockEntity> VENT = register("vent", VentBlockEntity::new, AstromineBlocks.VENT);
	
	public static final BlockEntityType<TankBlockEntity.Primitive> PRIMITIVE_TANK = register("primitive_tank", TankBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_TANK);
	public static final BlockEntityType<TankBlockEntity.Basic> BASIC_TANK = register("basic_tank", TankBlockEntity.Basic::new, AstromineBlocks.BASIC_TANK);
	public static final BlockEntityType<TankBlockEntity.Advanced> ADVANCED_TANK = register("advanced_tank", TankBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_TANK);
	public static final BlockEntityType<TankBlockEntity.Elite> ELITE_TANK = register("elite_tank", TankBlockEntity.Elite::new, AstromineBlocks.ELITE_TANK);
	public static final BlockEntityType<TankBlockEntity.Creative> CREATIVE_TANK = register("creative_tank", TankBlockEntity.Creative::new, AstromineBlocks.CREATIVE_TANK);
	
	public static final BlockEntityType<BufferBlockEntity.Primitive> PRIMITIVE_BUFFER = register("primitive_buffer", BufferBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_BUFFER);
	public static final BlockEntityType<BufferBlockEntity.Basic> BASIC_BUFFER = register("basic_buffer", BufferBlockEntity.Basic::new, AstromineBlocks.BASIC_BUFFER);
	public static final BlockEntityType<BufferBlockEntity.Advanced> ADVANCED_BUFFER = register("advanced_buffer", BufferBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_BUFFER);
	public static final BlockEntityType<BufferBlockEntity.Elite> ELITE_BUFFER = register("elite_buffer", BufferBlockEntity.Elite::new, AstromineBlocks.ELITE_BUFFER);
	public static final BlockEntityType<BufferBlockEntity.Creative> CREATIVE_BUFFER = register("creative_buffer", BufferBlockEntity.Creative::new, AstromineBlocks.CREATIVE_BUFFER);
	
	public static final BlockEntityType<SolidGeneratorBlockEntity.Primitive> PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", SolidGeneratorBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_SOLID_GENERATOR);
	public static final BlockEntityType<SolidGeneratorBlockEntity.Basic> BASIC_SOLID_GENERATOR = register("basic_solid_generator", SolidGeneratorBlockEntity.Basic::new, AstromineBlocks.BASIC_SOLID_GENERATOR);
	public static final BlockEntityType<SolidGeneratorBlockEntity.Advanced> ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", SolidGeneratorBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_SOLID_GENERATOR);
	public static final BlockEntityType<SolidGeneratorBlockEntity.Elite> ELITE_SOLID_GENERATOR = register("elite_solid_generator", SolidGeneratorBlockEntity.Elite::new, AstromineBlocks.ELITE_SOLID_GENERATOR);
	
	public static final BlockEntityType<FluidGeneratorBlockEntity.Primitive> PRIMITIVE_LIQUID_GENERATOR = register("primitive_fluid_generator", FluidGeneratorBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_LIQUID_GENERATOR);
	public static final BlockEntityType<FluidGeneratorBlockEntity.Basic> BASIC_LIQUID_GENERATOR = register("basic_fluid_generator", FluidGeneratorBlockEntity.Basic::new, AstromineBlocks.BASIC_LIQUID_GENERATOR);
	public static final BlockEntityType<FluidGeneratorBlockEntity.Advanced> ADVANCED_LIQUID_GENERATOR = register("advanced_fluid_generator", FluidGeneratorBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_LIQUID_GENERATOR);
	public static final BlockEntityType<FluidGeneratorBlockEntity.Elite> ELITE_LIQUID_GENERATOR = register("elite_fluid_generator", FluidGeneratorBlockEntity.Elite::new, AstromineBlocks.ELITE_LIQUID_GENERATOR);
	
	public static final BlockEntityType<ElectricFurnaceBlockEntity.Primitive> PRIMITIVE_ELECTRIC_FURNACE = register("primitive_electric_furnace", ElectricFurnaceBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_ELECTRIC_FURNACE);
	public static final BlockEntityType<ElectricFurnaceBlockEntity.Basic> BASIC_ELECTRIC_FURNACE = register("basic_electric_furnace", ElectricFurnaceBlockEntity.Basic::new, AstromineBlocks.BASIC_ELECTRIC_FURNACE);
	public static final BlockEntityType<ElectricFurnaceBlockEntity.Advanced> ADVANCED_ELECTRIC_FURNACE = register("advanced_electric_furnace", ElectricFurnaceBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_ELECTRIC_FURNACE);
	public static final BlockEntityType<ElectricFurnaceBlockEntity.Elite> ELITE_ELECTRIC_FURNACE = register("elite_electric_furnace", ElectricFurnaceBlockEntity.Elite::new, AstromineBlocks.ELITE_ELECTRIC_FURNACE);
	
	public static final BlockEntityType<AlloySmelterBlockEntity.Primitive> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", AlloySmelterBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_ALLOY_SMELTER);
	public static final BlockEntityType<AlloySmelterBlockEntity.Basic> BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", AlloySmelterBlockEntity.Basic::new, AstromineBlocks.BASIC_ALLOY_SMELTER);
	public static final BlockEntityType<AlloySmelterBlockEntity.Advanced> ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", AlloySmelterBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_ALLOY_SMELTER);
	public static final BlockEntityType<AlloySmelterBlockEntity.Elite> ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", AlloySmelterBlockEntity.Elite::new, AstromineBlocks.ELITE_ALLOY_SMELTER);
	
	public static final BlockEntityType<TrituratorBlockEntity.Primitive> PRIMITIVE_TRITURATOR = register("primitive_triturator", TrituratorBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_TRITURATOR);
	public static final BlockEntityType<TrituratorBlockEntity.Basic> BASIC_TRITURATOR = register("basic_triturator", TrituratorBlockEntity.Basic::new, AstromineBlocks.BASIC_TRITURATOR);
	public static final BlockEntityType<TrituratorBlockEntity.Advanced> ADVANCED_TRITURATOR = register("advanced_triturator", TrituratorBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_TRITURATOR);
	public static final BlockEntityType<TrituratorBlockEntity.Elite> ELITE_TRITURATOR = register("elite_triturator", TrituratorBlockEntity.Elite::new, AstromineBlocks.ELITE_TRITURATOR);
	
	public static final BlockEntityType<PressBlockEntity.Primitive> PRIMITIVE_PRESSER = register("primitive_press", PressBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_PRESSER);
	public static final BlockEntityType<PressBlockEntity.Basic> BASIC_PRESSER = register("basic_press", PressBlockEntity.Basic::new, AstromineBlocks.BASIC_PRESSER);
	public static final BlockEntityType<PressBlockEntity.Advanced> ADVANCED_PRESSER = register("advanced_press", PressBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_PRESSER);
	public static final BlockEntityType<PressBlockEntity.Elite> ELITE_PRESSER = register("elite_press", PressBlockEntity.Elite::new, AstromineBlocks.ELITE_PRESSER);
	
	public static final BlockEntityType<WireMillBlockEntity.Primitive> PRIMITIVE_WIREMILL = register("primitive_wire_mill", WireMillBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_WIREMILL);
	public static final BlockEntityType<WireMillBlockEntity.Basic> BASIC_WIREMILL = register("basic_wire_mill", WireMillBlockEntity.Basic::new, AstromineBlocks.BASIC_WIREMILL);
	public static final BlockEntityType<WireMillBlockEntity.Advanced> ADVANCED_WIREMILL = register("advanced_wire_mill", WireMillBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_WIREMILL);
	public static final BlockEntityType<WireMillBlockEntity.Elite> ELITE_WIREMILL = register("elite_wire_mill", WireMillBlockEntity.Elite::new, AstromineBlocks.ELITE_WIREMILL);
	
	public static final BlockEntityType<ElectrolyzerBlockEntity.Primitive> PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", ElectrolyzerBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_ELECTROLYZER);
	public static final BlockEntityType<ElectrolyzerBlockEntity.Basic> BASIC_ELECTROLYZER = register("basic_electrolyzer", ElectrolyzerBlockEntity.Basic::new, AstromineBlocks.BASIC_ELECTROLYZER);
	public static final BlockEntityType<ElectrolyzerBlockEntity.Advanced> ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", ElectrolyzerBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_ELECTROLYZER);
	public static final BlockEntityType<ElectrolyzerBlockEntity.Elite> ELITE_ELECTROLYZER = register("elite_electrolyzer", ElectrolyzerBlockEntity.Elite::new, AstromineBlocks.ELITE_ELECTROLYZER);
	
	public static final BlockEntityType<RefineryBlockEntity.Primitive> PRIMITIVE_REFINERY = register("primitive_refinery", RefineryBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_REFINERY);
	public static final BlockEntityType<RefineryBlockEntity.Basic> BASIC_REFINERY = register("basic_refinery", RefineryBlockEntity.Basic::new, AstromineBlocks.BASIC_REFINERY);
	public static final BlockEntityType<RefineryBlockEntity.Advanced> ADVANCED_REFINERY = register("advanced_refinery", RefineryBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_REFINERY);
	public static final BlockEntityType<RefineryBlockEntity.Elite> ELITE_REFINERY = register("elite_refinery", RefineryBlockEntity.Elite::new, AstromineBlocks.ELITE_REFINERY);
	
	public static final BlockEntityType<FluidMixerBlockEntity.Primitive> PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", FluidMixerBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_FLUID_MIXER);
	public static final BlockEntityType<FluidMixerBlockEntity.Basic> BASIC_FLUID_MIXER = register("basic_fluid_mixer", FluidMixerBlockEntity.Basic::new, AstromineBlocks.BASIC_FLUID_MIXER);
	public static final BlockEntityType<FluidMixerBlockEntity.Advanced> ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", FluidMixerBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_FLUID_MIXER);
	public static final BlockEntityType<FluidMixerBlockEntity.Elite> ELITE_FLUID_MIXER = register("elite_fluid_mixer", FluidMixerBlockEntity.Elite::new, AstromineBlocks.ELITE_FLUID_MIXER);
	
	public static final BlockEntityType<SolidifierBlockEntity.Primitive> PRIMITIVE_SOLIDIFIER = register("primitive_solidifier", SolidifierBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_SOLIDIFIER);
	public static final BlockEntityType<SolidifierBlockEntity.Basic> BASIC_SOLIDIFIER = register("basic_solidifier", SolidifierBlockEntity.Basic::new, AstromineBlocks.BASIC_SOLIDIFIER);
	public static final BlockEntityType<SolidifierBlockEntity.Advanced> ADVANCED_SOLIDIFIER = register("advanced_solidifier", SolidifierBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_SOLIDIFIER);
	public static final BlockEntityType<SolidifierBlockEntity.Elite> ELITE_SOLIDIFIER = register("elite_solidifier", SolidifierBlockEntity.Elite::new, AstromineBlocks.ELITE_SOLIDIFIER);
	
	public static final BlockEntityType<MelterBlockEntity.Primitive> PRIMITIVE_MELTER = register("primitive_melter", MelterBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_MELTER);
	public static final BlockEntityType<MelterBlockEntity.Basic> BASIC_MELTER = register("basic_melter", MelterBlockEntity.Basic::new, AstromineBlocks.BASIC_MELTER);
	public static final BlockEntityType<MelterBlockEntity.Advanced> ADVANCED_MELTER = register("advanced_melter", MelterBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_MELTER);
	public static final BlockEntityType<MelterBlockEntity.Elite> ELITE_MELTER = register("elite_melter", MelterBlockEntity.Elite::new, AstromineBlocks.ELITE_MELTER);
	
	public static final BlockEntityType<CapacitorBlockEntity.Primitive> PRIMITIVE_CAPACITOR = register("primitive_capacitor", CapacitorBlockEntity.Primitive::new, AstromineBlocks.PRIMITIVE_CAPACITOR);
	public static final BlockEntityType<CapacitorBlockEntity.Basic> BASIC_CAPACITOR = register("basic_capacitor", CapacitorBlockEntity.Basic::new, AstromineBlocks.BASIC_CAPACITOR);
	public static final BlockEntityType<CapacitorBlockEntity.Advanced> ADVANCED_CAPACITOR = register("advanced_capacitor", CapacitorBlockEntity.Advanced::new, AstromineBlocks.ADVANCED_CAPACITOR);
	public static final BlockEntityType<CapacitorBlockEntity.Elite> ELITE_CAPACITOR = register("elite_capacitor", CapacitorBlockEntity.Elite::new, AstromineBlocks.ELITE_CAPACITOR);
	public static final BlockEntityType<CapacitorBlockEntity.Creative> CREATIVE_CAPACITOR = register("creative_capacitor", CapacitorBlockEntity.Creative::new, AstromineBlocks.CREATIVE_CAPACITOR);
	
	public static final BlockEntityType<FluidCollectorBlockEntity> FLUID_EXTRACTOR = register("fluid_collector", FluidCollectorBlockEntity::new, AstromineBlocks.FLUID_EXTRACTOR);
	public static final BlockEntityType<FluidPlacerBlockEntity> FLUID_INSERTER = register("fluid_placer", FluidPlacerBlockEntity::new, AstromineBlocks.FLUID_INSERTER);
	
	public static final BlockEntityType<BlockBreakerBlockEntity> BLOCK_BREAKER = register("block_breaker", BlockBreakerBlockEntity::new, AstromineBlocks.BLOCK_BREAKER);
	public static final BlockEntityType<BlockPlacerBlockEntity> BLOCK_PLACER = register("block_placer", BlockPlacerBlockEntity::new, AstromineBlocks.BLOCK_PLACER);
	
	public static final BlockEntityType<AbstractConveyableBlockEntity> ALTERNATOR = register("alternator", AlternatorBlockEntity::new, AstromineBlocks.ALTERNATOR);
	public static final BlockEntityType<AbstractConveyableBlockEntity> SPLITTER = register("splitter", SplitterBlockEntity::new, AstromineBlocks.SPLITTER);
	public static final BlockEntityType<ShredderBlockEntity> INCINERATOR = register("shredder", ShredderBlockEntity::new, AstromineBlocks.SHREDDER);
	public static final BlockEntityType<InserterBlockEntity> INSERTER = register("inserter", InserterBlockEntity::new, AstromineBlocks.INSERTER, AstromineBlocks.FAST_INSERTER);
	
	public static final BlockEntityType<ConveyorBlockEntity> CONVEYOR = register("conveyor", ConveyorBlockEntity::new, AstromineBlocks.BASIC_CONVEYOR, AstromineBlocks.ADVANCED_CONVEYOR, AstromineBlocks.ELITE_CONVEYOR);
	public static final BlockEntityType<VerticalConveyorBlockEntity> VERTICAL_CONVEYOR = register("vertical_conveyor", VerticalConveyorBlockEntity::new, AstromineBlocks.BASIC_VERTICAL_CONVEYOR, AstromineBlocks.ADVANCED_VERTICAL_CONVEYOR, AstromineBlocks.ELITE_VERTICAL_CONVEYOR);
	public static final BlockEntityType<DownVerticalConveyorBlockEntity> DOWNWARD_VERTICAL_CONVEYOR = register("downward_vertical_conveyor", DownVerticalConveyorBlockEntity::new, AstromineBlocks.BASIC_DOWNWARD_VERTICAL_CONVEYOR, AstromineBlocks.ADVANCED_DOWNWARD_VERTICAL_CONVEYOR, AstromineBlocks.ELITE_DOWNWARD_VERTICAL_CONVEYOR);
	
	public static final BlockEntityType<DrainBlockEntity> DRAIN = register("drain", DrainBlockEntity::new, AstromineBlocks.DRAIN);
	
	public static void initialize() {

	}

	/**
	 * @param name
	 *        Name of BlockEntityType instance to be registered
	 * @param supplier
	 *        Supplier of BlockEntity to use for BlockEntityType
	 * @param supportedBlocks
	 *        Blocks the BlockEntity can be attached to
	 *
	 * @return Registered BlockEntityType
	 */
	public static <B extends BlockEntity> BlockEntityType<B> register(String name, Supplier<B> supplier, Block... supportedBlocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, AstromineCommon.identifier(name), BlockEntityType.Builder.create(supplier, supportedBlocks).build(null));
	}
}
