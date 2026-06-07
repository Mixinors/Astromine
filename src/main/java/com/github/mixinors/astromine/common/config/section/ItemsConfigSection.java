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


public class ItemsConfigSection {
	public BatteriesConfigSection batteries = new BatteriesConfigSection();
	public PortableTanksConfigSection portableTanks = new PortableTanksConfigSection();
	public long gravityGauntletEnergy = batteries.singleBatteries.advanced;
	public long gravityGauntletConsumed = 512L;
	public int fireExtinguisherSneakingDelay = 2;
	public int fireExtinguisherStandingDelay = 10;
	public long drillConsumedBlockBreak = 6L;
	public long drillConsumedEntityHit = 12L;
	public long primitiveDrillEnergy = batteries.batteryPacks.primitive * 2L;
	public long basicDrillEnergy = batteries.batteryPacks.basic * 2L;
	public long advancedDrillEnergy = batteries.batteryPacks.advanced * 2L;
	public long eliteDrillEnergy = batteries.batteryPacks.elite * 2L;
	public long spaceSuitChestplateEnergy = batteries.batteryPacks.elite * 2L;
	public long spaceSuitChestplateFluid = portableTanks.large * 2L;
	public long spaceSuitChestplateFluidConsumption = spaceSuitChestplateFluid / (20L * 60L * 30L);
	public long spaceSuitChestplateEnergyConsumption = spaceSuitChestplateEnergy / (20L * 60L * 30L);
	public long smallRocketFuelTankCapacity = portableTanks.large * 4L;
	public long mediumRocketFuelTankCapacity = portableTanks.large * 8L;
	public long largeRocketFuelTankCapacity = portableTanks.large * 16L;
	public long lowDurabilityRocketHullTrips = 6;
	public long mediumDurabilityRocketHullTrips = 18;
	public long highDurabilityRocketHullTrips = 36;
	public double lowEfficiencyRocketThrusterFuelConsumptionMultiplier = 1.0D;
	public double mediumEfficiencyRocketThrusterFuelConsumptionMultiplier = 0.8D;
	public double highEfficiencyRocketThrusterFuelConsumptionMultiplier = 0.5D;
}
