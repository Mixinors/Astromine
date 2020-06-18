package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.common.item.weapon.Weapon;

public class AstromineServerPackets {
	public static final Identifier SHOT_PACKET = AstromineCommon.identifier("shot");

	public static void initialize() {
		ServerSidePacketRegistry.INSTANCE.register(SHOT_PACKET, ((context, buffer) -> {
			context.getTaskQueue().execute(() -> {
				if (context.getPlayer().getMainHandStack().getItem() instanceof Weapon) {
					((BaseWeapon) context.getPlayer().getMainHandStack().getItem()).tryShoot(context.getPlayer().getEntityWorld(), context.getPlayer());
				}
			});
		}));
	}
}
