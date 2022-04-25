package com.github.mixinors.astromine.common.block.entity.cable;

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public class CableBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity {
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
		
		nbt.putLongArray("Data", connections.data.toLongArray());
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		connections = new Connections(BitSet.valueOf(nbt.getLongArray("Data")));
		
		if (world != null && world.isClient()) {
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
