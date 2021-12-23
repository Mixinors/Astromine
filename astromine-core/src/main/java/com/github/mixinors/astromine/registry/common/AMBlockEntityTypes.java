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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.block.entity.*;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.AMCommon;

import java.util.function.Supplier;

public class AMBlockEntityTypes {
	public static final RegistrySupplier<BlockEntityType<AltarPedestalBlockEntity>> ALTAR_PEDESTAL = register("altar_pedestal", AltarPedestalBlockEntity::new, AMBlocks.ALTAR_PEDESTAL);
	public static final RegistrySupplier<BlockEntityType<AltarBlockEntity>> ALTAR = register("altar", AltarBlockEntity::new, AMBlocks.ALTAR);
	
	public static final RegistrySupplier<BlockEntityType<HoloBridgeProjectorBlockEntity>> HOLOGRAPHIC_BRIDGE = register("holographic_bridge", HoloBridgeProjectorBlockEntity::new, AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR);
	
	public static final RegistrySupplier<BlockEntityType<VentBlockEntity>> VENT = register("vent", VentBlockEntity::new, AMBlocks.VENT);
	
	public static final RegistrySupplier<BlockEntityType<TankBlockEntity.Primitive>> PRIMITIVE_TANK = register("primitive_tank", TankBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_TANK);
	public static final RegistrySupplier<BlockEntityType<TankBlockEntity.Basic>> BASIC_TANK = register("basic_tank", TankBlockEntity.Basic::new, AMBlocks.BASIC_TANK);
	public static final RegistrySupplier<BlockEntityType<TankBlockEntity.Advanced>> ADVANCED_TANK = register("advanced_tank", TankBlockEntity.Advanced::new, AMBlocks.ADVANCED_TANK);
	public static final RegistrySupplier<BlockEntityType<TankBlockEntity.Elite>> ELITE_TANK = register("elite_tank", TankBlockEntity.Elite::new, AMBlocks.ELITE_TANK);
	public static final RegistrySupplier<BlockEntityType<TankBlockEntity.Creative>> CREATIVE_TANK = register("creative_tank", TankBlockEntity.Creative::new, AMBlocks.CREATIVE_TANK);
	
	public static final RegistrySupplier<BlockEntityType<BufferBlockEntity.Primitive>> PRIMITIVE_BUFFER = register("primitive_buffer", BufferBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_BUFFER);
	public static final RegistrySupplier<BlockEntityType<BufferBlockEntity.Basic>> BASIC_BUFFER = register("basic_buffer", BufferBlockEntity.Basic::new, AMBlocks.BASIC_BUFFER);
	public static final RegistrySupplier<BlockEntityType<BufferBlockEntity.Advanced>> ADVANCED_BUFFER = register("advanced_buffer", BufferBlockEntity.Advanced::new, AMBlocks.ADVANCED_BUFFER);
	public static final RegistrySupplier<BlockEntityType<BufferBlockEntity.Elite>> ELITE_BUFFER = register("elite_buffer", BufferBlockEntity.Elite::new, AMBlocks.ELITE_BUFFER);
	public static final RegistrySupplier<BlockEntityType<BufferBlockEntity.Creative>> CREATIVE_BUFFER = register("creative_buffer", BufferBlockEntity.Creative::new, AMBlocks.CREATIVE_BUFFER);
	
	public static final RegistrySupplier<BlockEntityType<SolidGeneratorBlockEntity.Primitive>> PRIMITIVE_SOLID_GENERATOR = register("primitive_solid_generator", SolidGeneratorBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_SOLID_GENERATOR);
	public static final RegistrySupplier<BlockEntityType<SolidGeneratorBlockEntity.Basic>> BASIC_SOLID_GENERATOR = register("basic_solid_generator", SolidGeneratorBlockEntity.Basic::new, AMBlocks.BASIC_SOLID_GENERATOR);
	public static final RegistrySupplier<BlockEntityType<SolidGeneratorBlockEntity.Advanced>> ADVANCED_SOLID_GENERATOR = register("advanced_solid_generator", SolidGeneratorBlockEntity.Advanced::new, AMBlocks.ADVANCED_SOLID_GENERATOR);
	public static final RegistrySupplier<BlockEntityType<SolidGeneratorBlockEntity.Elite>> ELITE_SOLID_GENERATOR = register("elite_solid_generator", SolidGeneratorBlockEntity.Elite::new, AMBlocks.ELITE_SOLID_GENERATOR);
	
