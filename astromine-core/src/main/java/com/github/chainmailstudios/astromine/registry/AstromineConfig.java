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

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine/config")
public class AstromineConfig implements ConfigData {
	@ConfigEntry.Gui.Excluded
	public static AstromineConfig instance;

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
	public double spaceGravity = 0.01d;

	@Comment("Gravity level in Moon")
	public double moonGravity = 0.03d;

	@Comment("Gravity level in Mars")
	public double marsGravity = 0.045d;

	@Comment("Speed for the Primitive Triturator.")
	public double primitiveTrituratorSpeed = 0.5D;

	@Comment("Speed for the Basic Triturator.")
	public double basicTrituratorSpeed = 1D;

	@Comment("Speed for the Advanced Triturator.")
	public double advancedTrituratorSpeed = 2D;

	@Comment("Speed for the Elite Triturator.")
	public double eliteTrituratorSpeed = 4D;

	@Comment("Energy for the Primitive Triturator.")
	public double primitiveTrituratorEnergy = 2048D;

	@Comment("Energy for the Basic Triturator.")
	public double basicTrituratorEnergy = 16384D;

	@Comment("Energy for the Advanced Triturator.")
	public double advancedTrituratorEnergy = 32767D;

	@Comment("Energy for the Elite Triturator.")
	public double eliteTrituratorEnergy = 65535D;

	@Comment("Speed for the Primitive Solid Generator.")
	public double primitiveSolidGeneratorSpeed = 0.5D;

	@Comment("Speed for the Basic Solid Generator.")
	public double basicSolidGeneratorSpeed = 1D;

	@Comment("Speed for the Advanced Solid Generator.")
	public double advancedSolidGeneratorSpeed = 2D;

	@Comment("Speed for the Elite Solid Generator.")
	public double eliteSolidGeneratorSpeed = 4D;

	@Comment("Energy for the Primitive Solid Generator.")
	public double primitiveSolidGeneratorEnergy = 2048D;

	@Comment("Energy for the Basic Solid Generator.")
	public double basicSolidGeneratorEnergy = 16384D;

	@Comment("Energy for the Advanced Solid Generator.")
	public double advancedSolidGeneratorEnergy = 32767D;

	@Comment("Energy for the Elite Solid Generator.")
	public double eliteSolidGeneratorEnergy = 65535D;

	@Comment("Speed for the Primitive Presser.")
	public double primitivePresserSpeed = 0.5D;

	@Comment("Speed for the Basic Presser.")
	public double basicPresserSpeed = 1D;

	@Comment("Speed for the Advanced Presser.")
	public double advancedPresserSpeed = 2D;

	@Comment("Speed for the Elite Presser.")
	public double elitePresserSpeed = 4D;

	@Comment("Energy for the Primitive Presser.")
	public double primitivePresserEnergy = 2048D;

	@Comment("Energy for the Basic Presser.")
	public double basicPresserEnergy = 16384D;

	@Comment("Energy for the Advanced Presser.")
	public double advancedPresserEnergy = 32767D;

	@Comment("Energy for the Elite Presser.")
	public double elitePresserEnergy = 65535D;

	@Comment("Speed for the Primitive Wire Mill.")
	public double primitiveWireMillSpeed = 0.5D;

	@Comment("Speed for the Basic Wire Mill.")
	public double basicWireMillSpeed = 1D;

	@Comment("Speed for the Advanced Wire Mill.")
	public double advancedWireMillSpeed = 2D;

	@Comment("Speed for the Elite Wire Mill.")
	public double eliteWireMillSpeed = 4D;

	@Comment("Energy for the Primitive Wire Mill.")
	public double primitiveWireMillEnergy = 2048D;

	@Comment("Energy for the Basic Wire Mill.")
	public double basicWireMillEnergy = 16384D;

	@Comment("Energy for the Advanced Wire Mill.")
	public double advancedWireMillEnergy = 32767D;

	@Comment("Energy for the Elite Wire Mill.")
	public double eliteWireMillEnergy = 65535D;

	@Comment("Speed for the Primitive Liquid Generator.")
	public double primitiveLiquidGeneratorSpeed = 0.5D;

	@Comment("Speed for the Basic Liquid Generator.")
	public double basicLiquidGeneratorSpeed = 1D;

	@Comment("Speed for the Advanced Liquid Generator.")
	public double advancedLiquidGeneratorSpeed = 2D;

	@Comment("Speed for the Elite Liquid Generator.")
	public double eliteLiquidGeneratorSpeed = 4D;

	@Comment("Energy for the Primitive Liquid Generator.")
	public double primitiveLiquidGeneratorEnergy = 2048D;

