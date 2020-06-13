package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.common.weapon.Weapon;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class AstromineServerPackets {
	public static final Identifier SHOT_PACKET = new Identifier(AstromineCommon.MOD_ID, "shot");

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
