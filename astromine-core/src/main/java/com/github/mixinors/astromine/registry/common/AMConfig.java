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

import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine/config")
public class AMConfig implements ConfigData {
	@ConfigEntry.Gui.Excluded
	public static AMConfig INSTANCE;

	@Comment("Whether Nuclear Warheads are enabled.")
	public boolean nuclearWarheadEnabled = true;

	@Comment("Whether to attempt to migrate old, broken data to new data. Usage recommended when first loading old chunks after an update.")
	public boolean compatibilityMode = true;

	@Comment("Y level in the Overworld to get to Space.")
	public int spaceTravelYLevel = 1024;

	@Comment("Y level to spawn at when travelling to Space.")
	public int spaceSpawnYLevel = 32;

	@Comment("Y level in Space to get back to the Overworld.")
	public int overworldTravelYLevel = -58;

	@Comment("Y level to spawn at when returning to the Overworld.")
	public int overworldSpawnYLevel = 992;

	@Comment("Gravity level in Space")
	public double spaceGravity = 0.01D;



	@Comment("Energy for the Primitive Battery.")
	public double primitiveBatteryEnergy = 8000;

	@Comment("Energy for the Basic Battery.")
	public double basicBatteryEnergy = 16000;

	@Comment("Energy for the Advanced Battery.")
	public double advancedBatteryEnergy = 32000;

	@Comment("Energy for the Elite Battery.")
	public double eliteBatteryEnergy = 64000;

	@Comment("Fluid for the Large Portable Tank.")
	public long largePortableTankFluid = FluidVolume.BUCKET * 16L;

	@Comment("Fluid for the Portable Tank.")
	public long portableTankFluid = FluidVolume.BUCKET * 8L;

	@Comment("Fluid for the Primitive Tank.")
	public long primitiveTankFluid = largePortableTankFluid * 4L;

	@Comment("Speed for the Primitive Tank.")
	public double primitiveTankSpeed = 1.0D;

	@Comment("Fluid for the Basic Tank.")
	public long basicTankFluid = largePortableTankFluid * 4L;

	@Comment("Speed for the Basic Tank.")
	public double basicTankSpeed = 2.0D;

	@Comment("Fluid for the Advanced Tank.")
	public long advancedTankFluid = largePortableTankFluid * 4L;

	@Comment("Speed for the Advanced Tank.")
	public double advancedTankSpeed = 4.0D;

	@Comment("Fluid for the Elite Tank.")
	public long eliteTankFluid = largePortableTankFluid * 4L;

	@Comment("Speed for the Elite Tank.")
	public double eliteTankSpeed = 16.0D;

	@Comment("Energy for the Primitive Battery Pack.")
	public double primitiveBatteryPackEnergy = primitiveBatteryEnergy * 6.0D;

	@Comment("Energy for the Basic Battery Pack.")
	public double basicBatteryPackEnergy = basicBatteryEnergy * 6.0D;

	@Comment("Energy for the Advanced Battery Pack.")
	public double advancedBatteryPackEnergy = advancedBatteryEnergy * 6.0D;

	@Comment("Energy for the Elite Battery Pack.")
	public double eliteBatteryPackEnergy = eliteBatteryEnergy * 6.0D;

	@Comment("Speed for the Primitive Triturator.")
	public double primitiveTrituratorSpeed = 0.5D;

	@Comment("Speed for the Basic Triturator.")
	public double basicTrituratorSpeed = 1.0D;

	@Comment("Speed for the Advanced Triturator.")
	public double advancedTrituratorSpeed = 2.0D;

	@Comment("Speed for the Elite Triturator.")
	public double eliteTrituratorSpeed = 4.0D;

	@Comment("Energy for the Primitive Triturator.")
	public double primitiveTrituratorEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Triturator.")
	public double basicTrituratorEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Triturator.")
	public double advancedTrituratorEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Triturator.")
	public double eliteTrituratorEnergy = eliteBatteryPackEnergy;

	@Comment("Speed for the Primitive Solid Generator.")
	public double primitiveSolidGeneratorSpeed = 0.5D;

