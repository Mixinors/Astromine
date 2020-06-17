package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ClientUtilities {
	public static void addEntity(PersistentProjectileEntity persistentProjectileEntity) {
		MinecraftClient.getInstance().world.addEntity(persistentProjectileEntity.getEntityId(), persistentProjectileEntity);
	}

	public static void playSound(BlockPos position, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
		MinecraftClient.getInstance().world.playSoundFromEntity(MinecraftClient.getInstance().player, MinecraftClient.getInstance().player, sound, category, volume, pitch);
	}

	public static final class Weapon {
		public static boolean isAiming() {
			return MinecraftClient.getInstance().options.keyUse.isPressed();
		}
	}
}
