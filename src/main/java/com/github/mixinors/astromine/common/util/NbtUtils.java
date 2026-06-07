package com.github.mixinors.astromine.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;

public final class NbtUtils {
	private NbtUtils() {
	}
	
	public static ResourceLocation getIdentifier(CompoundTag tag, String key) {
		return ResourceLocation.parse(tag.getString(key));
	}
	
	public static void putIdentifier(CompoundTag tag, String key, ResourceLocation id) {
		tag.putString(key, id.toString());
	}
	
	public static BlockPos getBlockPos(CompoundTag tag, String key) {
		return BlockPos.of(tag.getLong(key));
	}
	
	public static void putBlockPos(CompoundTag tag, String key, BlockPos pos) {
		tag.putLong(key, pos.asLong());
	}
	
	public static CompoundTag writeBlockPos(BlockPos pos) {
		var tag = new CompoundTag();
		putBlockPos(tag, "pos", pos);
		return tag;
	}
	
	public static BlockPos readBlockPos(CompoundTag tag) {
		return getBlockPos(tag, "pos");
	}
	
	public static ChunkPos getChunkPos(CompoundTag tag, String key) {
		return new ChunkPos(tag.getLong(key));
	}
	
	public static void putChunkPos(CompoundTag tag, String key, ChunkPos pos) {
		tag.putLong(key, pos.toLong());
	}
	
	public static <T> ResourceKey<T> getRegistryKey(CompoundTag tag, String key) {
		var registry = ResourceLocation.parse(tag.getString(key + "Registry"));
		var location = ResourceLocation.parse(tag.getString(key));
		
		return ResourceKey.create(ResourceKey.createRegistryKey(registry), location);
	}
	
	public static <T> void putRegistryKey(CompoundTag tag, String key, ResourceKey<T> registryKey) {
		tag.putString(key + "Registry", registryKey.registry().toString());
		tag.putString(key, registryKey.location().toString());
	}
}