	@Comment("Speed for the Basic Solid Generator.")
	public double basicSolidGeneratorSpeed = 1.0D;

	@Comment("Speed for the Advanced Solid Generator.")
	public double advancedSolidGeneratorSpeed = 2.0D;

	@Comment("Speed for the Elite Solid Generator.")
	public double eliteSolidGeneratorSpeed = 4.0D;

	@Comment("Energy for the Primitive Solid Generator.")
	public double primitiveSolidGeneratorEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Solid Generator.")
	public double basicSolidGeneratorEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Solid Generator.")
	public double advancedSolidGeneratorEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Solid Generator.")
	public double eliteSolidGeneratorEnergy = eliteBatteryPackEnergy;

	@Comment("Speed for the Primitive Press.")
	public double primitivePressSpeed = 0.5D;

	@Comment("Speed for the Basic Press.")
	public double basicPressSpeed = 1.0D;

	@Comment("Speed for the Advanced Press.")
	public double advancedPressSpeed = 2.0D;

	@Comment("Speed for the Elite Press.")
	public double elitePressSpeed = 4.0D;

	@Comment("Energy for the Primitive Press.")
	public double primitivePressEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Press.")
	public double basicPressEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Press.")
	public double advancedPressEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Press.")
	public double elitePressEnergy = eliteBatteryPackEnergy;
	
	@Comment("Speed for the Primitive Wire Mill.")
	public double primitiveWireMillSpeed = 0.5D;

	@Comment("Speed for the Basic Wire Mill.")
	public double basicWireMillSpeed = 1.0D;

	@Comment("Speed for the Advanced Wire Mill.")
	public double advancedWireMillSpeed = 2.0D;

	@Comment("Speed for the Elite Wire Mill.")
	public double eliteWireMillSpeed = 4.0D;

	@Comment("Energy for the Primitive Wire Mill.")
	public double primitiveWireMillEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Wire Mill.")
	public double basicWireMillEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Wire Mill.")
	public double advancedWireMillEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Wire Mill.")
	public double eliteWireMillEnergy = eliteBatteryPackEnergy;

	@Comment("Speed for the Primitive Fluid Generator.")
	public double primitiveFluidGeneratorSpeed = 0.5D;

	@Comment("Speed for the Basic Fluid Generator.")
	public double basicFluidGeneratorSpeed = 1.0D;

	@Comment("Speed for the Advanced Fluid Generator.")
	public double advancedFluidGeneratorSpeed = 2.0D;

	@Comment("Speed for the Elite Fluid Generator.")
	public double eliteFluidGeneratorSpeed = 4.0D;

	@Comment("Energy for the Primitive Fluid Generator.")
	public double primitiveFluidGeneratorEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Fluid Generator.")
	public double basicFluidGeneratorEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Fluid Generator.")
	public double advancedFluidGeneratorEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Fluid Generator.")
	public double eliteFluidGeneratorEnergy = eliteBatteryPackEnergy;

	@Comment("Fluid for the Primitive Fluid Generator.")
	public long primitiveFluidGeneratorFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Basic Fluid Generator.")
	public long basicFluidGeneratorFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Advanced Fluid Generator.")
	public long advancedFluidGeneratorFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Elite Fluid Generator.")
	public long eliteFluidGeneratorFluid = largePortableTankFluid * 2L;

	@Comment("Speed for the Primitive Fluid Mixer.")
	public double primitiveFluidMixerSpeed = 0.5D;

	@Comment("Speed for the Basic Fluid Mixer.")
	public double basicFluidMixerSpeed = 1.0D;

	@Comment("Speed for the Advanced Fluid Mixer.")
	public double advancedFluidMixerSpeed = 2.0D;

	@Comment("Speed for the Elite Fluid Mixer.")
	public double eliteFluidMixerSpeed = 4.0D;

	@Comment("Energy for the Primitive Fluid Mixer.")
	public double primitiveFluidMixerEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Fluid Mixer.")
	public double basicFluidMixerEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Fluid Mixer.")
	public double advancedFluidMixerEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Fluid Mixer.")
	public double eliteFluidMixerEnergy = eliteBatteryPackEnergy;

