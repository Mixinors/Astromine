package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class RocketInteriorManager {
	public static void teleportToRocket(PlayerEntity player, UUID uuid) {
		var rocket = RocketManager.get(uuid);
		if (rocket == null) return;
		
		var chunkPos = rocket.getInteriorPos();
		
		var placer = rocket.getPlacer(player.getUuid());
		
		if (placer == null) {
			placer = new Rocket.Placer(player.getWorld().getRegistryKey(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
		
			rocket.setPlacer(player.getUuid(), placer);
		}
		
		if (player instanceof ServerPlayerEntity serverPlayer) {
			var world = player.getServer().getWorld(AMWorlds.ROCKET_INTERIORS);
		
			serverPlayer.teleport(world, chunkPos.x * 16 + 3.5, 1, chunkPos.z * 16 + 3.5, 270, 0);
		}
	}
	
	public static void teleportToPlacer(PlayerEntity player, UUID uuid) {
		var rocket = RocketManager.get(uuid);
		if (rocket == null) return;
		
		var placer = rocket.getPlacer(player.getUuid());
		
		if (placer != null) {
			if (player instanceof ServerPlayerEntity serverPlayer) {
				var world = player.getServer().getWorld(placer.worldKey());
				if (world == null) return;
		
				serverPlayer.teleport(world, placer.x(), placer.y(), placer.z(), placer.yaw(), placer.pitch());
			}
		}
	}
	
}
