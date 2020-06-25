package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.common.item.weapon.Weapon;
import com.github.chainmailstudios.astromine.common.packet.PacketConsumer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AstromineServerPackets {
	public static final Identifier SHOT_PACKET = AstromineCommon.identifier("shot");
	public static final Identifier BLOCK_ENTITY_UPDATE_PACKET = AstromineCommon.identifier("block_entity_update");

	public static void initialize() {
		ServerSidePacketRegistry.INSTANCE.register(SHOT_PACKET, ((context, buffer) -> {
			context.getTaskQueue().execute(() -> {
				if (context.getPlayer().getMainHandStack().getItem() instanceof Weapon) {
					((BaseWeapon) context.getPlayer().getMainHandStack().getItem()).tryShoot(context.getPlayer().getEntityWorld(), context.getPlayer());
				}
			});
		}));

		ServerSidePacketRegistry.INSTANCE.register(BLOCK_ENTITY_UPDATE_PACKET, (((context, buffer) -> {
			BlockPos blockPos = buffer.readBlockPos();
			Identifier identifier = new Identifier(buffer.readString());
			PacketByteBuf storedBuffer = new PacketByteBuf(buffer.copy());

			context.getTaskQueue().execute(() -> {
				BlockEntity blockEntity = context.getPlayer().getEntityWorld().getBlockEntity(blockPos);

				if (blockEntity instanceof PacketConsumer) {
					((PacketConsumer) blockEntity).consumePacket(identifier, storedBuffer, context);
				}
			});
		})));
	}

}