	@Comment("Energy for the Basic Liquid Generator.")
	public double basicLiquidGeneratorEnergy = 16384D;

	@Comment("Energy for the Advanced Liquid Generator.")
	public double advancedLiquidGeneratorEnergy = 32767D;

	@Comment("Energy for the Elite Liquid Generator.")
	public double eliteLiquidGeneratorEnergy = 65535D;

	@Comment("Fluid for the Primitive Liquid Generator.")
	public long primitiveLiquidGeneratorFluid = 4L;

	@Comment("Fluid for the Basic Liquid Generator.")
	public long basicLiquidGeneratorFluid = 8L;

	@Comment("Fluid for the Advanced Liquid Generator.")
	public long advancedLiquidGeneratorFluid = 16L;

	@Comment("Fluid for the Elite Liquid Generator.")
	public long eliteLiquidGeneratorFluid = 64L;

	@Comment("Speed for the Primitive Fluid Mixer.")
	public double primitiveFluidMixerSpeed = 0.5D;

	@Comment("Speed for the Basic Fluid Mixer.")
	public double basicFluidMixerSpeed = 1D;

	@Comment("Speed for the Advanced Fluid Mixer.")
	public double advancedFluidMixerSpeed = 2D;

	@Comment("Speed for the Elite Fluid Mixer.")
	public double eliteFluidMixerSpeed = 4D;

	@Comment("Energy for the Primitive Fluid Mixer.")
	public double primitiveFluidMixerEnergy = 2048D;

	@Comment("Energy for the Basic Fluid Mixer.")
	public double basicFluidMixerEnergy = 16384D;

	@Comment("Energy for the Advanced Fluid Mixer.")
	public double advancedFluidMixerEnergy = 32767D;

	@Comment("Energy for the Elite Fluid Mixer.")
	public double eliteFluidMixerEnergy = 65535D;

	@Comment("Fluid for the Primitive Fluid Mixer.")
	public long primitiveFluidMixerFluid = 4L;

	@Comment("Fluid for the Basic Fluid Mixer.")
	public long basicFluidMixerFluid = 8L;

	@Comment("Fluid for the Advanced Fluid Mixer.")
	public long advancedFluidMixerFluid = 16L;

	@Comment("Fluid for the Elite Fluid Mixer.")
	public long eliteFluidMixerFluid = 64L;

	@Comment("Speed for the Primitive Electrolyzer.")
	public double primitiveElectrolyzerSpeed = 0.5D;

	@Comment("Speed for the Basic Electrolyzer.")
	public double basicElectrolyzerSpeed = 1D;

	@Comment("Speed for the Advanced Electrolyzer.")
	public double advancedElectrolyzerSpeed = 2D;

	@Comment("Speed for the Elite Electrolyzer.")
	public double eliteElectrolyzerSpeed = 4D;

	@Comment("Energy for the Primitive Electrolyzer.")
	public double primitiveElectrolyzerEnergy = 2048D;

	@Comment("Energy for the Basic Electrolyzer.")
	public double basicElectrolyzerEnergy = 16384D;

	@Comment("Energy for the Advanced Electrolyzer.")
	public double advancedElectrolyzerEnergy = 32767D;

	@Comment("Energy for the Elite Electrolyzer.")
	public double eliteElectrolyzerEnergy = 65535D;

	@Comment("Fluid for the Primitive Electrolyzer.")
	public long primitiveElectrolyzerFluid = 4L;

	@Comment("Fluid for the Basic Electrolyzer.")
	public long basicElectrolyzerFluid = 8L;

	@Comment("Fluid for the Advanced Electrolyzer.")
	public long advancedElectrolyzerFluid = 16L;

	@Comment("Fluid for the Elite Electrolyzer.")
	public long eliteElectrolyzerFluid = 64L;

	@Comment("Speed for the Primitive Refinery.")
	public double primitiveRefinerySpeed = 0.5D;

	@Comment("Speed for the Basic Refinery.")
	public double basicRefinerySpeed = 1D;

	@Comment("Speed for the Advanced Refinery.")
	public double advancedRefinerySpeed = 2D;

	@Comment("Speed for the Elite Refinery.")
	public double eliteRefinerySpeed = 4D;

	@Comment("Energy for the Primitive Refinery.")
	public double primitiveRefineryEnergy = 2048D;

	@Comment("Energy for the Basic Refinery.")
	public double basicRefineryEnergy = 16384D;

	@Comment("Energy for the Advanced Refinery.")
	public double advancedRefineryEnergy = 32767D;

	@Comment("Energy for the Elite Refinery.")
	public double eliteRefineryEnergy = 65535D;