	public static final RegistrySupplier<BlockEntityType<FluidGeneratorBlockEntity.Primitive>> PRIMITIVE_LIQUID_GENERATOR = register("primitive_fluid_generator", FluidGeneratorBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_FLUID_GENERATOR);
	public static final RegistrySupplier<BlockEntityType<FluidGeneratorBlockEntity.Basic>> BASIC_LIQUID_GENERATOR = register("basic_fluid_generator", FluidGeneratorBlockEntity.Basic::new, AMBlocks.BASIC_FLUID_GENERATOR);
	public static final RegistrySupplier<BlockEntityType<FluidGeneratorBlockEntity.Advanced>> ADVANCED_LIQUID_GENERATOR = register("advanced_fluid_generator", FluidGeneratorBlockEntity.Advanced::new, AMBlocks.ADVANCED_FLUID_GENERATOR);
	public static final RegistrySupplier<BlockEntityType<FluidGeneratorBlockEntity.Elite>> ELITE_LIQUID_GENERATOR = register("elite_fluid_generator", FluidGeneratorBlockEntity.Elite::new, AMBlocks.ELITE_FLUID_GENERATOR);
	
	public static final RegistrySupplier<BlockEntityType<ElectricFurnaceBlockEntity.Primitive>> PRIMITIVE_ELECTRIC_FURNACE = register("primitive_electric_furnace", ElectricFurnaceBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_ELECTRIC_FURNACE);
	public static final RegistrySupplier<BlockEntityType<ElectricFurnaceBlockEntity.Basic>> BASIC_ELECTRIC_FURNACE = register("basic_electric_furnace", ElectricFurnaceBlockEntity.Basic::new, AMBlocks.BASIC_ELECTRIC_FURNACE);
	public static final RegistrySupplier<BlockEntityType<ElectricFurnaceBlockEntity.Advanced>> ADVANCED_ELECTRIC_FURNACE = register("advanced_electric_furnace", ElectricFurnaceBlockEntity.Advanced::new, AMBlocks.ADVANCED_ELECTRIC_FURNACE);
	public static final RegistrySupplier<BlockEntityType<ElectricFurnaceBlockEntity.Elite>> ELITE_ELECTRIC_FURNACE = register("elite_electric_furnace", ElectricFurnaceBlockEntity.Elite::new, AMBlocks.ELITE_ELECTRIC_FURNACE);
	
	public static final RegistrySupplier<BlockEntityType<AlloySmelterBlockEntity.Primitive>> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", AlloySmelterBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_ALLOY_SMELTER);
	public static final RegistrySupplier<BlockEntityType<AlloySmelterBlockEntity.Basic>> BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", AlloySmelterBlockEntity.Basic::new, AMBlocks.BASIC_ALLOY_SMELTER);
	public static final RegistrySupplier<BlockEntityType<AlloySmelterBlockEntity.Advanced>> ADVANCED_ALLOY_SMELTER = register("advanced_alloy_smelter", AlloySmelterBlockEntity.Advanced::new, AMBlocks.ADVANCED_ALLOY_SMELTER);
	public static final RegistrySupplier<BlockEntityType<AlloySmelterBlockEntity.Elite>> ELITE_ALLOY_SMELTER = register("elite_alloy_smelter", AlloySmelterBlockEntity.Elite::new, AMBlocks.ELITE_ALLOY_SMELTER);
	
	public static final RegistrySupplier<BlockEntityType<TrituratorBlockEntity.Primitive>> PRIMITIVE_TRITURATOR = register("primitive_triturator", TrituratorBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_TRITURATOR);
	public static final RegistrySupplier<BlockEntityType<TrituratorBlockEntity.Basic>> BASIC_TRITURATOR = register("basic_triturator", TrituratorBlockEntity.Basic::new, AMBlocks.BASIC_TRITURATOR);
	public static final RegistrySupplier<BlockEntityType<TrituratorBlockEntity.Advanced>> ADVANCED_TRITURATOR = register("advanced_triturator", TrituratorBlockEntity.Advanced::new, AMBlocks.ADVANCED_TRITURATOR);
	public static final RegistrySupplier<BlockEntityType<TrituratorBlockEntity.Elite>> ELITE_TRITURATOR = register("elite_triturator", TrituratorBlockEntity.Elite::new, AMBlocks.ELITE_TRITURATOR);
	
	public static final RegistrySupplier<BlockEntityType<PressBlockEntity.Primitive>> PRIMITIVE_PRESSER = register("primitive_press", PressBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_PRESSER);
	public static final RegistrySupplier<BlockEntityType<PressBlockEntity.Basic>> BASIC_PRESSER = register("basic_press", PressBlockEntity.Basic::new, AMBlocks.BASIC_PRESSER);
	public static final RegistrySupplier<BlockEntityType<PressBlockEntity.Advanced>> ADVANCED_PRESSER = register("advanced_press", PressBlockEntity.Advanced::new, AMBlocks.ADVANCED_PRESSER);
	public static final RegistrySupplier<BlockEntityType<PressBlockEntity.Elite>> ELITE_PRESSER = register("elite_press", PressBlockEntity.Elite::new, AMBlocks.ELITE_PRESSER);
	
