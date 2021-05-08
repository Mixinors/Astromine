package com.github.mixinors.astromine.registry;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.base.ComponentBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AMNetworks {
	public static final Identifier BLOCK_ENTITY_UPDATE_PACKET = AMCommon.identifier("block_entity_update");
	
	public static final Identifier PRIMITIVE_ROCKET_SPAWN = AMCommon.identifier("primitive_rocket_spawn");
	
	public static final Identifier GAS_ADDED = AMCommon.identifier("gas_added");
	
	public static final Identifier GAS_REMOVED = AMCommon.identifier("gas_removed");
	
	public static final Identifier GAS_ERASED = AMCommon.identifier("gas_erased");
	
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(BLOCK_ENTITY_UPDATE_PACKET, ((server, player, handler, buf, responseSender) -> {
			buf.retain();
			
			BlockPos blockPos = buf.readBlockPos();
			Identifier identifier = buf.readIdentifier();
			
			server.execute(() -> {
				BlockEntity blockEntity = player.world.getBlockEntity(blockPos);
				
				if (blockEntity instanceof ComponentBlockEntity) {
					((ComponentBlockEntity) blockEntity).consumePacket(identifier, buf);
				}
			});
		}));
	}
}