	@Comment("Fluid for the Primitive Refinery.")
	public long primitiveRefineryFluid = 4L;

	@Comment("Fluid for the Basic Refinery.")
	public long basicRefineryFluid = 8L;

	@Comment("Fluid for the Advanced Refinery.")
	public long advancedRefineryFluid = 16L;

	@Comment("Fluid for the Elite Refinery.")
	public long eliteRefineryFluid = 64L;

	@Comment("Speed for the Primitive Electric Smelter.")
	public double primitiveElectricSmelterSpeed = 0.5D;

	@Comment("Speed for the Basic Electric Smelter.")
	public double basicElectricSmelterSpeed = 1D;

	@Comment("Speed for the Advanced Electric Smelter.")
	public double advancedElectricSmelterSpeed = 2D;

	@Comment("Speed for the Elite Electric Smelter.")
	public double eliteElectricSmelterSpeed = 4D;

	@Comment("Energy for the Primitive Electric Smelter.")
	public double primitiveElectricSmelterEnergy = 2048D;

	@Comment("Energy for the Basic Electric Smelter.")
	public double basicElectricSmelterEnergy = 16384D;

	@Comment("Energy for the Advanced Electric Smelter.")
	public double advancedElectricSmelterEnergy = 32767D;

	@Comment("Energy for the Elite Electric Smelter.")
	public double eliteElectricSmelterEnergy = 65535D;

	@Comment("Speed for the Primitive Alloy Smelter.")
	public double primitiveAlloySmelterSpeed = 0.5D;

	@Comment("Speed for the Basic Alloy Smelter.")
	public double basicAlloySmelterSpeed = 1D;

	@Comment("Speed for the Advanced Alloy Smelter.")
	public double advancedAlloySmelterSpeed = 2D;

	@Comment("Speed for the Elite Alloy Smelter.")
	public double eliteAlloySmelterSpeed = 4D;

	@Comment("Energy for the Primitive Alloy Smelter.")
	public double primitiveAlloySmelterEnergy = 2048D;

	@Comment("Energy for the Basic Alloy Smelter.")
	public double basicAlloySmelterEnergy = 16384D;

	@Comment("Energy for the Advanced Alloy Smelter.")
	public double advancedAlloySmelterEnergy = 32767D;

	@Comment("Energy for the Elite Alloy Smelter.")
	public double eliteAlloySmelterEnergy = 65535D;

	@Comment("Energy for the Block Placer.")
	public double blockPlacerEnergy = 16384D;

	@Comment("Energy for the Block Breaker.")
	public double blockBreakerEnergy = 16384D;

	@Comment("Energy for the Fluid Inserter.")
	public double fluidInserterEnergy = 16384D;

	@Comment("Energy for the Fluid Extractor.")
	public double fluidExtractorEnergy = 16384D;

	@Comment("Speed for the Vent.")
	public double ventSpeed = 1D;

	@Comment("Energy for the Vent.")
	public double ventEnergy = 16384D;

	@Comment("Fluid for the Vent.")
	public long ventFluid = 16L;

	@Comment("Fluid for the Primitive Tank.")
	public long primitiveTankFluid = 16L;

	@Comment("Speed for the Primitive Tank.")
	public double primitiveTankSpeed = 1D;

	@Comment("Fluid for the Basic Tank.")
	public long basicTankFluid = 32L;

	@Comment("Speed for the Basic Tank.")
	public double basicTankSpeed = 2D;

	@Comment("Fluid for the Advanced Tank.")
	public long advancedTankFluid = 128L;

	@Comment("Speed for the Advanced Tank.")
	public double advancedTankSpeed = 4D;

	@Comment("Fluid for the Elite Tank.")
	public long eliteTankFluid = 256L;

	@Comment("Speed for the Elite Tank.")
	public double eliteTankSpeed = 16D;

	@Comment("Speed for the Primitive Capacitor.")
	public double primitiveCapacitorSpeed = 0.5D;

	@Comment("Speed for the Basic Capacitor.")
	public double basicCapacitorSpeed = 1D;

	@Comment("Speed for the Advanced Capacitor.")
	public double advancedCapacitorSpeed = 2D;

	@Comment("Speed for the Elite Capacitor.")
	public double eliteCapacitorSpeed = 4D;

	@Comment("Energy for the Primitive Capacitor.")
	public double primitiveCapacitorEnergy = 16384D;

	@Comment("Energy for the Basic Capacitor.")
	public double basicCapacitorEnergy = 32767D;

	@Comment("Energy for the Advanced Capacitor.")
	public double advancedCapacitorEnergy = 65535D;

