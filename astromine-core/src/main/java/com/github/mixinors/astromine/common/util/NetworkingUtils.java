package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.common.transfer.RedstoneControl;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class NetworkingUtils {
	public static PacketByteBuf ofStorageSiding(StorageSiding siding, StorageType type, Direction direction, BlockPos pos) {
		PacketByteBuf buf = PacketByteBufs.create();
		
		buf.writeEnumConstant(siding);
		buf.writeEnumConstant(type);
		buf.writeEnumConstant(direction);
		buf.writeBlockPos(pos);
		
		return buf;
	}
	
	public static PacketByteBuf ofRedstoneControl(RedstoneControl control, BlockPos pos) {
		PacketByteBuf buf = PacketByteBufs.create();
		
		buf.writeEnumConstant(control);
		buf.writeBlockPos(pos);
		
		return buf;
	}
}
