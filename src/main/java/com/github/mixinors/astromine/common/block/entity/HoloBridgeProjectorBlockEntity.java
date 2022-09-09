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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.common.component.world.HoloBridgesComponent;
import com.github.mixinors.astromine.common.tick.Tickable;
import com.github.mixinors.astromine.common.util.LineUtils;
import com.github.mixinors.astromine.common.util.VectorUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HoloBridgeProjectorBlockEntity extends BlockEntity implements Tickable {
	public static final String CHILD_POSITION_KEY = "ChildPosition";
	public static final String PARENT_POSITION_KEY = "ParentPosition";
	public static final String COLOR_KEY = "Color";
	
	public static final String R_KEY = "R";
	public static final String G_KEY = "G";
	public static final String B_KEY = "B";
	public static final String A_KEY = "A";
	
	public static final Color DEFAULT_COLOR = new Color(0.5F, 0.79F, 0.83F, 0.5F);
	
	private HoloBridgeProjectorBlockEntity child = null;
	private HoloBridgeProjectorBlockEntity parent = null;
	
	private BlockPos childPosition = null;
	private BlockPos parentPosition = null;
	
	private boolean hasCheckedChild = false;
	private boolean hasCheckedParent = false;
	
	private boolean shouldInitialize = false;
	
	public List<Vec3f> segments = null;
	
	public Color color = DEFAULT_COLOR;
	
	public HoloBridgeProjectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.HOLOGRAPHIC_BRIDGE.get(), blockPos, blockState);
	}
	
	public boolean hasChild() {
		return this.child != null;
	}
	
	@Override
	public void tick() {
		if (world != null && world.isClient) {
			if (shouldInitialize) {
				this.destroyBridge();
				
				if (this.childPosition != null) {
					this.child = (HoloBridgeProjectorBlockEntity) this.world.getBlockEntity(this.childPosition);
				}
				
				this.buildBridge();
				
				shouldInitialize = false;
			}
		}
		
		if (this.world == null || this.world.isClient) {
			return;
		}
		
		if (!this.hasCheckedChild && this.childPosition != null) {
			var childEntity = this.world.getBlockEntity(this.childPosition);
			
			if (childEntity instanceof HoloBridgeProjectorBlockEntity holoChildEntity) {
				this.child = holoChildEntity;
				this.hasCheckedChild = true;
				
				this.buildBridge();
			} else if (childEntity != null) {
				this.hasCheckedChild = true;
			}
		}
		
		if (!this.hasCheckedParent && this.parentPosition != null) {
			var parentEntity = this.world.getBlockEntity(parentPosition);
			
			if (parentEntity instanceof HoloBridgeProjectorBlockEntity holoParentEntity) {
				this.parent = holoParentEntity;
				this.hasCheckedParent = true;
				
				this.buildBridge();
			} else if (parentEntity != null) {
				this.hasCheckedParent = true;
			}
		}
	}
	
	public boolean attemptToBuildBridge(HoloBridgeProjectorBlockEntity child) {
		var childPos = child.getPos();
		var pos = this.getPos();
		
		var offsetChildPos = childPos;
		
		var childFacing = child.getCachedState().get(HorizontalFacingBlock.FACING);
		
		if (childFacing == Direction.EAST) {
			offsetChildPos = offsetChildPos.add(1, 0, 0);
		} else if (childFacing == Direction.SOUTH) {
			offsetChildPos = offsetChildPos.add(0, 0, 1);
		}
		
		var distance = (int) Math.sqrt(this.getPos().getSquaredDistance(child.getPos()));
		
		if (distance == 0) {
			return false;
		}
		
		var segments = LineUtils.getBresenhamSegments(VectorUtils.toVector3f(pos.up()), VectorUtils.toVector3f(offsetChildPos.up()), 32);
		
		for (var segment : segments) {
			var segmentPos = new BlockPos(segment.getX(), segment.getY(), segment.getZ());
			
			if ((segmentPos.getX() != childPos.getX() && segmentPos.getX() != pos.getX()) || (segmentPos.getZ() != childPos.getZ() && segmentPos.getZ() != pos.getZ())) {
				if (!this.world.getBlockState(segmentPos).isAir()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void buildBridge() {
		if (this.child == null || this.world == null) {
			return;
		}
		
		var childPos = this.getChild().getPos();
		var pos = this.getPos();
		
		var offsetChildPos = childPos;
		
		var childFacing = this.getChild().getCachedState().get(HorizontalFacingBlock.FACING);
		
		if (childFacing == Direction.EAST) {
			offsetChildPos = offsetChildPos.add(1, 0, 0);
		} else if (childFacing == Direction.SOUTH) {
			offsetChildPos = offsetChildPos.add(0, 0, 1);
		}
		
		var distance = (int) Math.sqrt(this.getPos().getSquaredDistance(this.getChild().getPos()));
		
		if (distance == 0) {
			return;
		}
		
		this.segments = (ArrayList<Vec3f>) LineUtils.getBresenhamSegments(VectorUtils.toVector3f(pos.up()), VectorUtils.toVector3f(offsetChildPos.up()), 32);
		var bridgeComponent = HoloBridgesComponent.get(world);
		
		for (var segment : this.segments) {
			var segmentPos = new BlockPos(segment.getX(), segment.getY(), segment.getZ());
			
			if ((segmentPos.getX() != childPos.getX() && segmentPos.getX() != pos.getX()) || (segmentPos.getZ() != childPos.getZ() && segmentPos.getZ() != pos.getZ())) {
				if (this.world.getBlockState(segmentPos).isAir()) {
					this.world.setBlockState(segmentPos, AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get().getDefaultState());
				}
			}
			
			bridgeComponent.add(segmentPos, new Vec3i((segment.getX() - (int) segment.getX()) * 16.0F, (segment.getY() - (int) segment.getY()) * 16.0F, (segment.getZ() - (int) segment.getZ()) * 16.0F));
		}
	}
	
	public HoloBridgeProjectorBlockEntity getChild() {
		return this.child;
	}
	
	public void setChild(HoloBridgeProjectorBlockEntity child) {
		this.child = child;
		
		if (this.child != null) {
			this.child.setParent(this);
			this.child.setChild(null);
		}
		
		this.markDirty();
	}
	
	public HoloBridgeProjectorBlockEntity getParent() {
		return parent;
	}
	
	public void setParent(HoloBridgeProjectorBlockEntity parent) {
		this.parent = parent;
		this.setChild(null);
		
		this.markDirty();
	}
	
	@Override
	public void markRemoved() {
		if (this.child != null) {
			this.destroyBridge();
			
			this.setChild(null);
			
			if (!world.isClient) {
				BlockEntityHooks.syncData(this);
			}
		}
		
		if (this.parent != null) {
			this.parent.destroyBridge();
			
			this.parent.setChild(null);
			
			if (!world.isClient) {
				BlockEntityHooks.syncData(this.parent);
			}
		}
		
		
		super.markRemoved();
	}
	
	public void destroyBridge() {
		if (this.segments != null && this.world != null) {
			var bridgeComponent = HoloBridgesComponent.get(world);
			
			for (var vec : this.segments) {
				var pos = new BlockPos(vec.getX(), vec.getY(), vec.getZ());
				
				bridgeComponent.remove(pos);
				
				this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			this.segments.clear();
		}
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		if (nbt.contains(CHILD_POSITION_KEY)) {
			this.childPosition = BlockPos.fromLong(nbt.getLong(CHILD_POSITION_KEY));
		}
		
		if (nbt.contains(PARENT_POSITION_KEY)) {
			this.parentPosition = BlockPos.fromLong(nbt.getLong(PARENT_POSITION_KEY));
		}
		
		if (nbt.contains(COLOR_KEY)) {
			var colorTag = nbt.getCompound(COLOR_KEY);
			
			color = new Color(
					colorTag.getFloat(R_KEY),
					colorTag.getFloat(G_KEY),
					colorTag.getFloat(B_KEY),
					colorTag.getFloat(A_KEY)
			);
		}
		
		shouldInitialize = true;
		
		super.readNbt(nbt);
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		if (this.child != null) {
			nbt.putLong(CHILD_POSITION_KEY, this.child.getPos().asLong());
		} else if (this.childPosition != null) {
			nbt.putLong(CHILD_POSITION_KEY, this.childPosition.asLong());
		}
		
		if (this.parent != null) {
			nbt.putLong(PARENT_POSITION_KEY, this.parent.getPos().asLong());
		} else if (this.parentPosition != null) {
			nbt.putLong(PARENT_POSITION_KEY, this.parentPosition.asLong());
		}
		
		var colorTag = new NbtCompound();
		colorTag.putFloat(R_KEY, color.getR());
		colorTag.putFloat(G_KEY, color.getG());
		colorTag.putFloat(B_KEY, color.getB());
		colorTag.putFloat(A_KEY, color.getA());
		
		nbt.put(COLOR_KEY, colorTag);
		
		super.writeNbt(nbt);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}
