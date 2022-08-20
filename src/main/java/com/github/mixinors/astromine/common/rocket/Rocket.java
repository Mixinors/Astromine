package com.github.mixinors.astromine.common.rocket;

import com.github.mixinors.astromine.common.item.rocket.*;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.tick.Tickable;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class Rocket implements Tickable {
	private static final String UUID_KEY = "uuid";
	
	private static final String FUEL_TANK_ITEM_ID_KEY = "FuelTankItemId";
	private static final String HULL_ITEM_ID_KEY = "HullItemId";
	private static final String LANDING_MECHANISM_ID_KEY = "LandingMechanismId";
	private static final String LIFE_SUPPORT_ID_KEY = "LifeSupportId";
	private static final String SHIELDING_ID_KEY = "ShieldingId";
	private static final String THRUSTER_ID_KEY = "ThrusterId";
	
	private static final String JOURNEY_KEY = "Journey";
	
	private static final String SIZE_KEY = "Size";

	public static final String ITEM_STORAGE_KEY = "ItemStorage";
	public static final String FLUID_STORAGE_KEY = "FluidStorage";
	
	public static final FluidIngredient FUEL_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.FUEL), 1L);
	public static final FluidIngredient OXYGEN_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.OXYGEN), 2L);
	
	public static final int FLUID_INPUT_SLOT_1 = 0;
	public static final int FLUID_INPUT_SLOT_2 = 1;
	public static final int FLUID_OUTPUT_SLOT_1 = 0;
	public static final int FLUID_OUTPUT_SLOT_2 = 1;
	
	public static final int ITEM_INPUT_SLOT_1 = 0;
	public static final int ITEM_INPUT_SLOT_2 = 2;
	public static final int ITEM_BUFFER_SLOT_1 = 4;
	public static final int ITEM_OUTPUT_SLOT_1 = 1;
	public static final int ITEM_OUTPUT_SLOT_2 = 3;
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT_1, ITEM_INPUT_SLOT_2 };
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_BUFFER_SLOT_1, ITEM_OUTPUT_SLOT_1, ITEM_OUTPUT_SLOT_2 };
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { FLUID_INPUT_SLOT_1, FLUID_INPUT_SLOT_2 };
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { };
	
	private UUID uuid;
	
	private RocketFuelTankPart fuelTank;
	private RocketHullPart hull;
	private RocketLandingMechanismPart landingMechanism;
	private RocketLifeSupportPart lifeSupport;
	private RocketShieldingPart shielding;
	private RocketThrusterPart thruster;
	
	private RocketJourney journey;
	
	private boolean syncItemStorage = true;
	private boolean syncFluidStorage = true;
	
	private SimpleItemStorage itemStorage = null;
	private SimpleFluidStorage fluidStorage = null;
	
	private long lastItemStorageVersion = 0;
	private long lastFluidStorageVersion = 0;
	
	public Rocket() {
	
	}
	
	public Rocket(UUID uuid, RocketFuelTankPart fuelTank, RocketHullPart hull, RocketLandingMechanismPart landingMechanism, RocketLifeSupportPart lifeSupport, RocketShieldingPart shielding, RocketThrusterPart thruster) {
		this.uuid = uuid;
		
		this.fuelTank = fuelTank;
		this.hull = hull;
		this.landingMechanism = landingMechanism;
		this.lifeSupport = lifeSupport;
		this.shielding = shielding;
		this.thruster = thruster;
	}
	
	@Override
	public void tick() {
		if (journey != null) {
			if (journey.hasStarted() && !journey.hasFinished()) {
				journey.tick(this);
			} else if (journey.hasFinished()) {
				journey = null;
			}
		}
	}
	
	public void onPartChanged() {
		itemStorage = new SimpleItemStorage(5).extractPredicate((variant, slot) ->
				slot == ITEM_BUFFER_SLOT_1 || slot == ITEM_OUTPUT_SLOT_1 || slot == ITEM_OUTPUT_SLOT_2
		).insertPredicate((variant, slot) ->
				slot == ITEM_INPUT_SLOT_1 || slot == ITEM_INPUT_SLOT_2
		).listener(() -> {
			syncData();
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		
		fluidStorage = new SimpleFluidStorage(2, fuelTank.getCapacity().getSize()).extractPredicate((variant, slot) ->
				false
		).insertPredicate((variant, slot) ->
				(slot == FLUID_INPUT_SLOT_1 && OXYGEN_INGREDIENT.testVariant(variant)) || (slot == FLUID_INPUT_SLOT_2 && FUEL_INGREDIENT.testVariant(variant))
		).listener(() -> {
			syncData();
		}).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
	}
	
	public void syncData() {
		var server = InstanceUtil.getServer();
		
		// TODO: Check if this actually works.
		if (server != null) {
			AMComponents.ROCKET_COMPONENT.sync(server);
		}
	}
	
	public void writeToNbt(NbtCompound nbt) {
		nbt.putUuid(UUID_KEY, uuid);
		
		NbtUtil.putIdentifier(nbt, HULL_ITEM_ID_KEY, Registry.ITEM.getId(hull.asItem()));
		NbtUtil.putIdentifier(nbt, LANDING_MECHANISM_ID_KEY, Registry.ITEM.getId(landingMechanism.asItem()));
		NbtUtil.putIdentifier(nbt, LIFE_SUPPORT_ID_KEY, Registry.ITEM.getId(lifeSupport.asItem()));
		NbtUtil.putIdentifier(nbt, SHIELDING_ID_KEY, Registry.ITEM.getId(shielding.asItem()));
		NbtUtil.putIdentifier(nbt, THRUSTER_ID_KEY, Registry.ITEM.getId(thruster.asItem()));
		
		if (journey != null) {
			var journeyNbt = new NbtCompound();
			RocketJourney.CODEC.encode(journey, NbtOps.INSTANCE, journeyNbt);
			nbt.put(JOURNEY_KEY, journeyNbt);
		}
		
		if (itemStorage != null) {
			var itemStorageNbt = new NbtCompound();
			itemStorageNbt.putInt(SIZE_KEY, itemStorage.getSize());
			
			itemStorage.writeToNbt(itemStorageNbt);
			
			nbt.put(ITEM_STORAGE_KEY, itemStorageNbt);
		}
		
		if (fluidStorage != null) {
			var fluidStorageNbt = new NbtCompound();
			fluidStorageNbt.putInt(SIZE_KEY, fluidStorage.getSize());
			
			fluidStorage.writeToNbt(fluidStorageNbt);
			
			nbt.put(FLUID_STORAGE_KEY, fluidStorageNbt);
		}
		
		var itemStorage = getItemStorage();
		
		if (!syncItemStorage && itemStorage.getVersion() == lastItemStorageVersion) {
			nbt.remove(ITEM_STORAGE_KEY);
		} else {
			syncItemStorage = false;
			
			lastItemStorageVersion = itemStorage.getVersion();
		}
		
		var fluidStorage = getFluidStorage();
		
		if (!syncFluidStorage && fluidStorage.getVersion() == lastFluidStorageVersion) {
			nbt.remove(FLUID_STORAGE_KEY);
		} else {
			syncFluidStorage = false;
			
			lastFluidStorageVersion = fluidStorage.getVersion();
		}
	}

	public void readFromNbt(NbtCompound nbt) {
		this.uuid = nbt.getUuid(UUID_KEY);
		
		var fuelTankItem = (RocketFuelTankItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, FUEL_TANK_ITEM_ID_KEY));
		this.fuelTank = fuelTankItem.getPart();
		
		var hullItem = (RocketHullItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, HULL_ITEM_ID_KEY));
		this.hull = hullItem.getPart();
		
		var landingMechanismItem = (RocketLandingMechanismItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, LANDING_MECHANISM_ID_KEY));
		this.landingMechanism = landingMechanismItem.getPart();
		
		var lifeSupportItem = (RocketLifeSupportItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, LIFE_SUPPORT_ID_KEY));
		this.lifeSupport = lifeSupportItem.getPart();
		
		var shieldingItem = (RocketShieldingItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, SHIELDING_ID_KEY));
		this.shielding = shieldingItem.getPart();
		
		var thrusterItem = (RocketThrusterItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, THRUSTER_ID_KEY));
		this.thruster = thrusterItem.getPart();
		
		if (nbt.contains(JOURNEY_KEY)) {
			var journeyNbt = nbt.getCompound(JOURNEY_KEY);
			this.journey = RocketJourney.CODEC.decode(NbtOps.INSTANCE, journeyNbt).result().get().getFirst();;
		}
		
		if (nbt.contains(ITEM_STORAGE_KEY)) {
			var itemStorageNbt = nbt.getCompound(ITEM_STORAGE_KEY);
			
			itemStorage.readFromNbt(itemStorageNbt);
		}
		
		if (nbt.contains(FLUID_STORAGE_KEY)) {
			var fluidStorageNbt = nbt.getCompound(FLUID_STORAGE_KEY);
			
			fluidStorage.readFromNbt(fluidStorageNbt);
		}
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public RocketJourney getJourney() {
		return journey;
	}
	
	public void setJourney(RocketJourney journey) {
		this.journey = journey;
	}
	
	public SimpleItemStorage getItemStorage() {
		return itemStorage;
	}
	
	public SimpleFluidStorage getFluidStorage() {
		return fluidStorage;
	}
	
	public long getOxygenRemaining() {
		return fluidStorage.getStorage(FLUID_INPUT_SLOT_1).getAmount();
	}
	
	public void setOxygenRemaining(long oxygenRemaining) {
		fluidStorage.getStorage(FLUID_INPUT_SLOT_1).setAmount(oxygenRemaining);
	}
	
	public long getFuelRemaining() {
		return fluidStorage.getStorage(FLUID_INPUT_SLOT_2).getAmount();
	}
	
	public void setFuelRemaining(long fuelRemaining) {
		fluidStorage.getStorage(FLUID_INPUT_SLOT_2).setAmount(fuelRemaining);
	}
	
	public void setSyncItemStorage(boolean syncItemStorage) {
		this.syncItemStorage = syncItemStorage;
	}
	
	public void setSyncFluidStorage(boolean syncFluidStorage) {
		this.syncFluidStorage = syncFluidStorage;
	}
	
	public RocketFuelTankPart getFuelTank() {
		return fuelTank;
	}
	
	public void setFuelTank(RocketFuelTankPart fuelTank) {
		this.fuelTank = fuelTank;
		
		onPartChanged();
	}
	
	public RocketHullPart getHull() {
		return hull;
	}
	
	public void setHull(RocketHullPart hull) {
		this.hull = hull;
		
		onPartChanged();
	}
	
	public RocketLandingMechanismPart getLandingMechanism() {
		return landingMechanism;
	}
	
	public void setLandingMechanism(RocketLandingMechanismPart landingMechanism) {
		this.landingMechanism = landingMechanism;
		
		onPartChanged();
	}
	
	public RocketLifeSupportPart getLifeSupport() {
		return lifeSupport;
	}
	
	public void setLifeSupport(RocketLifeSupportPart lifeSupport) {
		this.lifeSupport = lifeSupport;
		
		onPartChanged();
	}
	
	public RocketShieldingPart getShielding() {
		return shielding;
	}
	
	public void setShielding(RocketShieldingPart shielding) {
		this.shielding = shielding;
		
		onPartChanged();
	}
	
	public RocketThrusterPart getThruster() {
		return thruster;
	}
	
	public void setThruster(RocketThrusterPart thruster) {
		this.thruster = thruster;
		
		onPartChanged();
	}
}