	public static final RegistrySupplier<BlockEntityType<WireMillBlockEntity.Primitive>> PRIMITIVE_WIRE_MILL = register("primitive_wire_mill", WireMillBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_WIRE_MILL);
	public static final RegistrySupplier<BlockEntityType<WireMillBlockEntity.Basic>> BASIC_WIRE_MILL = register("basic_wire_mill", WireMillBlockEntity.Basic::new, AMBlocks.BASIC_WIRE_MILL);
	public static final RegistrySupplier<BlockEntityType<WireMillBlockEntity.Advanced>> ADVANCED_WIRE_MILL = register("advanced_wire_mill", WireMillBlockEntity.Advanced::new, AMBlocks.ADVANCED_WIRE_MILL);
	public static final RegistrySupplier<BlockEntityType<WireMillBlockEntity.Elite>> ELITE_WIRE_MILL = register("elite_wire_mill", WireMillBlockEntity.Elite::new, AMBlocks.ELITE_WIRE_MILL);
	
	public static final RegistrySupplier<BlockEntityType<ElectrolyzerBlockEntity.Primitive>> PRIMITIVE_ELECTROLYZER = register("primitive_electrolyzer", ElectrolyzerBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_ELECTROLYZER);
	public static final RegistrySupplier<BlockEntityType<ElectrolyzerBlockEntity.Basic>> BASIC_ELECTROLYZER = register("basic_electrolyzer", ElectrolyzerBlockEntity.Basic::new, AMBlocks.BASIC_ELECTROLYZER);
	public static final RegistrySupplier<BlockEntityType<ElectrolyzerBlockEntity.Advanced>> ADVANCED_ELECTROLYZER = register("advanced_electrolyzer", ElectrolyzerBlockEntity.Advanced::new, AMBlocks.ADVANCED_ELECTROLYZER);
	public static final RegistrySupplier<BlockEntityType<ElectrolyzerBlockEntity.Elite>> ELITE_ELECTROLYZER = register("elite_electrolyzer", ElectrolyzerBlockEntity.Elite::new, AMBlocks.ELITE_ELECTROLYZER);
	
	public static final RegistrySupplier<BlockEntityType<RefineryBlockEntity.Primitive>> PRIMITIVE_REFINERY = register("primitive_refinery", RefineryBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_REFINERY);
	public static final RegistrySupplier<BlockEntityType<RefineryBlockEntity.Basic>> BASIC_REFINERY = register("basic_refinery", RefineryBlockEntity.Basic::new, AMBlocks.BASIC_REFINERY);
	public static final RegistrySupplier<BlockEntityType<RefineryBlockEntity.Advanced>> ADVANCED_REFINERY = register("advanced_refinery", RefineryBlockEntity.Advanced::new, AMBlocks.ADVANCED_REFINERY);
	public static final RegistrySupplier<BlockEntityType<RefineryBlockEntity.Elite>> ELITE_REFINERY = register("elite_refinery", RefineryBlockEntity.Elite::new, AMBlocks.ELITE_REFINERY);
	
	public static final RegistrySupplier<BlockEntityType<FluidMixerBlockEntity.Primitive>> PRIMITIVE_FLUID_MIXER = register("primitive_fluid_mixer", FluidMixerBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_FLUID_MIXER);
	public static final RegistrySupplier<BlockEntityType<FluidMixerBlockEntity.Basic>> BASIC_FLUID_MIXER = register("basic_fluid_mixer", FluidMixerBlockEntity.Basic::new, AMBlocks.BASIC_FLUID_MIXER);
	public static final RegistrySupplier<BlockEntityType<FluidMixerBlockEntity.Advanced>> ADVANCED_FLUID_MIXER = register("advanced_fluid_mixer", FluidMixerBlockEntity.Advanced::new, AMBlocks.ADVANCED_FLUID_MIXER);
	public static final RegistrySupplier<BlockEntityType<FluidMixerBlockEntity.Elite>> ELITE_FLUID_MIXER = register("elite_fluid_mixer", FluidMixerBlockEntity.Elite::new, AMBlocks.ELITE_FLUID_MIXER);
	
