package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class RocketInteriorManager {
	
	// TODO: Teleport to rocket from UUID, or from player name?
	
	public static boolean teleportToRocket(PlayerEntity player, ChunkPos pos) {
		if (pos.x % 32 == 0 && pos.z % 32 == 0) {
			if (player instanceof ServerPlayerEntity) {
				RegistryKey<World> registryKey = RegistryKey.of(Registry.WORLD_KEY, AMCommon.id("rocket"));
				ServerWorld serverWorld = player.getServer().getWorld(registryKey);
				((ServerPlayerEntity) player).teleport(serverWorld, pos.x * 16 + 3.5, 1, pos.z * 16 + 3.5, 270, 0);
			}
			return true;
		} else {
			return false;
		}
	}
	
}
