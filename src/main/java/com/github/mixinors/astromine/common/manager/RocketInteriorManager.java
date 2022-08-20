package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.component.level.RocketComponent;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class RocketInteriorManager {
	public static void teleportToRocket(PlayerEntity player, UUID uuid) {
		var chunkPos = RocketManager.getChunkPosition(uuid);
		var placer = RocketManager.getPlacer(uuid, player.getUuid());
		
		if (placer == null) {
			placer = new RocketComponent.Placer(player.getWorld().getRegistryKey(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
			
			RocketManager.setPlacer(uuid, player.getUuid(), placer);
		}
		
		if (chunkPos.x % 32 == 0 && chunkPos.z % 32 == 0) {
			if (player instanceof ServerPlayerEntity serverPlayer) {
				var world = player.getServer().getWorld(AMWorlds.ROCKET);
				
				serverPlayer.teleport(world, chunkPos.x * 16 + 3.5, 1, chunkPos.z * 16 + 3.5, 270, 0);
			}
			
		}
	}
	
	public static void teleportToPlacer(PlayerEntity player, UUID uuid) {
		var placer = RocketManager.getPlacer(uuid, player.getUuid());
		
		if (placer != null) {
			if (player instanceof ServerPlayerEntity serverPlayer) {
				var world = player.getServer().getWorld(AMWorlds.ROCKET);
				
				serverPlayer.teleport(world, placer.x(), placer.y(), placer.z(), placer.yaw(), placer.pitch());
			}
		}
	}
	
}
