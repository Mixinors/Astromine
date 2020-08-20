package com.github.chainmailstudios.astromine.client.cca;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuckingHellCCA {
	private static final Map<BlockPos, FluidVolume> volumes = new HashMap<>();

	public static final Identifier FUCKS_GIVEN = AstromineCommon.identifier("fucks_given");
	public static final Identifier FUCKS_TAKEN = AstromineCommon.identifier("fucks_taken");
	public static final Identifier FUCKS_ERASED = AstromineCommon.identifier("fucks_erased");

	public static Map<BlockPos, FluidVolume> getVolumes() {
		return volumes;
	}

	public static PacketByteBuf ofFucksErased() {
		return new PacketByteBuf(Unpooled.buffer());
	}

	public static PacketByteBuf ofFucksGiven(BlockPos fuckPosition, FluidVolume fuckVolume) {
		CompoundTag fuckPayload = new CompoundTag();
		fuckPayload.putLong("fuckPosition", fuckPosition.asLong());
		fuckPayload.put("fuckVolume", fuckVolume.toTag(new CompoundTag()));
		PacketByteBuf fuckBuffer = new PacketByteBuf(Unpooled.buffer());
		fuckBuffer.writeCompoundTag(fuckPayload);
		return fuckBuffer;
	}

	public static PacketByteBuf ofFucksTaken(BlockPos fuckPosition) {
		CompoundTag fuckPayload = new CompoundTag();
		fuckPayload.putLong("fuckPosition", fuckPosition.asLong());
		PacketByteBuf fuckBuffer = new PacketByteBuf(Unpooled.buffer());
		fuckBuffer.writeCompoundTag(fuckPayload);
		return fuckBuffer;
	}

	public static void onFucksErased(PacketByteBuf fuckBuffer) {
		volumes.clear();
	}

	public static void onFucksGiven(PacketByteBuf fuckBuffer) {
		CompoundTag fuckPayload = fuckBuffer.readCompoundTag();
		BlockPos fuckPosition = BlockPos.fromLong(fuckPayload.getLong("fuckPosition"));
		FluidVolume fuckVolume = FluidVolume.fromTag(fuckPayload.getCompound("fuckVolume"));
		volumes.put(fuckPosition, fuckVolume);
	}

	public static void onFucksTaken(PacketByteBuf fuckBuffer) {
		CompoundTag fuckPayload = fuckBuffer.readCompoundTag();
		BlockPos fuckPosition = BlockPos.fromLong(fuckPayload.getLong("fuckPosition"));
		volumes.remove(fuckPosition);
	}
}