	@Comment("Energy for the Elite Capacitor.")
	public double eliteCapacitorEnergy = 131071D;

	@Comment("Energy for the Block Placer actions.")
	public double blockPlacerEnergyConsumed = 1024D;

	@Comment("Energy for the Block Breaker actions.")
	public double blockBreakerEnergyConsumed = 1024D;

	@Comment("Energy for the Fluid Inserter actions.")
	public double fluidInserterEnergyConsumed = 1024D;

	@Comment("Energy for the Fluid Extractor actions.")
	public double fluidExtractorEnergyConsumed = 1024D;

	@Comment("Energy for the Vent actions.")
	public double ventEnergyConsumed = 1024D;

	@Comment("Delay for the Block Placer actions (smaller is faster).")
	public long blockPlacerSpeed = 40L;

	@Comment("Delay for the Block Breaker actions (smaller is faster).")
	public long blockBreakerSpeed = 40L;

	@Comment("Delay for the Fluid Inserter actions (smaller is faster).")
	public long fluidInserterSpeed = 40L;

	@Comment("Delay for the Fluid Extractor actions (smaller is faster).")
	public long fluidExtractorSpeed = 40L;

	@Comment("Energy for the Gravity Gauntlet.")
	public double gravityGauntletEnergy = 16384D;

	@Comment("Energy for the Gravity Gauntlet actions.")
	public double gravityGauntletConsumed = 1024D;

	@Comment("Energy for the Drill actions.")
	public double drillConsumed = 6D;

	@Comment("Multiplier for the Drill entity hit actions.")
	public double drillEntityHitMultiplier = 2D;

	@Comment("Delay for the Fire Extinguisher sneaking actions.")
	public int fireExtinguisherSneakingDelay = 2;

	@Comment("Delay for the Fire Extinguisher standing actions.")
	public int fireExtinguisherStandingDelay = 10;

	@Comment("Tick rate for gas movement in the atmosphere. This is inversely correlated to gas movement speed - higher means slower. Lower numbers are also less performant, so choose wisely.")
	public int gasTickRate = 10;

	@Comment("Numerator for the gas decay rate fraction. It will decay gases by this fraction every gas tick.")
	public int gasDecayNumerator = 5;

	@Comment("Denominator for the gas decay rate fraction. It will decay gases by this fraction every gas tick.")
	public int gasDecayDenominator = 1024;

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

	@Comment("Energy for the Basic Battery.")
	public double basicBatteryEnergy = 9000;

	@Comment("Energy for the Advanced Battery.")
	public double advancedBatteryEnergy = 24000;

	@Comment("Energy for the Elite Battery.")
	public double eliteBatteryEnergy = 64000;

	@Comment("Energy for the Basic Drill.")
	public double basicDrillEnergy = 90000;

	@Comment("Energy for the Advanced Drill.")
	public double advancedDrillEnergy = 240000;

	@Comment("Energy for the Elite Drill.")
	public double eliteDrillEnergy = 640000;

	@Comment("Fluid for the Gas Canister.")
	public long gasCanisterFluid = 8L;

	@Comment("Fluid for the Pressurized Gas Canister.")
	public long pressurizedGasCanisterFluid = 32L;

	@Comment("Energy rate for the Primitive Energy Cable.")
	public double primitiveEnergyCableEnergy = 64D;

	@Comment("Energy for the Basic Energy Cable.")
	public double basicEnergyCableEnergy = 256D;

	@Comment("Energy for the Advanced Energy Cable.")
	public double advancedEnergyCableEnergy = 1024D;

	@Comment("Energy for the Elite Energy Cable.")
	public double eliteEnergyCableEnergy = 4096D;

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
	public long spaceSuitFluid = 64L;

	@Comment("Threshold for Asteroid Ores.")
	public int asteroidOreThreshold = 6;

	@Comment("Whether AK9 should asphyxiate.")
	public boolean asphyxiateAK9 = true;

	@Comment("Fluid numerator for the Fluid Cable and inter-machine transfer.")
	public long fluidTransferNumerator = 1;

	@Comment("Fluid denominator for the Fluid Cable and inter-machine transfer.")
	public long fluidTransferDenominator = 5;

	public static AstromineConfig get() {
		if (instance == null) {
			try {
				AutoConfig.register(AstromineConfig.class, JanksonConfigSerializer::new);
				try {
					AutoConfig.getConfigHolder(AstromineConfig.class).save();
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
				instance = AutoConfig.getConfigHolder(AstromineConfig.class).getConfig();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
				instance = new AstromineConfig();
			}
		}

		return instance;
	}
}
