package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.common.weapon.WeaponElement;

import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class AstromineServerPackets {
	public static final Identifier SHOT_PACKET = new Identifier(AstromineCommon.MOD_ID, "shot");

	public static void initialize() {
		ServerSidePacketRegistry.INSTANCE.register(SHOT_PACKET, ((context, buffer) -> {
			context.getTaskQueue().execute(() -> {
				if (context.getPlayer().getMainHandStack().getItem() instanceof WeaponElement) {
					((BaseWeapon) context.getPlayer().getMainHandStack().getItem()).tryShot(context.getPlayer().getEntityWorld(), context.getPlayer());
				}
			});
		}));
	}
}
