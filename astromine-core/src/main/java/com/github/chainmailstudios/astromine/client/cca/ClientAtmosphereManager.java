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

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * A manager for gasses on the client side.
 */
public class ClientAtmosphereManager {
	public static final ResourceLocation GAS_ADDED = AstromineCommon.identifier("gas_added");

	public static final ResourceLocation GAS_REMOVED = AstromineCommon.identifier("gas_removed");

	public static final ResourceLocation GAS_ERASED = AstromineCommon.identifier("gas_erased");

	private static final Long2ObjectMap<FluidVolume> VOLUMES = new Long2ObjectOpenHashMap<>();

	/** Returns this manager's contents. */
	public static Long2ObjectMap<FluidVolume> getVolumes() {
		return VOLUMES;
	}

	/** Returns a {@link FriendlyByteBuf} for when all gasses are erased. */
	public static FriendlyByteBuf ofGasErased() {
		return new FriendlyByteBuf(Unpooled.buffer());
	}

	/** Returns a {@link FriendlyByteBuf} for when a gas is added. */
	public static FriendlyByteBuf ofGasAdded(BlockPos gasPosition, FluidVolume gasVolume) {
		CompoundTag gasPayload = new CompoundTag();
		gasPayload.putLong("gasPosition", gasPosition.asLong());
		gasPayload.put("gasVolume", gasVolume.toTag());

		FriendlyByteBuf gasBuffer = new FriendlyByteBuf(Unpooled.buffer());
		gasBuffer.writeNbt(gasPayload);

		return gasBuffer;
	}

	/** Returns a {@link FriendlyByteBuf} for when a gas is removed. */
	public static FriendlyByteBuf ofGasRemoved(BlockPos gasPosition) {
		CompoundTag gasPayload = new CompoundTag();
		gasPayload.putLong("gasPosition", gasPosition.asLong());

		FriendlyByteBuf gasBuffer = new FriendlyByteBuf(Unpooled.buffer());
		gasBuffer.writeNbt(gasPayload);

		return gasBuffer;
	}

	/** Handles {@link #GAS_ERASED} {@link FriendlyByteBuf}s. */
	public static void onGasErased(FriendlyByteBuf gasBuffer) {
		VOLUMES.clear();
	}

	/** Handles {@link #GAS_ADDED} {@link FriendlyByteBuf}s. */
	public static void onGasAdded(FriendlyByteBuf gasBuffer) {
		CompoundTag gasPayload = gasBuffer.readNbt();
		long gasPosition = gasPayload.getLong("gasPosition");

		FluidVolume gasVolume = FluidVolume.fromTag(gasPayload.getCompound("gasVolume"));

		VOLUMES.put(gasPosition, gasVolume);
	}

	/** Handles {@link #GAS_REMOVED} {@link FriendlyByteBuf}s. */
	public static void onGasRemoved(FriendlyByteBuf gasBuffer) {
		CompoundTag gasPayload = gasBuffer.readNbt();
		long gasPosition = gasPayload.getLong("gasPosition");

		VOLUMES.remove(gasPosition);
	}
}
