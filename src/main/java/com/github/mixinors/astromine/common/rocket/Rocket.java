package com.github.mixinors.astromine.common.rocket;

import com.github.mixinors.astromine.common.item.rocket.*;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.tick.Tickable;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMFluids;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Rocket Object 2: Electric boogaloo
 */
public final class Rocket implements Tickable {
	public static final FluidIngredient FUEL_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.FUEL), 1L);
	public static final FluidIngredient OXYGEN_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.OXYGEN), 2L);
	
	public static final int OXYGEN_TANK_IN = 0;
	public static final int OXYGEN_TANK_OUT = 0;
	public static final int FUEL_TANK_IN = 1;
	public static final int FUEL_TANK_OUT = 1;
	
	public static final int ITEM_INPUT_SLOT_1 = 0; // Oxygen Tank In
	public static final int ITEM_INPUT_SLOT_2 = 1; // Fuel Tank In
	public static final int ITEM_INPUT_SLOT_3 = 2; // Payload In
	public static final int FLUID_TANK_SLOT = 3; // Fuel Tank In
	public static final int HULL_SLOT = 4;
	public static final int LANDING_MECHANISM_SLOT = 5;
	public static final int LIFE_SUPPORT_SLOT = 6;
	public static final int SHIELDING_SLOT = 7;
	public static final int THRUSTER_SLOT = 8;
	public static final int ITEM_BUFFER_SLOT_1 = 9; // Oxygen Tank Buf
	public static final int ITEM_BUFFER_SLOT_2 = 10; // Fuel Tank Buf
	public static final int ITEM_OUTPUT_SLOT_1 = 11; // Oxygen Tank Out
	public static final int ITEM_OUTPUT_SLOT_2 = 12; // Fuel Tank Out
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT_1, ITEM_INPUT_SLOT_2, ITEM_INPUT_SLOT_3, FLUID_TANK_SLOT, HULL_SLOT, LANDING_MECHANISM_SLOT, LIFE_SUPPORT_SLOT, SHIELDING_SLOT, THRUSTER_SLOT };
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_BUFFER_SLOT_1, ITEM_BUFFER_SLOT_2, ITEM_OUTPUT_SLOT_1, ITEM_OUTPUT_SLOT_2 };
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { OXYGEN_TANK_IN, FUEL_TANK_IN };
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { OXYGEN_TANK_OUT, FUEL_TANK_OUT };
	
	public final UUID uuid;
	public final ChunkPos interiorWorldPos;
	public final Parts parts;
	public final SimpleItemStorage itemStorage;
	public SimpleFluidStorage fluidStorage;
	private RocketJourney journey;
	
	public Rocket(UUID uuid, ChunkPos interiorWorldPos) {
		this.uuid = uuid;
		this.interiorWorldPos = interiorWorldPos;
		this.parts = new Parts();
		
		this.itemStorage = new SimpleItemStorage(13).extractPredicate((variant, slot) -> {
			if (slot == ITEM_BUFFER_SLOT_1 || slot == ITEM_BUFFER_SLOT_2) {
				return true;
			} else {
				if (slot == FLUID_TANK_SLOT) {
					return fluidStorage.getStorage(OXYGEN_TANK_IN).isResourceBlank() && fluidStorage.getStorage(FUEL_TANK_IN).isResourceBlank();
				} else {
					return slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT;
				}
			}
		}).insertPredicate((variant, slot) ->
				slot == ITEM_INPUT_SLOT_1 || slot == ITEM_INPUT_SLOT_2 || slot == ITEM_INPUT_SLOT_3 || slot == FLUID_TANK_SLOT || slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT
		).listener(this::onStorageUpdate).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		
		updateFluidStorage();
	}
	
	public Rocket(NbtCompound tag) {
		this.uuid = tag.getUuid("uuid");
		this.interiorWorldPos = NbtUtil.getChunkPos(tag, "interior_world_pos");
		this.parts = new Parts(tag.getCompound("parts"));
		
		this.itemStorage = new SimpleItemStorage(13).extractPredicate((variant, slot) -> {
			if (slot == ITEM_BUFFER_SLOT_1 || slot == ITEM_BUFFER_SLOT_2) {
				return true;
			} else {
				if (slot == FLUID_TANK_SLOT) {
					return fluidStorage.getStorage(OXYGEN_TANK_IN).isResourceBlank() && fluidStorage.getStorage(FUEL_TANK_IN).isResourceBlank();
				} else {
					return slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT;
				}
			}
		}).insertPredicate((variant, slot) ->
				slot == ITEM_INPUT_SLOT_1 || slot == ITEM_INPUT_SLOT_2 || slot == ITEM_INPUT_SLOT_3 || slot == FLUID_TANK_SLOT || slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT
		).listener(this::onStorageUpdate).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		var itemStorageNbt = tag.getCompound("item_storage");
		itemStorage.readFromNbt(itemStorageNbt);
		
		updateFluidStorage();
		var fluidStorageNbt = tag.getCompound("fluid_storage");
		fluidStorage.readFromNbt(fluidStorageNbt);
	}
	
	public void writeToNbt(NbtCompound tag) {
		tag.putUuid("uuid", uuid);
		NbtUtil.putChunkPos(tag, "interior_world_pos", interiorWorldPos);
		tag.put("parts", parts.writeToNbt(new NbtCompound()));
		
		if (journey != null) {
			var journeyNbt = new NbtCompound();
			RocketJourney.CODEC.encode(journey, NbtOps.INSTANCE, journeyNbt);
			tag.put("journey", journeyNbt);
		}
		
		var itemStorageNbt = new NbtCompound();
		itemStorageNbt.putInt("size", itemStorage.getSize());
		itemStorage.writeToNbt(itemStorageNbt);
		tag.put("item_storage", itemStorageNbt);
		
		var fluidStorageNbt = new NbtCompound();
		fluidStorageNbt.putInt("size", fluidStorage.getSize());
		fluidStorage.writeToNbt(fluidStorageNbt);
		tag.put("fluid_storage", fluidStorageNbt);
	}
	
	@SuppressWarnings("DuplicatedCode")
	@Override
	public void tick() {
		tickJourney();
		
		var wildItemStorage = itemStorage.getWildProxy();
		var wildFluidStorage = fluidStorage.getWildProxy();
		
		var itemInputStorage1 = wildItemStorage.getStorage(ITEM_INPUT_SLOT_1);
		var itemBufferStorage1 = wildItemStorage.getStorage(ITEM_BUFFER_SLOT_1);
		var itemOutputStorage1 = wildItemStorage.getStorage(ITEM_OUTPUT_SLOT_1);
		var fluidInputStorage1 = wildFluidStorage.getStorage(OXYGEN_TANK_IN);
		var fluidOutputStorage1 = wildFluidStorage.getStorage(OXYGEN_TANK_OUT);
		
		var unloadFluidStorages1 = FluidStorage.ITEM.find(itemInputStorage1.getStack(), ContainerItemContext.ofSingleSlot(itemInputStorage1));
		var loadFluidStorages1 = FluidStorage.ITEM.find(itemOutputStorage1.getStack(), ContainerItemContext.ofSingleSlot(itemOutputStorage1));
		
		try (var transaction = Transaction.openOuter()) {
			StorageUtil.move(unloadFluidStorages1, fluidInputStorage1, fluidVariant -> !fluidVariant.isBlank(), FluidConstants.BUCKET, transaction);
			StorageUtil.move(fluidOutputStorage1, loadFluidStorages1, fluidVariant -> !fluidVariant.isBlank(), FluidConstants.BUCKET, transaction);
			
			StorageUtil.move(itemInputStorage1, itemBufferStorage1, (variant) -> {
				var stored = StorageUtil.findStoredResource(unloadFluidStorages1, transaction);
				return stored == null || stored.isBlank();
			}, 1, transaction);
			
			transaction.commit();
		}
		
		var itemInputStorage2 = wildItemStorage.getStorage(ITEM_INPUT_SLOT_2);
		var itemBufferStorage2 = wildItemStorage.getStorage(ITEM_BUFFER_SLOT_2);
		var itemOutputStorage2 = wildItemStorage.getStorage(ITEM_OUTPUT_SLOT_2);
		var fluidInputStorage2 = wildFluidStorage.getStorage(FUEL_TANK_IN);
		var fluidOutputStorage2 = wildFluidStorage.getStorage(FUEL_TANK_OUT);
		
		var unloadFluidStorages2 = FluidStorage.ITEM.find(itemInputStorage2.getStack(), ContainerItemContext.ofSingleSlot(itemInputStorage2));
		var loadFluidStorages2 = FluidStorage.ITEM.find(itemOutputStorage2.getStack(), ContainerItemContext.ofSingleSlot(itemOutputStorage2));
		
		try (var transaction = Transaction.openOuter()) {
			StorageUtil.move(unloadFluidStorages2, fluidInputStorage2, fluidVariant -> !fluidVariant.isBlank(), FluidConstants.BUCKET, transaction);
			StorageUtil.move(fluidOutputStorage2, loadFluidStorages2, fluidVariant -> !fluidVariant.isBlank(), FluidConstants.BUCKET, transaction);
			
			StorageUtil.move(itemInputStorage2, itemBufferStorage2, (variant) -> {
				var stored = StorageUtil.findStoredResource(unloadFluidStorages2, transaction);
				return stored == null || stored.isBlank();
			}, 2, transaction);
			
			transaction.commit();
		}
	}
	
	private void updateFluidStorage() {
		var capacity = parts.getPart(PartType.FUEL_TANK).isPresent() ? ((RocketFuelTankPart) parts.getPart(PartType.FUEL_TANK).get()).getCapacity().getSize() : 1_000L;
		fluidStorage = new SimpleFluidStorage(2, capacity);
		
		if (parts.getPart(PartType.FUEL_TANK).isPresent()) {
			fluidStorage.extractPredicate((variant, slot) -> false)
						.extractPredicate((variant, slot) -> slot == OXYGEN_TANK_OUT || slot == FUEL_TANK_OUT)
						.insertPredicate((variant, slot) -> (slot == OXYGEN_TANK_IN && OXYGEN_INGREDIENT.testVariant(variant)) || (slot == FUEL_TANK_IN && FUEL_INGREDIENT.testVariant(variant)))
						.listener(RocketManager::sync).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
		}
	}
	
	private void onStorageUpdate() {
		var fluidTankItem = (RocketFuelTankItem) itemStorage.getStorage(FLUID_TANK_SLOT).getResource().toStack().getItem();
		var hullItem = (RocketHullItem) itemStorage.getStorage(HULL_SLOT).getResource().toStack().getItem();
		var landingMechanismItem = (RocketLandingMechanismItem) itemStorage.getStorage(LANDING_MECHANISM_SLOT).getResource().toStack().getItem();
		var lifeSupportItem = (RocketLifeSupportItem) itemStorage.getStorage(LIFE_SUPPORT_SLOT).getResource().toStack().getItem();
		var shieldingItem = (RocketShieldingItem) itemStorage.getStorage(SHIELDING_SLOT).getResource().toStack().getItem();
		var thrusterItem = (RocketThrusterItem) itemStorage.getStorage(THRUSTER_SLOT).getResource().toStack().getItem();
		
		this.parts.setPart(PartType.FUEL_TANK, fluidTankItem.getPart());
		this.parts.setPart(PartType.ROCKET_HULL, hullItem.getPart());
		this.parts.setPart(PartType.LANDING_MECHANISM, landingMechanismItem.getPart());
		this.parts.setPart(PartType.LIFE_SUPPORT, lifeSupportItem.getPart());
		this.parts.setPart(PartType.SHIELDING, shieldingItem.getPart());
		this.parts.setPart(PartType.THRUSTER, thrusterItem.getPart());
		
		updateFluidStorage();
		RocketManager.sync();
	}
	
	private void tickJourney() {
		if (journey != null) {
			if (journey.hasStarted() && !journey.hasFinished()) {
				journey.tick(this);
			} else if (journey.hasFinished()) {
				journey = null;
			}
		}
	}
	
	public void startJourney(RocketJourney journey) {
		if (this.journey == null) {
			throw new RuntimeException("Tried changing journey while already on one.");
		}
		
		this.journey = journey;
	}
	
	public Optional<RocketJourney> getJourney() {
		return journey == null ? Optional.empty() : Optional.of(journey);
	}
	
	public long getOxygen() {
		return fluidStorage.getStorage(OXYGEN_TANK_IN).getAmount();
	}
	
	public void setOxygen(long oxygenRemaining) {
		fluidStorage.getStorage(OXYGEN_TANK_IN).setAmount(oxygenRemaining);
	}
	
	public long getFuel() {
		return fluidStorage.getStorage(FUEL_TANK_IN).getAmount();
	}
	
	public void setFuel(long fuelRemaining) {
		fluidStorage.getStorage(FUEL_TANK_IN).setAmount(fuelRemaining);
	}
	
	/**
	 * Returns the thruster attached to the rocket
	 *
	 * @return returns null if no thruster is attached
	 */
	@NotNull
	public Optional<RocketThrusterPart> getThruster() {
		return this.parts.getPart(PartType.THRUSTER, RocketThrusterPart.class);
	}
	
	/**
	 * Used in {@link RocketManager#findUnoccupiedSpace()}
	 */
	public ChunkPos getInteriorPos() {
		return this.interiorWorldPos;
	}
	
	private static class Parts {
		
		private final Map<PartType, Optional<RocketPart<?>>> parts = new HashMap<>();
		
		public Parts() {
		}
		
		public Parts(NbtCompound tag) {
			for (PartType value : PartType.values()) {
				if (tag.contains("part_type_" + value.name())) {
					Identifier identifier = NbtUtil.getIdentifier(tag, "part_type_" + value.name());
					parts.put(value, Optional.of(((RocketPartItem<RocketPart<?>>) Registry.ITEM.get(identifier)).getPart()));
				}
			}
		}
		
		public Optional<RocketPart<?>> getPart(PartType type) {
			return parts.getOrDefault(type, Optional.empty());
		}
		
		@SuppressWarnings("unchecked")
		public <T extends RocketPart<?>> Optional<T> getPart(PartType type, Class<T> clazz) {
			return (Optional<T>) parts.getOrDefault(type, Optional.empty());
		}
		
		public void setPart(PartType type, RocketPart<?> part) {
			parts.put(type, Optional.of(part));
		}
		
		public NbtElement writeToNbt(NbtCompound tag) {
			for (Map.Entry<PartType, Optional<RocketPart<? extends Item>>> entry : parts.entrySet()) {
				if (entry.getValue().isPresent()) {
					NbtUtil.putIdentifier(tag, "part_type_" + entry.getKey().name(), Registry.ITEM.getId(entry.getValue().get().asItem()));
				}
			}
			
			return tag;
		}
	}
	
	public enum PartType {
		FUEL_TANK,
		ROCKET_HULL,
		LANDING_MECHANISM,
		LIFE_SUPPORT,
		SHIELDING,
		THRUSTER
	}
}
