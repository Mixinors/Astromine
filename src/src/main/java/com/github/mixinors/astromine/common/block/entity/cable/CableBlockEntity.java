package com.github.mixinors.astromine.common.block.entity.cable;

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public class CableBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity {
	private Connections connections = new Connections();
	
	public CableBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.CABLE.get(), blockPos, blockState);
	}
	
	public Connections getConnections() {
		return connections;
	}
	
	@Override
	public @Nullable Object getRenderAttachmentData() {
		return connections.clone();
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
		
		world.scheduleBlockRerenderIfNeeded(getPos(), Blocks.AIR.getDefaultState(), Blocks.BEDROCK.getDefaultState());
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
		private final BitSet data;
		
		public Connections() {
			this.data = new BitSet(12);
			for (var i = 0; i < 12; ++i) {
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
			return data.get(6 + direction.ordinal());
		}
		
		public void setConnection(Direction direction, boolean state) {
			data.set(direction.ordinal() + 6, state);
		}
		
		@Override
		protected Object clone() {
			return new Connections((BitSet) data.clone());
		}
	}
}
