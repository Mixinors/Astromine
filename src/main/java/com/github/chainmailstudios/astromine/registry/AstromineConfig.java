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
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine/config")
public class AstromineConfig implements ConfigData {
	public static final AstromineConfig DEFAULT = new AstromineConfig();

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

	@Comment("Whether generation of Copper Ore in the Overworld is enabled.")
	public boolean overworldCopperOre = true;

	@Comment("Bottom offset of Overworld Copper ore.")
	public int overworldCopperOreBottomOffset = 0;

	@Comment("Top offset of Overworld Copper ore.")
	public int overworldCopperOreTopOffset = 0;

	@Comment("Maximum layer of Overworld Copper ore.")
	public int overworldCopperOreMaximumLayer = 64;

	@Comment("Maximum count of Overworld Copper ore veins per chunk.")
	public int overworldCopperOreMaximumVeins = 20;

	@Comment("Maximum count of Overworld Copper ore blocks per vein.")
	public int overworldCopperOreMaximumBlocks = 14;

	@Comment("Whether generation of Tin Ore in the Overworld is enabled.")
	public boolean overworldTinOre = true;

	@Comment("Bottom offset of Overworld Tin Ore.")
	public int overworldTinOreBottomOffset = 0;

	@Comment("Top offset of Overworld Tin Ore.")
	public int overworldTinOreTopOffset = 0;

	@Comment("Maximum layer of Overworld Tin Ore.")
	public int overworldTinOreMaximumLayer = 64;

	@Comment("Maximum count of Overworld Tin Ore veins per chunk.")
	public int overworldTinOreMaximumVeins = 20;

	@Comment("Maximum count of Overworld Tin Ore blocks per vein.")
	public int overworldTinOreMaximumBlocks = 11;
	
	@Comment("Minimum range of Asteroid Coal Ore weight.")
	public int asteroidCoalOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Coal Ore weight.")
	public int asteroidCoalOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Coal Ore veins.")
	public int asteroidCoalOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Coal Ore veins.")
	public int asteroidCoalOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Iron Ore weight.")
	public int asteroidIronOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Iron Ore weight.")
	public int asteroidIronOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Iron Ore veins.")
	public int asteroidIronOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Iron Ore veins.")
	public int asteroidIronOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Gold Ore weight.")
	public int asteroidGoldOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Gold Ore weight.")
	public int asteroidGoldOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Gold Ore veins.")
	public int asteroidGoldOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Gold Ore veins.")
	public int asteroidGoldOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Copper Ore weight.")
	public int asteroidCopperOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Copper Ore weight.")
	public int asteroidCopperOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Copper Ore veins.")
	public int asteroidCopperOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Copper Ore veins.")
	public int asteroidCopperOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Tin Ore weight.")
	public int asteroidTinOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Tin Ore weight.")
	public int asteroidTinOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Tin Ore veins.")
	public int asteroidTinOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Tin Ore veins.")
	public int asteroidTinOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Redstone Ore weight.")
	public int asteroidRedstoneOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Redstone Ore weight.")
	public int asteroidRedstoneOreMaximumRange = 32;

	@Comment("Minimum size of Asteroid Redstone Ore veins.")
	public int asteroidRedstoneOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Redstone Ore veins.")
	public int asteroidRedstoneOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Lapis Ore weight.")
	public int asteroidLapisOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Lapis Ore weight.")
	public int asteroidLapisOreMaximumRange = 32;

	@Comment("Minimum size of Asteroid Lapis Ore veins.")
	public int asteroidLapisOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Lapis Ore veins.")
	public int asteroidLapisOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Diamond Ore weight.")
	public int asteroidDiamondOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Diamond Ore weight.")
	public int asteroidDiamondOreMaximumRange = 16;

	@Comment("Minimum size of Asteroid Diamond Ore veins.")
	public int asteroidDiamondOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Diamond Ore veins.")
	public int asteroidDiamondOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Emerald Ore weight.")
	public int asteroidEmeraldOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Emerald Ore weight.")
	public int asteroidEmeraldOreMaximumRange = 16;

	@Comment("Minimum size of Asteroid Emerald Ore veins.")
	public int asteroidEmeraldOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Emerald Ore veins.")
	public int asteroidEmeraldOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Metite Ore weight.")
	public int asteroidMetiteOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Metite Ore weight.")
	public int asteroidMetiteOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Metite Ore veins.")
	public int asteroidMetiteOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Metite Ore veins.")
	public int asteroidMetiteOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Asterite Ore weight.")
	public int asteroidAsteriteOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Asterite Ore weight.")
	public int asteroidAsteriteOreMaximumRange = 32;

	@Comment("Minimum size of Asteroid Asterite Ore veins.")
	public int asteroidAsteriteOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Asterite Ore veins.")
	public int asteroidAsteriteOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Stellum Ore weight.")
	public int asteroidStellumOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Stellum Ore weight.")
	public int asteroidStellumOreMaximumRange = 8;

	@Comment("Minimum size of Asteroid Stellum Ore veins.")
	public int asteroidStellumOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Stellum Ore veins.")
	public int asteroidStellumOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Galaxium Ore weight.")
	public int asteroidGalaxiumOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Galaxium Ore weight.")
	public int asteroidGalaxiumOreMaximumRange = 4;

	@Comment("Minimum size of Asteroid Galaxium Ore veins.")
	public int asteroidGalaxiumOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Galaxium Ore veins.")
	public int asteroidGalaxiumOreMaximumSize = 48;

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

	@Comment("Energy for the Vent.")
	public double ventEnergy = 16384D;

	@Comment("Fluid for the Vent.")
	public long ventFluid = 16L;

	@Comment("Fluid for the Tank.")
	public long tankFluid = 16L;

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
	public long blockPlacerTimeConsumed = 40L;

	@Comment("Delay for the Block Breaker actions (smaller is faster).")
	public long blockBreakerTimeConsumed = 40L;

	@Comment("Delay for the Fluid Inserter actions (smaller is faster).")
	public long fluidInserterTimeConsumed = 40L;

	@Comment("Delay for the Fluid Extractor actions (smaller is faster).")
	public long fluidExtractorTimeConsumed = 40L;

	public static AstromineConfig get() {
		try {
			return AutoConfig.getConfigHolder(AstromineConfig.class).getConfig();
		} catch(RuntimeException exception) {
			return DEFAULT;
		}
	}

	public static void initialize() {
		AutoConfig.register(AstromineConfig.class, JanksonConfigSerializer::new);
	}
}
