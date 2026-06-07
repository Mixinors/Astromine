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
import com.github.mixinors.astromine.common.util.Color;
import org.joml.Vector3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
	
	public List<Vector3f> segments = null;
	
	public Color color = DEFAULT_COLOR;
	
	public HoloBridgeProjectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.HOLOGRAPHIC_BRIDGE.get(), blockPos, blockState);
	}
	
	public boolean hasChild() {
		return this.child != null;
	}
	
	@Override
	public void tick() {
		if (level != null && level.isClientSide) {
			if (shouldInitialize) {
				this.destroyBridge();
				
				if (this.childPosition != null) {
					this.child = (HoloBridgeProjectorBlockEntity) this.level.getBlockEntity(this.childPosition);
				}
				
				this.buildBridge();
				
				shouldInitialize = false;
			}
		}
		
		if (this.level == null || this.level.isClientSide) {
			return;
		}
		
		if (!this.hasCheckedChild && this.childPosition != null) {
			var childEntity = this.level.getBlockEntity(this.childPosition);
			
			if (childEntity instanceof HoloBridgeProjectorBlockEntity holoChildEntity) {
				this.child = holoChildEntity;
				this.hasCheckedChild = true;
				
				this.buildBridge();
			} else if (childEntity != null) {
				this.hasCheckedChild = true;
			}
		}
		
		if (!this.hasCheckedParent && this.parentPosition != null) {
			var parentEntity = this.level.getBlockEntity(parentPosition);
			
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
		var childPos = child.getBlockPos();
		var pos = this.getBlockPos();
		
		var offsetChildPos = childPos;
		
		var childFacing = child.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		
		if (childFacing == Direction.EAST) {
			offsetChildPos = offsetChildPos.offset(1, 0, 0);
		} else if (childFacing == Direction.SOUTH) {
			offsetChildPos = offsetChildPos.offset(0, 0, 1);
		}
		
		var distance = (int) Math.sqrt(this.getBlockPos().distSqr(child.getBlockPos()));
		
		if (distance == 0) {
			return false;
		}
		
		var segments = LineUtils.getBresenhamSegments(VectorUtils.toVector3f(pos.above()), VectorUtils.toVector3f(offsetChildPos.above()), 32);
		
		for (var segment : segments) {
			var segmentPos = BlockPos.containing(segment.x(), segment.y(), segment.z());
			
			if ((segmentPos.getX() != childPos.getX() && segmentPos.getX() != pos.getX()) || (segmentPos.getZ() != childPos.getZ() && segmentPos.getZ() != pos.getZ())) {
				if (!this.level.getBlockState(segmentPos).isAir()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void buildBridge() {
		if (this.child == null || this.level == null) {
			return;
		}
		
		var childPos = this.getChild().getBlockPos();
		var pos = this.getBlockPos();
		
		var offsetChildPos = childPos;
		
		var childFacing = this.getChild().getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		
		if (childFacing == Direction.EAST) {
			offsetChildPos = offsetChildPos.offset(1, 0, 0);
		} else if (childFacing == Direction.SOUTH) {
			offsetChildPos = offsetChildPos.offset(0, 0, 1);
		}
		
		var distance = (int) Math.sqrt(this.getBlockPos().distSqr(this.getChild().getBlockPos()));
		
		if (distance == 0) {
			return;
		}
		
		this.segments = (ArrayList<Vector3f>) LineUtils.getBresenhamSegments(VectorUtils.toVector3f(pos.above()), VectorUtils.toVector3f(offsetChildPos.above()), 32);
		var bridgeComponent = HoloBridgesComponent.get(level);
		
		for (var segment : this.segments) {
			var segmentPos = BlockPos.containing(segment.x(), segment.y(), segment.z());
			
			if ((segmentPos.getX() != childPos.getX() && segmentPos.getX() != pos.getX()) || (segmentPos.getZ() != childPos.getZ() && segmentPos.getZ() != pos.getZ())) {
				if (this.level.getBlockState(segmentPos).isAir()) {
					this.level.setBlockAndUpdate(segmentPos, AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get().defaultBlockState());
				}
			}
			
			bridgeComponent.add(segmentPos, new Vec3i((int) ((segment.x() - (int) segment.x()) * 16.0F), (int) ((segment.y() - (int) segment.y()) * 16.0F), (int) ((segment.z() - (int) segment.z()) * 16.0F)));
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
		
		this.setChanged();
	}
	
	public HoloBridgeProjectorBlockEntity getParent() {
		return parent;
	}
	
	public void setParent(HoloBridgeProjectorBlockEntity parent) {
		this.parent = parent;
		this.setChild(null);
		
		this.setChanged();
	}
	
	@Override
	public void setRemoved() {
		if (this.child != null) {
			this.destroyBridge();
			
			this.setChild(null);
			
			if (!level.isClientSide) {
				syncData();
			}
		}
		
		if (this.parent != null) {
			this.parent.destroyBridge();
			
			this.parent.setChild(null);
			
			if (!level.isClientSide) {
				this.parent.syncData();
			}
		}
		
		
		super.setRemoved();
	}
	
	public void destroyBridge() {
		if (this.segments != null && this.level != null) {
			var bridgeComponent = HoloBridgesComponent.get(level);
			
			for (var vec : this.segments) {
				var pos = BlockPos.containing(vec.x(), vec.y(), vec.z());
				
				bridgeComponent.remove(pos);
				
				this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
			
			this.segments.clear();
		}
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		if (nbt.contains(CHILD_POSITION_KEY)) {
			this.childPosition = BlockPos.of(nbt.getLong(CHILD_POSITION_KEY));
		}
		
		if (nbt.contains(PARENT_POSITION_KEY)) {
			this.parentPosition = BlockPos.of(nbt.getLong(PARENT_POSITION_KEY));
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
		
		super.loadAdditional(nbt, registries);
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		if (this.child != null) {
			nbt.putLong(CHILD_POSITION_KEY, this.child.getBlockPos().asLong());
		} else if (this.childPosition != null) {
			nbt.putLong(CHILD_POSITION_KEY, this.childPosition.asLong());
		}
		
		if (this.parent != null) {
			nbt.putLong(PARENT_POSITION_KEY, this.parent.getBlockPos().asLong());
		} else if (this.parentPosition != null) {
			nbt.putLong(PARENT_POSITION_KEY, this.parentPosition.asLong());
		}
		
		var colorTag = new CompoundTag();
		colorTag.putFloat(R_KEY, color.getR());
		colorTag.putFloat(G_KEY, color.getG());
		colorTag.putFloat(B_KEY, color.getB());
		colorTag.putFloat(A_KEY, color.getA());
		
		nbt.put(COLOR_KEY, colorTag);
		
		super.saveAdditional(nbt, registries);
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveWithoutMetadata(registries);
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public void syncData() {
		setChanged();
		
		if (level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), net.minecraft.world.level.block.Block.UPDATE_CLIENTS);
		}
	}
}