	@Comment("Fluid for the Primitive Fluid Mixer.")
	public long primitiveFluidMixerFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Basic Fluid Mixer.")
	public long basicFluidMixerFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Advanced Fluid Mixer.")
	public long advancedFluidMixerFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Elite Fluid Mixer.")
	public long eliteFluidMixerFluid = largePortableTankFluid * 2L;

	@Comment("Speed for the Primitive Electrolyzer.")
	public double primitiveElectrolyzerSpeed = 0.5D;

	@Comment("Speed for the Basic Electrolyzer.")
	public double basicElectrolyzerSpeed = 1.0D;

	@Comment("Speed for the Advanced Electrolyzer.")
	public double advancedElectrolyzerSpeed = 2.0D;

	@Comment("Speed for the Elite Electrolyzer.")
	public double eliteElectrolyzerSpeed = 4.0D;

	@Comment("Energy for the Primitive Electrolyzer.")
	public double primitiveElectrolyzerEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Electrolyzer.")
	public double basicElectrolyzerEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Electrolyzer.")
	public double advancedElectrolyzerEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Electrolyzer.")
	public double eliteElectrolyzerEnergy = eliteBatteryPackEnergy;

	@Comment("Fluid for the Primitive Electrolyzer.")
	public long primitiveElectrolyzerFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Basic Electrolyzer.")
	public long basicElectrolyzerFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Advanced Electrolyzer.")
	public long advancedElectrolyzerFluid = largePortableTankFluid * 2L;

	@Comment("Fluid for the Elite Electrolyzer.")
	public long eliteElectrolyzerFluid = largePortableTankFluid * 2L;

	@Comment("Speed for the Primitive Refinery.")
	public double primitiveRefinerySpeed = 0.5D;

	@Comment("Speed for the Basic Refinery.")
	public double basicRefinerySpeed = 1.0D;

	@Comment("Speed for the Advanced Refinery.")
	public double advancedRefinerySpeed = 2.0D;

	@Comment("Speed for the Elite Refinery.")
	public double eliteRefinerySpeed = 4.0D;

	@Comment("Energy for the Primitive Refinery.")
	public double primitiveRefineryEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Refinery.")
	public double basicRefineryEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Refinery.")
	public double advancedRefineryEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Refinery.")
	public double eliteRefineryEnergy = eliteBatteryPackEnergy;

	@Comment("Fluid for the Primitive Refinery.")
	public long primitiveRefineryFluid = primitiveTankFluid * 2L;

	@Comment("Fluid for the Basic Refinery.")
	public long basicRefineryFluid = basicTankFluid * 2L;

	@Comment("Fluid for the Advanced Refinery.")
	public long advancedRefineryFluid = advancedTankFluid * 2L;

	@Comment("Fluid for the Elite Refinery.")
	public long eliteRefineryFluid = eliteTankFluid * 2L;

	@Comment("Speed for the Primitive Electric Furnace.")
	public double primitiveElectricFurnaceSpeed = 0.5D;

	@Comment("Speed for the Basic Electric Furnace.")
	public double basicElectricFurnaceSpeed = 1.0D;

	@Comment("Speed for the Advanced Electric Furnace.")
	public double advancedElectricFurnaceSpeed = 2.0D;

	@Comment("Speed for the Elite Electric Furnace.")
	public double eliteElectricFurnaceSpeed = 4.0D;

	@Comment("Energy for the Primitive Electric Furnace.")
	public double primitiveElectricFurnaceEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Electric Furnace.")
	public double basicElectricFurnaceEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Electric Furnace.")
	public double advancedElectricFurnaceEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Electric Furnace.")
	public double eliteElectricFurnaceEnergy = eliteBatteryPackEnergy;

	@Comment("Speed for the Primitive Alloy Smelter.")
	public double primitiveAlloySmelterSpeed = 0.5D;

	@Comment("Speed for the Basic Alloy Smelter.")
	public double basicAlloySmelterSpeed = 1.0D;

	@Comment("Speed for the Advanced Alloy Smelter.")
	public double advancedAlloySmelterSpeed = 2.0D;

