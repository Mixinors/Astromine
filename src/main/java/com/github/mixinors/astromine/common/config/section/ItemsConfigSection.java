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

package com.github.mixinors.astromine.common.config.section;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ItemsConfigSection {
	@Comment("Settings for Batteries")
	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	public BatteriesConfigSection batteries = new BatteriesConfigSection();
	
	@Comment("Settings for Portable Tanks")
	@ConfigEntry.Gui.CollapsibleObject
	public PortableTanksConfigSection portableTanks = new PortableTanksConfigSection();
	
	@Comment("Energy for the Gravity Gauntlet")
	public long gravityGauntletEnergy = batteries.singleBatteries.advanced;
	
	@Comment("Energy for the Gravity Gauntlet actions")
	public long gravityGauntletConsumed = 512L;
	
	@Comment("Delay for the Fire Extinguisher sneaking actions")
	public int fireExtinguisherSneakingDelay = 2;
	
	@Comment("Delay for the Fire Extinguisher standing actions")
	public int fireExtinguisherStandingDelay = 10;
	
	@Comment("Energy for the Drill block breaking")
	public long drillConsumedBlockBreak = 6L;
	
	@Comment("Energy for the Drill entity hits")
	public long drillConsumedEntityHit = 12L;
	
	@Comment("Energy for the Primitive Drill")
	public long primitiveDrillEnergy = batteries.batteryPacks.primitive * 2L;
	
	@Comment("Energy for the Basic Drill")
	public long basicDrillEnergy = batteries.batteryPacks.basic * 2L;
	
	@Comment("Energy for the Advanced Drill")
	public long advancedDrillEnergy = batteries.batteryPacks.advanced * 2L;
	
	@Comment("Energy for the Elite Drill")
	public long eliteDrillEnergy = batteries.batteryPacks.elite * 2L;
	
	@Comment("Energy for the Space Suit Chestplate")
	public long spaceSuitChestplateEnergy = batteries.batteryPacks.elite * 2L;
	
	@Comment("Fluid for the Space Suit Chestplate")
	public long spaceSuitChestplateFluid = portableTanks.large * 2L;
	
	@Comment("Fluid consumption for the Space Suit Chestplate")
	public long spaceSuitChestplateFluidConsumption = spaceSuitChestplateFluid / (20L * 60L * 30L);
	
	@Comment("Energy consumption for the Space Suit Chestplate")
	public long spaceSuitChestplateEnergyConsumption = spaceSuitChestplateEnergy / (20L * 60L * 30L);
	
	
	
	
	@Comment("Liquid Oxygen consumption modifier for the Primitive Rocket Thruster")
	public double primitiveRocketThrusterLiquidOxygenConsumptionCoefficient = 0.66;
	
	@Comment("Liquid Fuel consumption modifier for the Primitive Rocket Thruster")
	public double primitiveRocketThrusterLiquidFuelConsumptionCoefficient = 0.33;
	
	@Comment("Solid Fuel consumption modifier for the Primitive Rocket Thruster")
	public double primitiveRocketThrusterSolidFuelConsumptionCoefficient = 0.66;
	
	@Comment("Fuel Tank capacity for the Primitive Rocket Fuel Tank")
	public long primitiveRocketFuelTankCapacity = portableTanks.large * 4L;
	@Comment("Hull minimum temperature for the Primitive Rocket Hull")
	public double primitiveRocketHullMinimumTemperature = 0.0D;
	
	@Comment("Hull maximum temperature for the Primitive Rocket Hull")
	public double primitiveRocketHullMaximumTemperature = 140.0D;
	
	
	
	
	@Comment("Liquid Oxygen consumption modifier for the Basic Rocket Thruster")
	public double basicRocketThrusterLiquidOxygenConsumptionCoefficient = 0.6;
	
	@Comment("Liquid Fuel consumption modifier for the Basic Rocket Thruster")
	public double basicRocketThrusterLiquidFuelConsumptionCoefficient = 0.27;
	
	@Comment("Solid Fuel consumption modifier for the Basic Rocket Thruster")
	public double basicRocketThrusterSolidFuelConsumptionCoefficient = 0.6;
	
	@Comment("Fuel Tank capacity for the Basic Rocket Fuel Tank")
	public long basicRocketFuelTankCapacity = portableTanks.large * 8L;
	
	@Comment("Hull minimum temperature for the Basic Rocket Hull")
	public double basicRocketHullMinimumTemperature = -30.0D;
	
	@Comment("Hull maximum temperature for the Basic Rocket Hull")
	public double basicRocketHullMaximumTemperature = 210.0D;
	
	
	
	
	@Comment("Liquid Oxygen consumption modifier for the Advanced Rocket Thruster")
	public double advancedRocketThrusterLiquidOxygenConsumptionCoefficient = 0.53;
	
	@Comment("Liquid Fuel consumption modifier for the Advanced Rocket Thruster")
	public double advancedRocketThrusterLiquidFuelConsumptionCoefficient = 0.21;
	
	@Comment("Solid Fuel consumption modifier for the Advanced Rocket Thruster")
	public double advancedRocketThrusterSolidFuelConsumptionCoefficient = 0.53;
	
	@Comment("Fuel Tank capacity for the Advanced Rocket Fuel Tank")
	public long advancedRocketFuelTankCapacity = portableTanks.large * 16L;
	
	@Comment("Hull minimum temperature for the Advanced Rocket Hull")
	public double advancedRocketHullMinimumTemperature = -50.0D;
	
	@Comment("Hull maximum temperature for the Advanced Rocket Hull")
	public double advancedRocketHullMaximumTemperature = 260.0D;
	
	
	
	
	@Comment("Liquid Oxygen consumption modifier for the Elite Rocket Thruster")
	public double eliteRocketThrusterLiquidOxygenConsumptionCoefficient = 0.47;
	
	@Comment("Liquid Fuel consumption modifier for the Elite Rocket Thruster")
	public double eliteRocketThrusterLiquidFuelConsumptionCoefficient = 0.15;
	
	@Comment("Solid Fuel consumption modifier for the Elite Rocket Thruster")
	public double eliteRocketThrusterSolidFuelConsumptionCoefficient = 0.47;
	
	@Comment("Fuel Tank capacity for the Elite Rocket Fuel Tank")
	public long eliteRocketFuelTankCapacity = portableTanks.large * 32L;
	
	@Comment("Hull minimum temperature for the Elite Rocket Hull")
	public double eliteRocketHullMinimumTemperature = -90.0D;
	
	@Comment("Hull maximum temperature for the Elite Rocket Hull")
	public double eliteRocketHullMaximumTemperature = 380.0D;
}
