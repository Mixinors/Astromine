package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.mixinors.astromine.common.block.transfer.TransferType;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class AMNetworks {
	public static final Identifier TRANSFER_UPDATE = AMCommon.id("block_entity_update");
	
	public static final Identifier PRIMITIVE_ROCKET_SPAWN = AMCommon.id("primitive_rocket_spawn");
	
	public static final Identifier GAS_ADDED = AMCommon.id("gas_added");
	
	public static final Identifier GAS_REMOVED = AMCommon.id("gas_removed");
	
	public static final Identifier GAS_ERASED = AMCommon.id("gas_erased");
	
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.c2s(), TRANSFER_UPDATE, ((buf, context) -> {
			buf.retain();
			
			var pos = buf.readBlockPos();
			
			var id = buf.readIdentifier();
			var dir = buf.readEnumConstant(Direction.class);
			var type = buf.readEnumConstant(TransferType.class);
			
			context.queue(() -> {
				BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(pos);
				
				if (blockEntity instanceof ComponentBlockEntity componentBlockEntity) {
					componentBlockEntity.getTransferComponent().get(id).set(dir, type);
					componentBlockEntity.markDirty();
					componentBlockEntity.syncData();
				}
			});
		}));
	}
}
