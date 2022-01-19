/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.config;

import com.github.mixinors.astromine.common.config.entry.tiered.CapacitorConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.TankConfig;
import com.github.mixinors.astromine.common.config.section.BatteriesConfigSection;
import com.github.mixinors.astromine.common.config.section.MachinesConfigSection;
import com.github.mixinors.astromine.common.config.section.PortableTanksConfigSection;
import com.github.mixinors.astromine.common.config.section.UtilitiesConfigSection;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine")
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

	@Comment("Settings for batteries")
	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	public BatteriesConfigSection batteries = new BatteriesConfigSection();

	@Comment("Settings for machines")
	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	public MachinesConfigSection machines = new MachinesConfigSection();

	@Comment("Settings for Capacitors")
	@ConfigEntry.Gui.CollapsibleObject
	public CapacitorConfig capacitors = new CapacitorConfig();

	@Comment("Settings for Tanks")
	@ConfigEntry.Gui.CollapsibleObject
	public TankConfig tanks = new TankConfig();

	@Comment("Settings for Portable Tanks")
	@ConfigEntry.Gui.CollapsibleObject
	public PortableTanksConfigSection portableTanks = new PortableTanksConfigSection();

	@Comment("Settings for utilities")
	@ConfigEntry.Gui.CollapsibleObject
	public UtilitiesConfigSection utilities = new UtilitiesConfigSection();

	@Comment("Energy for the Gravity Gauntlet.")
	public long gravityGauntletEnergy = batteries.singleBatteries.advanced;

	@Comment("Energy for the Gravity Gauntlet actions.")
	public long gravityGauntletConsumed = 512L;

	@Comment("Energy for the Drill actions.")
	public long drillConsumed = 6L;

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
	public long primitiveDrillEnergy = batteries.batteryPacks.primitive * 2;

	@Comment("Energy for the Basic Drill.")
	public long basicDrillEnergy = batteries.batteryPacks.basic * 2;

	@Comment("Energy for the Advanced Drill.")
	public long advancedDrillEnergy = batteries.batteryPacks.advanced * 2;

	@Comment("Energy for the Elite Drill.")
	public long eliteDrillEnergy = batteries.batteryPacks.elite * 2;

	@Comment("Energy rate for the Primitive Energy Cable.")
	public long primitiveEnergyNetworkTransferRate = 256;

	@Comment("Energy for the Basic Energy Cable.")
	public long basicEnergyNetworkTransferRate = 512;

	@Comment("Energy for the Advanced Energy Cable.")
	public long advancedEnergyNetworkTransferRate = 1024;

	@Comment("Energy for the Elite Energy Cable.")
	public long eliteEnergyNetworkTransferRate = 4096;

	@Comment("Fluid for the Space Suit.")
	public long spaceSuitFluid = portableTanks.regular;

	@Comment("Threshold for Asteroid Ores.")
	public int asteroidOreThreshold = 2;

	@Comment("Whether AK9 should asphyxiate.")
	public boolean asphyxiateAK9 = true;

	@Comment("Fluid amount for the Fluid Pipe and inter-machine transfer.")
	public long fluidNetworkTransferRate = FluidConstants.BUCKET * 4;

	@Comment("Default gravity for non-Astromine dimensions.")
	public double defaultGravity = 0.08D;

	@Comment("Chance for Piglins to realize if you try to trick them. (1 in x)")
	public int piglinAngerChance = 5;
	
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

	@Comment("The maximum fuel a Primitive Rocket can hold.")
	public long primitiveRocketFluid = FluidConstants.BUCKET * 16L;

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