	public static final RegistrySupplier<BlockEntityType<SolidifierBlockEntity.Primitive>> PRIMITIVE_SOLIDIFIER = register("primitive_solidifier", SolidifierBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_SOLIDIFIER);
	public static final RegistrySupplier<BlockEntityType<SolidifierBlockEntity.Basic>> BASIC_SOLIDIFIER = register("basic_solidifier", SolidifierBlockEntity.Basic::new, AMBlocks.BASIC_SOLIDIFIER);
	public static final RegistrySupplier<BlockEntityType<SolidifierBlockEntity.Advanced>> ADVANCED_SOLIDIFIER = register("advanced_solidifier", SolidifierBlockEntity.Advanced::new, AMBlocks.ADVANCED_SOLIDIFIER);
	public static final RegistrySupplier<BlockEntityType<SolidifierBlockEntity.Elite>> ELITE_SOLIDIFIER = register("elite_solidifier", SolidifierBlockEntity.Elite::new, AMBlocks.ELITE_SOLIDIFIER);
	
	public static final RegistrySupplier<BlockEntityType<MelterBlockEntity.Primitive>> PRIMITIVE_MELTER = register("primitive_melter", MelterBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_MELTER);
	public static final RegistrySupplier<BlockEntityType<MelterBlockEntity.Basic>> BASIC_MELTER = register("basic_melter", MelterBlockEntity.Basic::new, AMBlocks.BASIC_MELTER);
	public static final RegistrySupplier<BlockEntityType<MelterBlockEntity.Advanced>> ADVANCED_MELTER = register("advanced_melter", MelterBlockEntity.Advanced::new, AMBlocks.ADVANCED_MELTER);
	public static final RegistrySupplier<BlockEntityType<MelterBlockEntity.Elite>> ELITE_MELTER = register("elite_melter", MelterBlockEntity.Elite::new, AMBlocks.ELITE_MELTER);
	
	public static final RegistrySupplier<BlockEntityType<CapacitorBlockEntity.Primitive>> PRIMITIVE_CAPACITOR = register("primitive_capacitor", CapacitorBlockEntity.Primitive::new, AMBlocks.PRIMITIVE_CAPACITOR);
	public static final RegistrySupplier<BlockEntityType<CapacitorBlockEntity.Basic>> BASIC_CAPACITOR = register("basic_capacitor", CapacitorBlockEntity.Basic::new, AMBlocks.BASIC_CAPACITOR);
	public static final RegistrySupplier<BlockEntityType<CapacitorBlockEntity.Advanced>> ADVANCED_CAPACITOR = register("advanced_capacitor", CapacitorBlockEntity.Advanced::new, AMBlocks.ADVANCED_CAPACITOR);
	public static final RegistrySupplier<BlockEntityType<CapacitorBlockEntity.Elite>> ELITE_CAPACITOR = register("elite_capacitor", CapacitorBlockEntity.Elite::new, AMBlocks.ELITE_CAPACITOR);
	public static final RegistrySupplier<BlockEntityType<CapacitorBlockEntity.Creative>> CREATIVE_CAPACITOR = register("creative_capacitor", CapacitorBlockEntity.Creative::new, AMBlocks.CREATIVE_CAPACITOR);
	
	public static final RegistrySupplier<BlockEntityType<FluidCollectorBlockEntity>> FLUID_EXTRACTOR = register("fluid_collector", FluidCollectorBlockEntity::new, AMBlocks.FLUID_EXTRACTOR);
	public static final RegistrySupplier<BlockEntityType<FluidPlacerBlockEntity>> FLUID_INSERTER = register("fluid_placer", FluidPlacerBlockEntity::new, AMBlocks.FLUID_INSERTER);
	
	public static final RegistrySupplier<BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER = register("block_breaker", BlockBreakerBlockEntity::new, AMBlocks.BLOCK_BREAKER);
	public static final RegistrySupplier<BlockEntityType<BlockPlacerBlockEntity>> BLOCK_PLACER = register("block_placer", BlockPlacerBlockEntity::new, AMBlocks.BLOCK_PLACER);
	
	public static final RegistrySupplier<BlockEntityType<DrainBlockEntity>> DRAIN = register("drain", DrainBlockEntity::new, AMBlocks.DRAIN);
	
	public static void init() {

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
	@SafeVarargs
	public static <B extends BlockEntity> RegistrySupplier<BlockEntityType<B>> register(String name, FabricBlockEntityTypeBuilder.Factory<B> supplier, Supplier<Block>... supportedBlocks) {
		return AMCommon.registry(Registry.BLOCK_ENTITY_TYPE_KEY).register(AMCommon.id(name), () -> FabricBlockEntityTypeBuilder.create(supplier, resolveBlocks(supportedBlocks)).build(null));
	}

	private static Block[] resolveBlocks(Supplier<Block>[] supportedBlocks) {
		Block[] blocks = new Block[supportedBlocks.length];
		for (int i = 0; i < supportedBlocks.length; i++) {
			blocks[i] = supportedBlocks[i].get();
		}
		return blocks;
	}
}
