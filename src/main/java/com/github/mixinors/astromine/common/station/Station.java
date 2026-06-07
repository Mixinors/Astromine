package com.github.mixinors.astromine.common.station;

import com.github.mixinors.astromine.common.tick.Tickable;
import com.github.mixinors.astromine.common.util.NbtUtils;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class Station implements Tickable {
	private static final String WORLD_KEY_KEY = "World";
	private static final String POS_KEY = "Pos";
	
	private static final String UUID_KEY = "Uuid";
	private static final String OWNER_UUID_KEY = "OwnerUuid";
	
	private static final String NAME_KEY = "Name";
	
	private final ResourceKey<Level> worldKey;
	
	private final BlockPos pos;
	
	private final UUID uuid;
	private final UUID ownerUuid;
	
	private final String name;
	
	public Station(ResourceKey<Level> worldKey, BlockPos pos, UUID uuid, UUID ownerUuid, String name) {
		this.worldKey = worldKey;
		
		this.pos = pos;
		
		this.uuid = uuid;
		this.ownerUuid = ownerUuid;
		
		this.name = name;
	}
	
	public Station(CompoundTag nbt) {
		this.worldKey = NbtUtils.getRegistryKey(nbt, WORLD_KEY_KEY);
		this.pos = NbtUtils.getBlockPos(nbt, POS_KEY);
		
		this.uuid = nbt.getUUID(UUID_KEY);
		this.ownerUuid = nbt.getUUID(OWNER_UUID_KEY);
		
		this.name = nbt.getString(NAME_KEY);
	}
	
	public void writeToNbt(CompoundTag nbt) {
		NbtUtils.putRegistryKey(nbt, WORLD_KEY_KEY, worldKey);
		NbtUtils.putBlockPos(nbt, POS_KEY, pos);
		
		nbt.putUUID(UUID_KEY, uuid);
		nbt.putUUID(OWNER_UUID_KEY, ownerUuid);
		
		nbt.putString(NAME_KEY, name);
	}
	
	@Override
	public void tick() {
	
	}
	
	public ResourceKey<Level> getWorldKey() {
		return worldKey;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public UUID getOwnerUuid() {
		return ownerUuid;
	}
	
	public String getName() {
		return name;
	}
}
