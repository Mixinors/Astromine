/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.block.entity.cable;

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public class CableBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity {
	public static final String DATA_KEY = "Data";
	
	private Connections connections = new Connections();
	
	private long lastToggledMs = 0;
	
	public CableBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.CABLE.get(), blockPos, blockState);
	}
	
	public Connections getConnections() {
		return connections;
	}
	
	public long getLastToggledMillis() {
		return lastToggledMs;
	}
	
	public void setLastToggledMillis(long lastToggledMillis) {
		this.lastToggledMs = lastToggledMillis;
	}
	
	@Override
	public @Nullable Object getRenderAttachmentData() {
		return connections;
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		
		nbt.putLongArray(DATA_KEY, connections.data.toLongArray());
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		connections = new Connections(BitSet.valueOf(nbt.getLongArray(DATA_KEY)));
		
		if (world != null && world.isClient) {
			world.scheduleBlockRerenderIfNeeded(getPos(), Blocks.AIR.getDefaultState(), Blocks.BEDROCK.getDefaultState());
		}
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		var nbt = new NbtCompound();
		
		writeNbt(nbt);
		
		return nbt;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public static class Connections {
		private static final int SIDE_DATA_LENGTH = 6;
		private static final int CONNECTION_DATA_LENGTH = 6;
		private static final int INSERT_DATA_LENGTH = 6;
		private static final int EXTRACT_DATA_LENGTH = 6;
		private static final int INSERT_EXTRACT_DATA_LENGTH = 6;
		
		private static final int TOTAL_LENGTH = SIDE_DATA_LENGTH + CONNECTION_DATA_LENGTH + INSERT_DATA_LENGTH + EXTRACT_DATA_LENGTH + INSERT_EXTRACT_DATA_LENGTH;
		
		private final BitSet data;
		
		public Connections() {
			this.data = new BitSet(TOTAL_LENGTH);
			
			for (var i = 0; i < TOTAL_LENGTH; ++i) {
				data.set(i, false);
			}
		}
		
		public Connections(BitSet data) {
			this.data = data;
		}
		
		public boolean hasSide(Direction direction) {
			return data.get(direction.ordinal());
		}
		
		public void setSide(Direction direction, boolean state) {
			data.set(direction.ordinal(), state);
		}
		
		public boolean hasConnector(Direction direction) {
			return data.get(direction.ordinal() + SIDE_DATA_LENGTH);
		}
		
		public void setConnection(Direction direction, boolean state) {
			data.set(direction.ordinal() + SIDE_DATA_LENGTH, state);
		}
		
		public boolean isInsert(Direction direction) {
			return data.get(direction.ordinal() + SIDE_DATA_LENGTH + CONNECTION_DATA_LENGTH);
		}
		
		public void setInsert(Direction direction, boolean state) {
			data.set(direction.ordinal() + SIDE_DATA_LENGTH + CONNECTION_DATA_LENGTH, state);
		}
		
		public boolean isExtract(Direction direction) {
			return data.get(direction.ordinal() + SIDE_DATA_LENGTH + CONNECTION_DATA_LENGTH + INSERT_DATA_LENGTH);
		}
		
		public void setExtract(Direction direction, boolean state) {
			data.set(direction.ordinal() + SIDE_DATA_LENGTH + CONNECTION_DATA_LENGTH + INSERT_DATA_LENGTH, state);
		}
		
		public boolean isInsertExtract(Direction direction) {
			return data.get(direction.ordinal() + SIDE_DATA_LENGTH + CONNECTION_DATA_LENGTH + INSERT_DATA_LENGTH + EXTRACT_DATA_LENGTH);
		}
		
		public void setInsertExtract(Direction direction, boolean state) {
			data.set(direction.ordinal() + SIDE_DATA_LENGTH + CONNECTION_DATA_LENGTH + INSERT_DATA_LENGTH + EXTRACT_DATA_LENGTH, state);
		}
		
		@Override
		protected Object clone() {
			return new Connections((BitSet) data.clone());
		}
	}
}