	@Comment("Speed for the Elite Alloy Smelter.")
	public double eliteAlloySmelterSpeed = 4.0D;

	@Comment("Energy for the Primitive Alloy Smelter.")
	public double primitiveAlloySmelterEnergy = primitiveElectricFurnaceEnergy* 2.0D + primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Alloy Smelter.")
	public double basicAlloySmelterEnergy = basicElectricFurnaceEnergy* 2.0D + basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Alloy Smelter.")
	public double advancedAlloySmelterEnergy = advancedElectricFurnaceEnergy* 2.0D + advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Alloy Smelter.")
	public double eliteAlloySmelterEnergy = eliteElectricFurnaceEnergy* 2.0D + eliteBatteryPackEnergy;

	@Comment("Energy for the Block Placer.")
	public double blockPlacerEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Block Breaker.")
	public double blockBreakerEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Fluid Placer.")
	public double fluidPlacerEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Fluid Collector.")
	public double fluidCollectorEnergy = primitiveBatteryPackEnergy;

	@Comment("Speed for the Vent.")
	public double ventSpeed = 1.0D;

	@Comment("Energy for the Vent.")
	public double ventEnergy = primitiveBatteryPackEnergy;

	@Comment("Fluid for the Vent.")
	public long ventFluid = FluidVolume.BUCKET * 16L;

	@Comment("Speed for the Primitive Capacitor.")
	public double primitiveCapacitorSpeed = 0.5D;

	@Comment("Speed for the Basic Capacitor.")
	public double basicCapacitorSpeed = 1.0D;

	@Comment("Speed for the Advanced Capacitor.")
	public double advancedCapacitorSpeed = 2.0D;

	@Comment("Speed for the Elite Capacitor.")
	public double eliteCapacitorSpeed = 4.0D;

	@Comment("Energy for the Primitive Capacitor.")
	public double primitiveCapacitorEnergy = primitiveBatteryPackEnergy * 4.0D;

	@Comment("Energy for the Basic Capacitor.")
	public double basicCapacitorEnergy = basicBatteryPackEnergy * 4.0D;

	@Comment("Energy for the Advanced Capacitor.")
	public double advancedCapacitorEnergy = advancedBatteryPackEnergy * 4.0D;

	@Comment("Energy for the Elite Capacitor.")
	public double eliteCapacitorEnergy = eliteBatteryPackEnergy * 4.0D;

	@Comment("Energy for the Block Placer actions.")
	public double blockPlacerEnergyConsumed = 1024.0D;

	@Comment("Energy for the Block Breaker actions.")
	public double blockBreakerEnergyConsumed = 1024.0D;

	@Comment("Energy for the Fluid Placer actions.")
	public double fluidPlacerEnergyConsumed = 1024.0D;

	@Comment("Energy for the Fluid Collector actions.")
	public double fluidCollectorEnergyConsumed = 1024.0D;

	@Comment("Energy for the Vent actions.")
	public double ventEnergyConsumed = 1024.0D;

	@Comment("Delay for the Block Placer actions (smaller is faster).")
	public long blockPlacerSpeed = 40L;

	@Comment("Delay for the Block Breaker actions (smaller is faster).")
	public long blockBreakerSpeed = 40L;

	@Comment("Delay for the Fluid Placer actions (smaller is faster).")
	public long fluidPlacerSpeed = 40L;

	@Comment("Delay for the Fluid Collector actions (smaller is faster).")
	public long fluidCollectorSpeed = 40L;

	@Comment("Energy for the Gravity Gauntlet.")
	public double gravityGauntletEnergy = advancedBatteryEnergy;

	@Comment("Energy for the Gravity Gauntlet actions.")
	public double gravityGauntletConsumed = 512.0D;

	@Comment("Energy for the Drill actions.")
	public double drillConsumed = 6.0D;

	@Comment("Multiplier for the Drill entity hit actions.")
	public double drillEntityHitMultiplier = 2.0D;

	@Comment("Delay for the Fire Extinguisher sneaking actions.")
	public int fireExtinguisherSneakingDelay = 2;

