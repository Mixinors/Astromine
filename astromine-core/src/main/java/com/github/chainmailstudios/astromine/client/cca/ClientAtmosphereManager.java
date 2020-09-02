/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.client.cca;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class ClientAtmosphereManager {
	public static final Identifier GAS_ADDED = AstromineCommon.identifier("gas_added");
	public static final Identifier GAS_REMOVED = AstromineCommon.identifier("gas_removed");
	public static final Identifier GAS_ERASED = AstromineCommon.identifier("gas_erased");
	private static final Long2ObjectMap<FluidVolume> VOLUMES = new Long2ObjectOpenHashMap<>();

	public static Long2ObjectMap<FluidVolume> getVolumes() {
		return VOLUMES;
	}

	public static PacketByteBuf ofGasErased() {
		return new PacketByteBuf(Unpooled.buffer());
	}

	public static PacketByteBuf ofGasAdded(BlockPos gasPosition, FluidVolume gasVolume) {
		CompoundTag gasPayload = new CompoundTag();
		gasPayload.putLong("gasPosition", gasPosition.asLong());
		gasPayload.put("gasVolume", gasVolume.toTag());
		PacketByteBuf gasBuffer = new PacketByteBuf(Unpooled.buffer());
		gasBuffer.writeCompoundTag(gasPayload);
		return gasBuffer;
	}

	public static PacketByteBuf ofGasRemoved(BlockPos gasPosition) {
		CompoundTag gasPayload = new CompoundTag();
		gasPayload.putLong("gasPosition", gasPosition.asLong());
		PacketByteBuf gasBuffer = new PacketByteBuf(Unpooled.buffer());
		gasBuffer.writeCompoundTag(gasPayload);
		return gasBuffer;
	}

	public static void onGasErased(PacketByteBuf gasBuffer) {
		VOLUMES.clear();
	}

	public static void onGasAdded(PacketByteBuf gasBuffer) {
		CompoundTag gasPayload = gasBuffer.readCompoundTag();
		long gasPosition = gasPayload.getLong("gasPosition");
		FluidVolume gasVolume = FluidVolume.fromTag(gasPayload.getCompound("gasVolume"));
		VOLUMES.put(gasPosition, gasVolume);
	}

	public static void onGasRemoved(PacketByteBuf gasBuffer) {
		CompoundTag gasPayload = gasBuffer.readCompoundTag();
		long gasPosition = gasPayload.getLong("gasPosition");
		VOLUMES.remove(gasPosition);
	}
}
