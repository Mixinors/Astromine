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

package com.github.chainmailstudios.astromine. registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AstromineCommonPackets {
	public static final ResourceLocation BLOCK_ENTITY_UPDATE_PACKET = AstromineCommon.identifier("block_entity_update");

	public static void initialize() {
		ServerSidePacketRegistry.INSTANCE.register(BLOCK_ENTITY_UPDATE_PACKET, (((context, buffer) -> {
			BlockPos blockPos = buffer.readBlockPos();
			ResourceLocation identifier = buffer.readResourceLocation();
			FriendlyByteBuf storedBuffer = new FriendlyByteBuf(buffer.copy());

			context.getTaskQueue().execute(() -> {
				BlockEntity blockEntity = context.getPlayer().getCommandSenderWorld().getBlockEntity(blockPos);

				if (blockEntity instanceof ComponentBlockEntity) {
					((ComponentBlockEntity) blockEntity).consumePacket(identifier, storedBuffer, context);
				}
			});
		})));
	}

}