	@Comment("Delay for the Fire Extinguisher standing actions.")
	public int fireExtinguisherStandingDelay = 10;

	@Comment("Tick rate for gas movement in the atmosphere. This is inversely correlated to gas movement speed - higher means slower. Lower numbers are also less performant, so choose wisely.")
	public int gasTickRate = 10;

	@Comment("Amount for the gas decay rate fraction. It will decay gases by this fraction every gas tick.")
	public long gasDecayAmount = 40L;

	@Comment("Noise threshold for Asteroid generation.")
	public float asteroidNoiseThreshold = 0.545F;

	@Comment("Threshold for Crude Oil wells.")
	public int crudeOilThreshold = 2000;

	@Comment("Whether to enable Crude Oil well generation.")
	public boolean crudeOilWells = true;

	@Comment("Whether to enable Crude Oil well generation in the Ocean biome.")
	public boolean oceanicCrudeOilWells = true;

	@Comment("Whether to enable Crude Oil well generation in the Desert biome.")
	public boolean desertCrudeOilWells = true;

	@Comment("Whether to enable Meteor generation in all dimensions, except The Nether and The End.")
	public boolean meteorGeneration = true;

	@Comment("Whether to enable Meteor generation in The Nether.")
	public boolean netherMeteorGeneration = true;

	@Comment("Whether to enable Meteor generation in The End.")
	public boolean endMeteorGeneration = true;

	@Comment("Energy for the Primitive Drill.")
	public double primitiveDrillEnergy = primitiveBatteryPackEnergy * 2;

	@Comment("Energy for the Basic Drill.")
	public double basicDrillEnergy = basicBatteryPackEnergy * 2;

	@Comment("Energy for the Advanced Drill.")
	public double advancedDrillEnergy = advancedBatteryPackEnergy * 2;

	@Comment("Energy for the Elite Drill.")
	public double eliteDrillEnergy = eliteBatteryPackEnergy * 2;

	@Comment("Energy rate for the Primitive Energy Cable.")
	public double primitiveEnergyCableEnergy = 256.0D;

	@Comment("Energy for the Basic Energy Cable.")
	public double basicEnergyCableEnergy = 512.0D;

	@Comment("Energy for the Advanced Energy Cable.")
	public double advancedEnergyCableEnergy = 1024.0D;

	@Comment("Energy for the Elite Energy Cable.")
	public double eliteEnergyCableEnergy = 4096.0D;

	@Comment("Delay for the Inserter actions (smaller is faster).")
	public int inserterSpeed = 16;

	@Comment("Delay for the Fast Inserter actions (smaller is faster).")
	public int fastInserterSpeed = 8;

	@Comment("Delay for the Basic Conveyor actions (smaller is faster).")
	public int basicConveyorSpeed = 16;

	@Comment("Delay for the Advanced Conveyor actions (smaller is faster).")
	public int advancedConveyorSpeed = 8;

	@Comment("Delay for the Elite Conveyor actions (smaller is faster).")
	public int eliteConveyorSpeed = 8;

	@Comment("Fluid for the Space Suit.")
	public long spaceSuitFluid = portableTankFluid;

	@Comment("Threshold for Asteroid Ores.")
	public int asteroidOreThreshold = 2;

	@Comment("Whether AK9 should asphyxiate.")
	public boolean asphyxiateAK9 = true;

	@Comment("Fluid amount for the Fluid Pipe and inter-machine transfer.")
	public long fluidTransfer = FluidVolume.BUCKET * 4;

	@Comment("Fluid for the Primitive Melter.")
	public long primitiveMelterFluid = largePortableTankFluid;

	@Comment("Speed for the Primitive Melter.")
	public double primitiveMelterSpeed = 0.5D;

	@Comment("Fluid for the Basic Melter.")
	public long basicMelterFluid = largePortableTankFluid;

	@Comment("Speed for the Basic Melter.")
	public double basicMelterSpeed = 1.0D;

	@Comment("Fluid for the Advanced Melter.")
	public long advancedMelterFluid = largePortableTankFluid;

	@Comment("Speed for the Advanced Melter.")
	public double advancedMelterSpeed = 2.0D;

