package com.github.mixinors.astromine.common.entity.rocket.part;

import com.github.mixinors.astromine.common.entity.rocket.base.RocketEntity;
import com.github.mixinors.astromine.common.entity.rocket.part.base.RocketPart;
import com.github.mixinors.astromine.common.item.rocket.RocketThrusterItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public class RocketThrusterPart extends RocketPart<RocketThrusterItem> {
	private final double liquidOxygenConsumptionCoefficient;
	private final double liquidFuelConsumptionCoefficient;
	
	private final double solidFuelConsumptionCoefficient;
	
	public RocketThrusterPart(RocketThrusterItem item, Tier tier, double solidFuelConsumptionCoefficient) {
		super(item, tier);
		
		this.liquidOxygenConsumptionCoefficient = 0.0D;
		this.liquidFuelConsumptionCoefficient = 0.0D;
		
		this.solidFuelConsumptionCoefficient = solidFuelConsumptionCoefficient;
	}
	
	public RocketThrusterPart(RocketThrusterItem item, Tier tier, double liquidOxygenConsumptionCoefficient, double liquidFuelConsumptionCoefficient) {
		super(item, tier);
		
		this.liquidOxygenConsumptionCoefficient = liquidOxygenConsumptionCoefficient;
		this.liquidFuelConsumptionCoefficient = liquidFuelConsumptionCoefficient;
		
		this.solidFuelConsumptionCoefficient = 0.0D;
	}
	
	public RocketThrusterPart(RocketThrusterItem item, Tier tier, double liquidOxygenConsumptionCoefficient, double liquidFuelConsumptionCoefficient, double solidFuelConsumptionCoefficient) {
		super(item, tier);
		
		this.liquidOxygenConsumptionCoefficient = liquidOxygenConsumptionCoefficient;
		this.liquidFuelConsumptionCoefficient = liquidFuelConsumptionCoefficient;
		
		this.solidFuelConsumptionCoefficient = solidFuelConsumptionCoefficient;
	}
	
	public boolean usesLiquidFuel() {
		return solidFuelConsumptionCoefficient == 0.0D;
	}
	
	public boolean usesSolidFuel() {
		return solidFuelConsumptionCoefficient != 0.0D;
	}
	
	public long getAvailableTravelDistance(RocketEntity rocket) {
		if (usesLiquidFuel()) {
			var liquidOxygenStorage = rocket.getFluidStorage().getStorage(RocketEntity.LIQUID_FUEL_FLUID_INPUT_SLOT_1);
			var liquidFuelStorage = rocket.getFluidStorage().getStorage(RocketEntity.LIQUID_FUEL_FLUID_INPUT_SLOT_2);
			
			var liquidOxygenAmount = (liquidOxygenStorage.getAmount() / liquidOxygenConsumptionCoefficient);
			var liquidFuelAmount = (liquidFuelStorage.getAmount() / liquidFuelConsumptionCoefficient);
			
			return (long) ((liquidOxygenAmount * 0.66D + liquidFuelAmount * 0.33D) * 0.5D);
		} else {
			var solidFuelStorage = rocket.getFluidStorage().getStorage(RocketEntity.SOLID_FUEL_FLUID_INPUT_SLOT_1);
			
			var solidFuelAvailable = (solidFuelStorage.getAmount() / solidFuelConsumptionCoefficient);
			
			return (long) (solidFuelAvailable * 0.5D);
		}
	}
	
	public void consumeFuelByTravelledDistance(RocketEntity rocket, long distance) {
		if (usesLiquidFuel()) {
			var liquidOxygenStorage = rocket.getFluidStorage().getStorage(RocketEntity.LIQUID_FUEL_FLUID_INPUT_SLOT_1);
			var liquidFuelStorage = rocket.getFluidStorage().getStorage(RocketEntity.LIQUID_FUEL_FLUID_INPUT_SLOT_2);
			
			var amount = ((double) distance) * 0.5D;
			
			try (var transaction = Transaction.openOuter()) {
				liquidOxygenStorage.extract(liquidOxygenStorage.getResource(), (long) (amount * 0.66D), transaction);
				liquidFuelStorage.extract(liquidFuelStorage.getResource(), (long) (amount * 0.33D), transaction);
				
				transaction.commit();
			}
		} else {
			var solidFuelStorage = rocket.getFluidStorage().getStorage(RocketEntity.SOLID_FUEL_FLUID_INPUT_SLOT_1);
			
			var amount = ((double) distance) * 0.5D;
			
			try (var transaction = Transaction.openOuter()) {
				solidFuelStorage.extract(solidFuelStorage.getResource(), (long) amount, transaction);
				
				transaction.commit();
			}
		}
	}
	
	public double getLiquidOxygenConsumptionCoefficient() {
		return liquidOxygenConsumptionCoefficient;
	}
	
	public double getLiquidFuelConsumptionCoefficient() {
		return liquidFuelConsumptionCoefficient;
	}
	
	public double getSolidFuelConsumptionCoefficient() {
		return solidFuelConsumptionCoefficient;
	}
}
