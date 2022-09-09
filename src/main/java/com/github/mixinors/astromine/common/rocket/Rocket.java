package com.github.mixinors.astromine.common.rocket;

import com.github.mixinors.astromine.common.item.rocket.*;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.tick.Tickable;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class Rocket implements Tickable {
	private static final String UUID_KEY = "Uuid";
	private static final String CHUNK_POS_KEY = "ChunkPos";
	private static final String PLACERS_KEY = "Placer";
	
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
	
	public static final int FLUID_INPUT_SLOT_1 = 0; // Oxygen Tank In
	public static final int FLUID_INPUT_SLOT_2 = 1; // Fuel Tank In
	public static final int FLUID_OUTPUT_SLOT_1 = 0; // Oxygen Tank Out
	public static final int FLUID_OUTPUT_SLOT_2 = 1; // Fuel Tank Out
	
	public static final int ITEM_INPUT_SLOT_1 = 0; // Oxygen Tank In
	public static final int ITEM_INPUT_SLOT_2 = 1; // Fuel Tank In
	public static final int ITEM_INPUT_SLOT_3 = 2; // Payload In
	public static final int ITEM_INPUT_SLOT_4 = 3; // Fuel Tank In
	public static final int ITEM_INPUT_SLOT_5 = 4; // Hull In
	public static final int ITEM_INPUT_SLOT_6 = 5; // Landing Mechanism In
	public static final int ITEM_INPUT_SLOT_7 = 6; // Life Support In
	public static final int ITEM_INPUT_SLOT_8 = 7; // Shielding In
	public static final int ITEM_INPUT_SLOT_9 = 8; // Thruster In
	public static final int ITEM_BUFFER_SLOT_1 = 9; // Oxygen Tank Buf
	public static final int ITEM_BUFFER_SLOT_2 = 10; // Fuel Tank Buf
	public static final int ITEM_OUTPUT_SLOT_1 = 11; // Oxygen Tank Out
	public static final int ITEM_OUTPUT_SLOT_2 = 12; // Fuel Tank Out
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT_1, ITEM_INPUT_SLOT_2, ITEM_INPUT_SLOT_3, ITEM_INPUT_SLOT_4, ITEM_INPUT_SLOT_5, ITEM_INPUT_SLOT_6, ITEM_INPUT_SLOT_7, ITEM_INPUT_SLOT_8, ITEM_INPUT_SLOT_9 };
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_BUFFER_SLOT_1, ITEM_BUFFER_SLOT_2, ITEM_OUTPUT_SLOT_1, ITEM_OUTPUT_SLOT_2 };
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { FLUID_INPUT_SLOT_1, FLUID_INPUT_SLOT_2 };
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { FLUID_OUTPUT_SLOT_1, FLUID_OUTPUT_SLOT_2 };
	
	private UUID uuid;
	
	private ChunkPos chunkPos;
	
	private RocketFuelTankPart fuelTank = null;
	private RocketHullPart hull = null;
	private RocketLandingMechanismPart landingMechanism = null;
	private RocketLifeSupportPart lifeSupport = null;
	private RocketShieldingPart shielding = null;
	private RocketThrusterPart thruster = null;
	
	private RocketJourney journey;
	
	private boolean syncItemStorage = true;
	private boolean syncFluidStorage = true;
	
	private SimpleItemStorage itemStorage = null;
	private SimpleFluidStorage fluidStorage = null;
	
	private long lastItemStorageVersion = 0;
	private long lastFluidStorageVersion = 0;
	
	private final Map<UUID, Placer> placers = new HashMap<>();
	
	public Rocket(UUID uuid) {
		this.uuid = uuid;
		
		// Generate random chunk position for this rocket.
		var chunkPositions = RocketManager
				.getRockets()
				.stream()
				.map(Rocket::getChunkPos)
				.collect(Collectors.toSet());
		
		var random = new Random(uuid.hashCode());
		
		ChunkPos chunkPos = null;
		
		while (chunkPos == null || chunkPositions.contains(chunkPos)) {
			chunkPos = new ChunkPos(random.nextInt(Integer.MAX_VALUE / 2), random.nextInt(Integer.MAX_VALUE / 2));
		}
		
		this.chunkPos = chunkPos;
		
		itemStorage = new SimpleItemStorage(13).extractPredicate((variant, slot) -> {
			if (slot == ITEM_BUFFER_SLOT_1 || slot == ITEM_BUFFER_SLOT_2) {
				return true;
			} else {
				if (slot == ITEM_INPUT_SLOT_4) {
					return fluidStorage.getStorage(FLUID_INPUT_SLOT_1).isResourceBlank() && fluidStorage.getStorage(FLUID_INPUT_SLOT_2).isResourceBlank();
				} else {
					return slot == ITEM_INPUT_SLOT_5 || slot == ITEM_INPUT_SLOT_6 || slot == ITEM_INPUT_SLOT_7 || slot == ITEM_INPUT_SLOT_8 || slot == ITEM_INPUT_SLOT_9;
				}
			}
		}).insertPredicate((variant, slot) ->
				slot == ITEM_INPUT_SLOT_1 || slot == ITEM_INPUT_SLOT_2 || slot == ITEM_INPUT_SLOT_3 || slot == ITEM_INPUT_SLOT_4 || slot == ITEM_INPUT_SLOT_5 || slot == ITEM_INPUT_SLOT_6 || slot == ITEM_INPUT_SLOT_7 || slot == ITEM_INPUT_SLOT_8 || slot == ITEM_INPUT_SLOT_9
		).listener(() -> {
			var fluidTankItem = (RocketFuelTankItem) itemStorage.getStorage(ITEM_INPUT_SLOT_4).getResource().toStack().getItem();
			var hullItem = (RocketHullItem) itemStorage.getStorage(ITEM_INPUT_SLOT_5).getResource().toStack().getItem();
			var landingMechanismItem = (RocketLandingMechanismItem) itemStorage.getStorage(ITEM_INPUT_SLOT_6).getResource().toStack().getItem();
			var lifeSupportItem = (RocketLifeSupportItem) itemStorage.getStorage(ITEM_INPUT_SLOT_7).getResource().toStack().getItem();
			var shieldingItem = (RocketShieldingItem) itemStorage.getStorage(ITEM_INPUT_SLOT_8).getResource().toStack().getItem();
			var thrusterItem = (RocketThrusterItem) itemStorage.getStorage(ITEM_INPUT_SLOT_9).getResource().toStack().getItem();
			
			if (this.fuelTank != fluidTankItem.getPart()) {
				this.fuelTank = fluidTankItem.getPart();
				onPartChanged();
			}
			
			if (this.hull != hullItem.getPart()) {
				this.hull = hullItem.getPart();
				onPartChanged();
			}
			
			if (this.landingMechanism != landingMechanismItem.getPart()) {
				this.landingMechanism = landingMechanismItem.getPart();
				onPartChanged();
			}
			
			if (this.lifeSupport != lifeSupportItem.getPart()) {
				this.lifeSupport = lifeSupportItem.getPart();
				onPartChanged();
			}
			
			if (this.shielding != shieldingItem.getPart()) {
				this.shielding = shieldingItem.getPart();
				onPartChanged();
			}
			
			if (this.thruster != thrusterItem.getPart()) {
				this.thruster = thrusterItem.getPart();
				onPartChanged();
			}
			
			RocketManager.sync();
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
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
		
		var wildItemStorage = itemStorage.getWildProxy();
		var wildFluidStorage = fluidStorage.getWildProxy();
		
		var itemInputStorage1 = wildItemStorage.getStorage(Rocket.ITEM_INPUT_SLOT_1);
		var itemBufferStorage1 = wildItemStorage.getStorage(Rocket.ITEM_BUFFER_SLOT_1);
		var itemOutputStorage1 = wildItemStorage.getStorage(Rocket.ITEM_OUTPUT_SLOT_1);
		var fluidInputStorage1 = wildFluidStorage.getStorage(Rocket.FLUID_INPUT_SLOT_1);
		var fluidOutputStorage1 = wildFluidStorage.getStorage(Rocket.FLUID_OUTPUT_SLOT_1);
		
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
		
		var itemInputStorage2 = wildItemStorage.getStorage(Rocket.ITEM_INPUT_SLOT_2);
		var itemBufferStorage2 = wildItemStorage.getStorage(Rocket.ITEM_BUFFER_SLOT_2);
		var itemOutputStorage2 = wildItemStorage.getStorage(Rocket.ITEM_OUTPUT_SLOT_2);
		var fluidInputStorage2 = wildFluidStorage.getStorage(Rocket.FLUID_INPUT_SLOT_2);
		var fluidOutputStorage2 = wildFluidStorage.getStorage(Rocket.FLUID_OUTPUT_SLOT_2);
		
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
	
	public void onPartChanged() {
		if (fuelTank != null) {
			fluidStorage = new SimpleFluidStorage(2, fuelTank.getCapacity().getSize()).extractPredicate((variant, slot) ->
					false
			).extractPredicate((variant, slot) ->
					slot == FLUID_OUTPUT_SLOT_1 || slot == FLUID_OUTPUT_SLOT_2
			).insertPredicate((variant, slot) ->
					(slot == FLUID_INPUT_SLOT_1 && OXYGEN_INGREDIENT.testVariant(variant)) || (slot == FLUID_INPUT_SLOT_2 && FUEL_INGREDIENT.testVariant(variant))
			).listener(RocketManager::sync).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
		} else {
			fluidStorage = new SimpleFluidStorage(2, 1_000L);
		}
	}
	
	public void writeToNbt(NbtCompound nbt) {
		nbt.putUuid(UUID_KEY, uuid);
		
		NbtUtil.putChunkPos(nbt, CHUNK_POS_KEY, chunkPos);
		
		var placersNbt = new NbtCompound();
		
		for (var entry : placers.entrySet()) {
			var placerNbt = new NbtCompound();
			Placer.CODEC.encode(entry.getValue(), NbtOps.INSTANCE, placerNbt);
			
			placersNbt.put(entry.getKey().toString(), placerNbt);
		}
		
		nbt.put(PLACERS_KEY, placersNbt);
		
		if (fuelTank != null) NbtUtil.putIdentifier(nbt, FUEL_TANK_ITEM_ID_KEY, Registry.ITEM.getId(fuelTank.asItem()));
		if (hull != null) NbtUtil.putIdentifier(nbt, HULL_ITEM_ID_KEY, Registry.ITEM.getId(hull.asItem()));
		if (landingMechanism != null) NbtUtil.putIdentifier(nbt, LANDING_MECHANISM_ID_KEY, Registry.ITEM.getId(landingMechanism.asItem()));
		if (lifeSupport != null) NbtUtil.putIdentifier(nbt, LIFE_SUPPORT_ID_KEY, Registry.ITEM.getId(lifeSupport.asItem()));
		if (shielding != null) NbtUtil.putIdentifier(nbt, SHIELDING_ID_KEY, Registry.ITEM.getId(shielding.asItem()));
		if (thruster != null) NbtUtil.putIdentifier(nbt, THRUSTER_ID_KEY, Registry.ITEM.getId(thruster.asItem()));
		
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
		
		this.chunkPos = NbtUtil.getChunkPos(nbt, CHUNK_POS_KEY);
		
		var placersNbt = nbt.getCompound(PLACERS_KEY);
		
		for (var key : placersNbt.getKeys()) {
			var placerNbt = placersNbt.getCompound(key);
			var placer = Placer.CODEC.decode(NbtOps.INSTANCE, placerNbt).result().get().getFirst();
			
			placers.put(UUID.fromString(key), placer);
		}
		
		if (nbt.contains(FUEL_TANK_ITEM_ID_KEY)) {
			var fuelTankItem = (RocketFuelTankItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, FUEL_TANK_ITEM_ID_KEY));
			this.fuelTank = fuelTankItem.getPart();
		}
		
		if (nbt.contains(HULL_ITEM_ID_KEY)) {
			var hullItem = (RocketHullItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, HULL_ITEM_ID_KEY));
			this.hull = hullItem.getPart();
		}
		
		if (nbt.contains(LANDING_MECHANISM_ID_KEY)) {
			var landingMechanismItem = (RocketLandingMechanismItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, LANDING_MECHANISM_ID_KEY));
			this.landingMechanism = landingMechanismItem.getPart();
		}

		if (nbt.contains(LIFE_SUPPORT_ID_KEY)) {
			var lifeSupportItem = (RocketLifeSupportItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, LIFE_SUPPORT_ID_KEY));
			this.lifeSupport = lifeSupportItem.getPart();
		}

		if (nbt.contains(SHIELDING_ID_KEY)) {
			var shieldingItem = (RocketShieldingItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, SHIELDING_ID_KEY));
			this.shielding = shieldingItem.getPart();
		}
		
		if (nbt.contains(THRUSTER_ID_KEY)) {
			var thrusterItem = (RocketThrusterItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, THRUSTER_ID_KEY));
			this.thruster = thrusterItem.getPart();
		}
		
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
	
	public ChunkPos getChunkPos() {
		return chunkPos;
	}
	
	public Placer getPlacer(UUID uuid) {
		return placers.get(uuid);
	}
	
	public void setPlacer(UUID uuid, Placer placer) {
		placers.put(uuid, placer);
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
}
