package com.github.mixinors.astromine.common.rocket;

import com.github.mixinors.astromine.common.item.rocket.*;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.tick.Tickable;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class Rocket implements Tickable {
	private static final String UUID_KEY = "Uuid";
	private static final String PARTS_KEY = "Parts";
	private static final String PLACERS_KEY = "Placers";
	private static final String INTERIOR_POS_KEY = "InteriorPos";
	private static final String JOURNEY_KEY = "Journey";
	
	private static final String ITEM_STORAGE_KEY = "ItemStorage";
	private static final String FLUID_STORAGE_KEY = "FluidStorage";
	
	public static final FluidIngredient FUEL_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.FUEL), 1L);
	public static final FluidIngredient OXYGEN_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.OXYGEN), 2L);
	
	public static final int OXYGEN_TANK_FLUID_IN = 0;
	public static final int OXYGEN_TANK_FLUID_OUT = 0;
	public static final int FUEL_TANK_FLUID_IN = 1;
	public static final int FUEL_TANK_FLUID_OUT = 1;
	
	public static final int PAYLOAD_SLOT = 0;
	public static final int HULL_SLOT = 1;
	public static final int LANDING_MECHANISM_SLOT = 2;
	public static final int LIFE_SUPPORT_SLOT = 3;
	public static final int SHIELDING_SLOT = 4;
	public static final int THRUSTER_SLOT = 5;
	public static final int FUEL_TANK_SLOT = 6;
	
	public static final int OXYGEN_TANK_UNLOAD_SLOT = 7;
	public static final int OXYGEN_TANK_BUFFER_SLOT = 8;
	public static final int OXYGEN_TANK_OUTPUT_SLOT = 9;
	
	public static final int FUEL_TANK_UNLOAD_SLOT = 10;
	public static final int FUEL_TANK_BUFFER_SLOT = 11;
	public static final int FUEL_TANK_OUTPUT_SLOT = 12;
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { OXYGEN_TANK_UNLOAD_SLOT, FUEL_TANK_UNLOAD_SLOT, PAYLOAD_SLOT, HULL_SLOT, LANDING_MECHANISM_SLOT, LIFE_SUPPORT_SLOT, SHIELDING_SLOT, THRUSTER_SLOT, FUEL_TANK_SLOT };
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { OXYGEN_TANK_BUFFER_SLOT, FUEL_TANK_BUFFER_SLOT, OXYGEN_TANK_OUTPUT_SLOT, FUEL_TANK_OUTPUT_SLOT };
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { OXYGEN_TANK_FLUID_IN, FUEL_TANK_FLUID_IN };
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { OXYGEN_TANK_FLUID_OUT, FUEL_TANK_FLUID_OUT };
	
	private final UUID uuid;
	private final ChunkPos interiorPos;
	private final Parts parts;
	
	private RocketJourney journey;
	
	private final SimpleItemStorage itemStorage;
	private SimpleFluidStorage fluidStorage;
	
	private final Map<UUID, Placer> placers = new HashMap<>();
	
	public Rocket(UUID uuid, ChunkPos interiorPos) {
		this.uuid = uuid;
		this.interiorPos = interiorPos;
		this.parts = new Parts();
		
		this.itemStorage = new SimpleItemStorage(13).extractPredicate((variant, slot) -> {
			if (slot == OXYGEN_TANK_BUFFER_SLOT || slot == FUEL_TANK_BUFFER_SLOT) {
				return true;
			} else {
				if (slot == FUEL_TANK_SLOT) {
					return fluidStorage.getStorage(OXYGEN_TANK_FLUID_IN).isResourceBlank() && fluidStorage.getStorage(FUEL_TANK_FLUID_IN).isResourceBlank();
				} else {
					return slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT;
				}
			}
		}).insertPredicate((variant, slot) ->
				slot == OXYGEN_TANK_UNLOAD_SLOT || slot == FUEL_TANK_UNLOAD_SLOT || slot == PAYLOAD_SLOT || slot == FUEL_TANK_SLOT || slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT
		).listener(this::onStorageUpdate).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		
		updateFluidStorage();
	}
	
	public Rocket(NbtCompound nbt) {
		this.uuid = nbt.getUuid(UUID_KEY);
		this.interiorPos = NbtUtil.getChunkPos(nbt, INTERIOR_POS_KEY);
		this.parts = new Parts(nbt.getCompound(PARTS_KEY));
		
		var placersNbt = nbt.getCompound(PLACERS_KEY);
		
		for (var key : placersNbt.getKeys()) {
			var placerNbt = placersNbt.getCompound(key);
			var placer = Placer.CODEC.decode(NbtOps.INSTANCE, placerNbt).result().get().getFirst();
			
			placers.put(UUID.fromString(key), placer);
		}
		
		this.itemStorage = new SimpleItemStorage(13).extractPredicate((variant, slot) -> {
			if (slot == OXYGEN_TANK_BUFFER_SLOT || slot == FUEL_TANK_BUFFER_SLOT) {
				return true;
			} else {
				if (slot == FUEL_TANK_SLOT) {
					return fluidStorage.getStorage(OXYGEN_TANK_FLUID_IN).isResourceBlank() && fluidStorage.getStorage(FUEL_TANK_FLUID_IN).isResourceBlank();
				} else {
					return slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT;
				}
			}
		}).insertPredicate((variant, slot) ->
				slot == OXYGEN_TANK_UNLOAD_SLOT || slot == FUEL_TANK_UNLOAD_SLOT || slot == PAYLOAD_SLOT || slot == FUEL_TANK_SLOT || slot == HULL_SLOT || slot == LANDING_MECHANISM_SLOT || slot == LIFE_SUPPORT_SLOT || slot == SHIELDING_SLOT || slot == THRUSTER_SLOT
		).listener(this::onStorageUpdate).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		var itemStorageNbt = nbt.getCompound(ITEM_STORAGE_KEY);
		itemStorage.readFromNbt(itemStorageNbt);
		
		updateFluidStorage();
		var fluidStorageNbt = nbt.getCompound(FLUID_STORAGE_KEY);
		fluidStorage.readFromNbt(fluidStorageNbt);
	}
	
	public void writeToNbt(NbtCompound nbt) {
		nbt.putUuid(UUID_KEY, uuid);
		NbtUtil.putChunkPos(nbt, INTERIOR_POS_KEY, interiorPos);
		nbt.put(PARTS_KEY, parts.writeToNbt(new NbtCompound()));
		
		var placersNbt = new NbtCompound();
		
		for (var entry : placers.entrySet()) {
			var placerNbt = Placer.CODEC.encode(entry.getValue(), NbtOps.INSTANCE, new NbtCompound()).result().get();
			placersNbt.put(entry.getKey().toString(), placerNbt);
		}
		
		nbt.put(PLACERS_KEY, placersNbt);
		
		if (journey != null) {
			var journeyNbt = new NbtCompound();
			RocketJourney.CODEC.encode(journey, NbtOps.INSTANCE, journeyNbt);
			nbt.put(JOURNEY_KEY, journeyNbt);
		}
		
		var itemStorageNbt = new NbtCompound();
		itemStorage.writeToNbt(itemStorageNbt);
		nbt.put(ITEM_STORAGE_KEY, itemStorageNbt);
		
		var fluidStorageNbt = new NbtCompound();
		fluidStorage.writeToNbt(fluidStorageNbt);
		nbt.put(FLUID_STORAGE_KEY, fluidStorageNbt);
	}
	
	@SuppressWarnings("DuplicatedCode")
	@Override
	public void tick() {
		tickJourney();
		
		var wildItemStorage = itemStorage.getWildProxy();
		var wildFluidStorage = fluidStorage.getWildProxy();
		
		var itemInputStorage1 = wildItemStorage.getStorage(OXYGEN_TANK_UNLOAD_SLOT);
		var itemBufferStorage1 = wildItemStorage.getStorage(OXYGEN_TANK_BUFFER_SLOT);
		var itemOutputStorage1 = wildItemStorage.getStorage(OXYGEN_TANK_OUTPUT_SLOT);
		var fluidInputStorage1 = wildFluidStorage.getStorage(OXYGEN_TANK_FLUID_IN);
		var fluidOutputStorage1 = wildFluidStorage.getStorage(OXYGEN_TANK_FLUID_OUT);
		
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
		
		var itemInputStorage2 = wildItemStorage.getStorage(FUEL_TANK_UNLOAD_SLOT);
		var itemBufferStorage2 = wildItemStorage.getStorage(FUEL_TANK_BUFFER_SLOT);
		var itemOutputStorage2 = wildItemStorage.getStorage(FUEL_TANK_OUTPUT_SLOT);
		var fluidInputStorage2 = wildFluidStorage.getStorage(FUEL_TANK_FLUID_IN);
		var fluidOutputStorage2 = wildFluidStorage.getStorage(FUEL_TANK_FLUID_OUT);
		
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
		Optional<RocketFuelTankPart> fuelTank = parts.getPart(PartType.FUEL_TANK);
		long capacity = fuelTank.map(rocketFuelTankPart -> rocketFuelTankPart.getCapacity().getSize()).orElse(1_000L);
		
		fluidStorage = new SimpleFluidStorage(2, capacity);
		
		if (fuelTank.isPresent()) {
			fluidStorage.extractPredicate((variant, slot) -> false)
						.extractPredicate((variant, slot) -> slot == OXYGEN_TANK_FLUID_OUT || slot == FUEL_TANK_FLUID_OUT)
						.insertPredicate((variant, slot) -> (slot == OXYGEN_TANK_FLUID_IN && OXYGEN_INGREDIENT.testVariant(variant)) || (slot == FUEL_TANK_FLUID_IN && FUEL_INGREDIENT.testVariant(variant)))
						.listener(RocketManager::sync).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
		}
	}
	
	private void onStorageUpdate() {
		var fuelTankItem = itemStorage.getStorage(FUEL_TANK_SLOT).getResource().toStack().getItem();
		var hullItem = itemStorage.getStorage(HULL_SLOT).getResource().toStack().getItem();
		var landingMechanismItem = itemStorage.getStorage(LANDING_MECHANISM_SLOT).getResource().toStack().getItem();
		var lifeSupportItem = itemStorage.getStorage(LIFE_SUPPORT_SLOT).getResource().toStack().getItem();
		var shieldingItem =  itemStorage.getStorage(SHIELDING_SLOT).getResource().toStack().getItem();
		var thrusterItem = itemStorage.getStorage(THRUSTER_SLOT).getResource().toStack().getItem();
		
		if(fuelTankItem instanceof RocketFuelTankItem rocketFuelTankItem) this.parts.setPart(PartType.FUEL_TANK, rocketFuelTankItem.getPart());
		if(hullItem instanceof RocketHullItem rocketHullItem) this.parts.setPart(PartType.ROCKET_HULL, rocketHullItem.getPart());
		if(landingMechanismItem instanceof RocketLandingMechanismItem rocketLandingMechanismItem) this.parts.setPart(PartType.LANDING_MECHANISM, rocketLandingMechanismItem.getPart());
		if(lifeSupportItem instanceof RocketLifeSupportItem rocketLifeSupportItem) this.parts.setPart(PartType.LIFE_SUPPORT, rocketLifeSupportItem.getPart());
		if(shieldingItem instanceof RocketShieldingItem rocketShieldingItem) this.parts.setPart(PartType.SHIELDING, rocketShieldingItem.getPart());
		if(thrusterItem instanceof RocketThrusterItem rocketThrusterItem) this.parts.setPart(PartType.THRUSTER, rocketThrusterItem.getPart());
		
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
		return fluidStorage.getStorage(OXYGEN_TANK_FLUID_IN).getAmount();
	}
	
	public void setOxygen(long oxygenRemaining) {
		fluidStorage.getStorage(OXYGEN_TANK_FLUID_IN).setAmount(oxygenRemaining);
	}
	
	public long getFuel() {
		return fluidStorage.getStorage(FUEL_TANK_FLUID_IN).getAmount();
	}
	
	public void setFuel(long fuelRemaining) {
		fluidStorage.getStorage(FUEL_TANK_FLUID_IN).setAmount(fuelRemaining);
	}
	
	public ChunkPos getInteriorPos() {
		return this.interiorPos;
	}
	
	public Placer getPlacer(UUID uuid) {
		return this.placers.get(uuid);
	}
	
	public void setPlacer(UUID uuid, Placer placer) {
		this.placers.put(uuid, placer);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public Parts getParts() {
		return parts;
	}
	
	public SimpleItemStorage getItemStorage() {
		return itemStorage;
	}
	
	public SimpleFluidStorage getFluidStorage() {
		return fluidStorage;
	}
	
	public static class Parts {
		private final Map<PartType, Optional<RocketPart<?>>> parts = new HashMap<>();
		
		public Parts() {
		}
		
		public Parts(NbtCompound tag) {
			for (PartType value : PartType.values()) {
				if (tag.contains("part_type_" + value.name())) {
					var identifier = NbtUtil.getIdentifier(tag, "part_type_" + value.name());
					parts.put(value, Optional.of(((RocketPartItem<RocketPart<?>>) Registry.ITEM.get(identifier)).getPart()));
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		public <T extends RocketPart<?>> Optional<T> getPart(PartType type) {
			return (Optional<T>) parts.getOrDefault(type, Optional.empty());
		}
		
		@SuppressWarnings("unchecked")
		public <T extends RocketPart<?>> T getPartOrThrow(PartType type) {
			return (T) parts.getOrDefault(type, Optional.empty()).get();
		}
		
		public void setPart(PartType type, RocketPart<?> part) {
			parts.put(type, Optional.of(part));
		}
		
		public NbtElement writeToNbt(NbtCompound tag) {
			for (var entry : parts.entrySet()) {
				if (entry.getValue().isPresent()) {
					NbtUtil.putIdentifier(tag, "part_type_" + entry.getKey().name(), Registry.ITEM.getId(entry.getValue().get().asItem()));
				}
			}
			
			return tag;
		}
	}
	
	public record Placer(
			RegistryKey<World> worldKey,
			double x, double y, double z,
			float yaw, float pitch
	) {
		public static final Codec<Placer> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						RegistryKey.createCodec(Registry.WORLD_KEY).fieldOf("world").forGetter(Placer::worldKey),
						Codec.DOUBLE.fieldOf("x").forGetter(Placer::x),
						Codec.DOUBLE.fieldOf("y").forGetter(Placer::y),
						Codec.DOUBLE.fieldOf("z").forGetter(Placer::z),
						Codec.FLOAT.fieldOf("yaw").forGetter(Placer::yaw),
						Codec.FLOAT.fieldOf("pitch").forGetter(Placer::pitch)
				).apply(instance, Placer::new)
		);
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
