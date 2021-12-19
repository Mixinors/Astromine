package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import dev.architectury.networking.NetworkManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AMNetworks {
	public static final Identifier BLOCK_ENTITY_UPDATE_PACKET = AMCommon.id("block_entity_update");
	
	public static final Identifier PRIMITIVE_ROCKET_SPAWN = AMCommon.id("primitive_rocket_spawn");
	
	public static final Identifier GAS_ADDED = AMCommon.id("gas_added");
	
	public static final Identifier GAS_REMOVED = AMCommon.id("gas_removed");
	
	public static final Identifier GAS_ERASED = AMCommon.id("gas_erased");
	
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.c2s(), BLOCK_ENTITY_UPDATE_PACKET, (( buf, context) -> {
			buf.retain();
			
			BlockPos blockPos = buf.readBlockPos();
			Identifier identifier = buf.readIdentifier();

			context.queue(() -> {
				BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(blockPos);
				
				if (blockEntity instanceof ExtendedBlockEntity) {
					((ExtendedBlockEntity) blockEntity).consumePacket(identifier, buf);
				}
			});
		}));
	}
}