	@Comment("Fluid for the Elite Melter.")
	public long eliteMelterFluid = largePortableTankFluid;

	@Comment("Speed for the Elite Melter.")
	public double eliteMelterSpeed = 4.0D;

	@Comment("Energy for the Primitive Melter.")
	public double primitiveMelterEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Melter.")
	public double basicMelterEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Melter.")
	public double advancedMelterEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Melter.")
	public double eliteMelterEnergy = eliteBatteryPackEnergy;

	@Comment("Fluid for the Primitive Solidifier.")
	public long primitiveSolidifierFluid = largePortableTankFluid;

	@Comment("Speed for the Primitive Solidifier.")
	public double primitiveSolidifierSpeed = 0.5D;

	@Comment("Fluid for the Basic Solidifier.")
	public long basicSolidifierFluid = largePortableTankFluid;

	@Comment("Speed for the Basic Solidifier.")
	public double basicSolidifierSpeed = 1.0D;

	@Comment("Fluid for the Advanced Solidifier.")
	public long advancedSolidifierFluid = largePortableTankFluid;

	@Comment("Speed for the Advanced Solidifier.")
	public double advancedSolidifierSpeed = 2.0D;

	@Comment("Fluid for the Elite Solidifier.")
	public long eliteSolidifierFluid = largePortableTankFluid;

	@Comment("Speed for the Elite Solidifier.")
	public double eliteSolidifierSpeed = 4.0D;

	@Comment("Energy for the Primitive Solidifier.")
	public double primitiveSolidifierEnergy = primitiveBatteryPackEnergy;

	@Comment("Energy for the Basic Solidifier.")
	public double basicSolidifierEnergy = basicBatteryPackEnergy;

	@Comment("Energy for the Advanced Solidifier.")
	public double advancedSolidifierEnergy = advancedBatteryPackEnergy;

	@Comment("Energy for the Elite Solidifier.")
	public double eliteSolidifierEnergy = eliteBatteryPackEnergy;

	@Comment("Default gravity for non-Astromine dimensions.")
	public double defaultGravity = 0.08D;

	@Comment("Chance for Piglins to realize if you try to trick them. (1 in x)")
	public int piglinAngerChance = 5;

	@Comment("Stack size transferred by Inserters.")
	public int inserterStackSize = 8;
	
	@Comment("Whether generation of Copper Ore in the Overworld is enabled.")
	public boolean overworldCopperOre = true;
	
	@Comment("Whether generation of Tin Ore in the Overworld is enabled.")
	public boolean overworldTinOre = true;
	
	@Comment("Whether generation of Silver Ore in the Overworld is enabled.")
	public boolean overworldSilverOre = true;
	
	@Comment("Whether generation of Lead Ore in the Overworld is enabled.")
	public boolean overworldLeadOre = true;
	
	@Comment("Minimum range of Asteroid Coal Ore weight.")
	public int asteroidCoalOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Coal Ore weight.")
	public int asteroidCoalOreMaximumRange = 100;
	
	@Comment("Minimum size of Asteroid Coal Ore veins.")
	public int asteroidCoalOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Coal Ore veins.")
	public int asteroidCoalOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Iron Ore weight.")
	public int asteroidIronOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Iron Ore weight.")
	public int asteroidIronOreMaximumRange = 100;
	
	@Comment("Minimum size of Asteroid Iron Ore veins.")
	public int asteroidIronOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Iron Ore veins.")
	public int asteroidIronOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Gold Ore weight.")
	public int asteroidGoldOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Gold Ore weight.")
	public int asteroidGoldOreMaximumRange = 100;
	
	@Comment("Minimum size of Asteroid Gold Ore veins.")
	public int asteroidGoldOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Gold Ore veins.")
	public int asteroidGoldOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Copper Ore weight.")
	public int asteroidCopperOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Copper Ore weight.")
	public int asteroidCopperOreMaximumRange = 100;
	
	@Comment("Minimum size of Asteroid Copper Ore veins.")
	public int asteroidCopperOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Copper Ore veins.")
	public int asteroidCopperOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Tin Ore weight.")
	public int asteroidTinOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Tin Ore weight.")
	public int asteroidTinOreMaximumRange = 100;
	
