/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.client.atmosphere;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * A manager for gasses on the client side.
 */
public class ClientAtmosphereManager {
	private static final Long2ObjectMap<FluidVolume> VOLUMES = new Long2ObjectOpenHashMap<>();

	/** Returns this manager's contents. */
	public static Long2ObjectMap<FluidVolume> getVolumes() {
		return VOLUMES;
	}

	/** Returns a {@link PacketByteBuf} for when all gasses are erased. */
	public static PacketByteBuf ofGasErased() {
		return new PacketByteBuf(Unpooled.buffer());
	}

	/** Returns a {@link PacketByteBuf} for when a gas is added. */
	public static PacketByteBuf ofGasAdded(BlockPos gasPosition, FluidVolume gasVolume) {
		var payload = new CompoundTag();
		payload.putLong("Position", gasPosition.asLong());
		payload.put("Volume", gasVolume.toTag());

		var buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeCompoundTag(payload);

		return buf;
	}

	/** Returns a {@link PacketByteBuf} for when a gas is removed. */
	public static PacketByteBuf ofGasRemoved(BlockPos gasPosition) {
		var payload = new CompoundTag();
		payload.putLong("Position", gasPosition.asLong());

		var buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeCompoundTag(payload);

		return buf;
	}

	/** Handles gas erasure {@link PacketByteBuf}s. */
	public static void onGasErased(PacketByteBuf gasBuffer) {
		VOLUMES.clear();
	}

	/** Handles gas addition {@link PacketByteBuf}s. */
	public static void onGasAdded(PacketByteBuf gasBuffer) {
		var payload = gasBuffer.readCompoundTag();
		var position = payload.getLong("Position");

		var volume = FluidVolume.fromTag(payload.getCompound("Volume"));

		VOLUMES.put(position, volume);
	}

	/** Handles gas removal {@link PacketByteBuf}s. */
	public static void onGasRemoved(PacketByteBuf gasBuffer) {
		var payload = gasBuffer.readCompoundTag();
		long position = payload.getLong("Position");

		VOLUMES.remove(position);
	}
}
