package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.entity.player.PlayerEntity;

public class SoundUtils {
	public static float getSoundMufflingMultiplier(PlayerEntity player) {
		if (player.world == null) {
			return 1.0F;
		}
		
		return AMWorlds.isSpace(player.world.getRegistryKey()) ? 0.25F : 1.0F;
	}
}