	@Comment("Minimum size of Asteroid Tin Ore veins.")
	public int asteroidTinOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Tin Ore veins.")
	public int asteroidTinOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Silver Ore weight.")
	public int asteroidSilverOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Silver Ore weight.")
	public int asteroidSilverOreMaximumRange = 100;
	
	@Comment("Minimum size of Asteroid Silver Ore veins.")
	public int asteroidSilverOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Silver Ore veins.")
	public int asteroidSilverOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Lead Ore weight.")
	public int asteroidLeadOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Lead Ore weight.")
	public int asteroidLeadOreMaximumRange = 100;
	
	@Comment("Minimum size of Asteroid Lead Ore veins.")
	public int asteroidLeadOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Lead Ore veins.")
	public int asteroidLeadOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Redstone Ore weight.")
	public int asteroidRedstoneOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Redstone Ore weight.")
	public int asteroidRedstoneOreMaximumRange = 40;
	
	@Comment("Minimum size of Asteroid Redstone Ore veins.")
	public int asteroidRedstoneOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Redstone Ore veins.")
	public int asteroidRedstoneOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Lapis Ore weight.")
	public int asteroidLapisOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Lapis Ore weight.")
	public int asteroidLapisOreMaximumRange = 40;
	
	@Comment("Minimum size of Asteroid Lapis Ore veins.")
	public int asteroidLapisOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Lapis Ore veins.")
	public int asteroidLapisOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Diamond Ore weight.")
	public int asteroidDiamondOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Diamond Ore weight.")
	public int asteroidDiamondOreMaximumRange = 50;
	
	@Comment("Minimum size of Asteroid Diamond Ore veins.")
	public int asteroidDiamondOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Diamond Ore veins.")
	public int asteroidDiamondOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Emerald Ore weight.")
	public int asteroidEmeraldOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Emerald Ore weight.")
	public int asteroidEmeraldOreMaximumRange = 50;
	
	@Comment("Minimum size of Asteroid Emerald Ore veins.")
	public int asteroidEmeraldOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Emerald Ore veins.")
	public int asteroidEmeraldOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Metite Ore weight.")
	public int asteroidMetiteOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Metite Ore weight.")
	public int asteroidMetiteOreMaximumRange = 50;
	
	@Comment("Minimum size of Asteroid Metite Ore veins.")
	public int asteroidMetiteOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Metite Ore veins.")
	public int asteroidMetiteOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Asterite Ore weight.")
	public int asteroidAsteriteOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Asterite Ore weight.")
	public int asteroidAsteriteOreMaximumRange = 40;
	
	@Comment("Minimum size of Asteroid Asterite Ore veins.")
	public int asteroidAsteriteOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Asterite Ore veins.")
	public int asteroidAsteriteOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Stellum Ore weight.")
	public int asteroidStellumOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Stellum Ore weight.")
	public int asteroidStellumOreMaximumRange = 30;
	
	@Comment("Minimum size of Asteroid Stellum Ore veins.")
	public int asteroidStellumOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Stellum Ore veins.")
	public int asteroidStellumOreMaximumSize = 48;
	
	@Comment("Minimum range of Asteroid Galaxium Ore weight.")
	public int asteroidGalaxiumOreMinimumRange = 0;
	
	@Comment("Maximum range of Asteroid Galaxium Ore weight.")
	public int asteroidGalaxiumOreMaximumRange = 20;
	
	@Comment("Minimum size of Asteroid Galaxium Ore veins.")
	public int asteroidGalaxiumOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Galaxium Ore veins.")
	public int asteroidGalaxiumOreMaximumSize = 48;

	public static AMConfig get() {
		if (INSTANCE == null) {
			try {
				AutoConfig.register(AMConfig.class, JanksonConfigSerializer::new);
				try {
					AutoConfig.getConfigHolder(AMConfig.class).save();
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
				INSTANCE = AutoConfig.getConfigHolder(AMConfig.class).getConfig();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
				INSTANCE = new AMConfig();
			}
		}

		return INSTANCE;
	}
}
