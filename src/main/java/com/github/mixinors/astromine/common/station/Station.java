package com.github.mixinors.astromine.common.station;

import com.github.mixinors.astromine.common.tick.Tickable;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.UUID;

public final class Station implements Tickable {
	private static final String WORLD_KEY_KEY = "World";
	private static final String POS_KEY = "Pos";
	
	private static final String UUID_KEY = "Uuid";
	private static final String OWNER_UUID_KEY = "OwnerUuid";
	
	private static final String NAME_KEY = "Name";
	
	private final RegistryKey<World> worldKey;
	
	private final BlockPos pos;
	
	private final UUID uuid;
	private final UUID ownerUuid;
	
	private final String name;
	
	public Station(RegistryKey<World> worldKey, BlockPos pos, UUID uuid, UUID ownerUuid, String name) {
		this.worldKey = worldKey;
		
		this.pos = pos;
		
		this.uuid = uuid;
		this.ownerUuid = ownerUuid;
		
		this.name = name;
	}
	
	public Station(NbtCompound nbt) {
		this.worldKey = NbtUtil.getRegistryKey(nbt, WORLD_KEY_KEY);
		this.pos = NbtUtil.getBlockPos(nbt, POS_KEY);
		
		this.uuid = nbt.getUuid(UUID_KEY);
		this.ownerUuid = nbt.getUuid(OWNER_UUID_KEY);
		
		this.name = nbt.getString(NAME_KEY);
	}
	
	public void writeToNbt(NbtCompound nbt) {
		NbtUtil.putRegistryKey(nbt, WORLD_KEY_KEY, worldKey);
		NbtUtil.putBlockPos(nbt, POS_KEY, pos);
		
		nbt.putUuid(UUID_KEY, uuid);
		nbt.putUuid(OWNER_UUID_KEY, ownerUuid);
		
		nbt.putString(NAME_KEY, name);
	}
	
	@Override
	public void tick() {
	
	}
	
	public RegistryKey<World> getWorldKey() {
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
