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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public class CableBlockEntity extends BlockEntity {
	public static final String DATA_KEY = "Data";
	public static final ModelProperty<Connections> CONNECTIONS = new ModelProperty<>();
	
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
	public ModelData getModelData() {
		return ModelData.of(CONNECTIONS, connections);
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		
		nbt.putLongArray(DATA_KEY, connections.data.toLongArray());
	}
	
	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		
		connections = new Connections(BitSet.valueOf(nbt.getLongArray(DATA_KEY)));
		
		if (level != null && level.isClientSide) {
			requestModelDataUpdate();
			level.setBlocksDirty(getBlockPos(), Blocks.AIR.defaultBlockState(), Blocks.BEDROCK.defaultBlockState());
		}
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var nbt = new CompoundTag();
		
		saveAdditional(nbt, registries);
		
		return nbt;
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public void syncData() {
		setChanged();
		
		if (level != null) {
			requestModelDataUpdate();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
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
