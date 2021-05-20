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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.common.component.world.WorldHoloBridgeComponent;
import com.github.mixinors.astromine.common.util.LineUtils;
import com.github.mixinors.astromine.common.util.VectorUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.vini2003.blade.common.miscellaneous.Color;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HoloBridgeProjectorBlockEntity extends BlockEntity implements Tickable, BlockEntityExtension {
	public ArrayList<Vector3f> segments = null;

	public Color color = Color.of("0x7e80cad4");

	private HoloBridgeProjectorBlockEntity child = null;
	private HoloBridgeProjectorBlockEntity parent = null;

	private BlockPos childPosition = null;
	private BlockPos parentPosition = null;

	private boolean hasCheckedChild = false;
	private boolean hasCheckedParent = false;

	public HoloBridgeProjectorBlockEntity() {
		super(AMBlockEntityTypes.HOLOGRAPHIC_BRIDGE.get());
	}

	public boolean hasChild() {
		return this.child != null;
	}

	@Override
	public void tick() {
		if (this.world == null || this.world.isClient)
			return;

		if (!this.hasCheckedChild && this.childPosition != null) {
			var childEntity = this.world.getBlockEntity(this.childPosition);

			if (childEntity instanceof HoloBridgeProjectorBlockEntity projectorBlockEntity) {
				this.child = projectorBlockEntity;
				this.hasCheckedChild = true;

				this.buildBridge();
			} else if (childEntity != null) {
				this.hasCheckedChild = true;
			}
		}

		if (!this.hasCheckedParent && this.parentPosition != null) {
			var parentEntity = this.world.getBlockEntity(parentPosition);

			if (parentEntity instanceof HoloBridgeProjectorBlockEntity projectorBlockEntity) {
				this.parent = projectorBlockEntity;
				this.hasCheckedParent = true;

				this.buildBridge();
			} else if (parentEntity != null) {
				this.hasCheckedParent = true;
			}
		}
	}

	public boolean attemptToBuildBridge(HoloBridgeProjectorBlockEntity child) {
		var bCP = child.getPos();
		var bOP = this.getPos();

		var nCP = bCP;

		var cD = child.getCachedState().get(HorizontalFacingBlock.FACING);

		if (cD == Direction.EAST) {
			nCP = nCP.add(1, 0, 0);
		} else if (cD == Direction.SOUTH) {
			nCP = nCP.add(0, 0, 1);
		}

		var distance = (int) Math.sqrt(this.getPos().getSquaredDistance(child.getPos()));

		if (distance == 0) {
			return false;
		}

		var segments = (ArrayList<Vector3f>) LineUtils.getBresenhamSegments(VectorUtils.toVector3f(bOP.offset(Direction.UP)), VectorUtils.toVector3f(nCP.offset(Direction.UP)), 32);

		for (var v : segments) {
			var nP = new BlockPos(v.getX(), v.getY(), v.getZ());

			if ((nP.getX() != bCP.getX() && nP.getX() != bOP.getX()) || (nP.getZ() != bCP.getZ() && nP.getZ() != bOP.getZ())) {
				if (!this.world.getBlockState(nP).isAir()) {
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

		var bCP = this.getChild().getPos();
		var bOP = this.getPos();

		var nCP = bCP;

		var cD = this.getChild().getCachedState().get(HorizontalFacingBlock.FACING);

		if (cD == Direction.EAST) {
			nCP = nCP.add(1, 0, 0);
		} else if (cD == Direction.SOUTH) {
			nCP = nCP.add(0, 0, 1);
		}

		int distance = (int) Math.sqrt(this.getPos().getSquaredDistance(this.getChild().getPos()));

		if (distance == 0) {
			return;
		}

		this.segments = (ArrayList<Vector3f>) LineUtils.getBresenhamSegments(VectorUtils.toVector3f(bOP.offset(Direction.UP)), VectorUtils.toVector3f(nCP.offset(Direction.UP)), 32);
		var bridgeComponent = WorldHoloBridgeComponent.from(world);

		for (var v : this.segments) {
			var nP = new BlockPos(v.getX(), v.getY(), v.getZ());

			if ((nP.getX() != bCP.getX() && nP.getX() != bOP.getX()) || (nP.getZ() != bCP.getZ() && nP.getZ() != bOP.getZ())) {
				if (this.world.getBlockState(nP).isAir()) {
					this.world.setBlockState(nP, AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get().getDefaultState());
				}
			}
			
			bridgeComponent.add(nP, new Vec3i((v.getX() - (int) v.getX()) * 16f, (v.getY() - (int) v.getY()) * 16f, (v.getZ() - (int) v.getZ()) * 16f));
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
	public double getSquaredRenderDistance() {
		return Math.pow(2, 15);
	}

	@Override
	public void markRemoved() {
		if (this.child != null) {
			this.destroyBridge();

			this.setChild(null);

			if (!world.isClient) {
				this.syncData();
			}
		}

		if (this.parent != null) {
			this.parent.destroyBridge();

			this.parent.setChild(null);

			if (!world.isClient) {
				this.parent.syncData();
			}
		}


		super.markRemoved();
	}

	public void destroyBridge() {
		if (this.segments != null && this.world != null) {
			var bridgeComponent = WorldHoloBridgeComponent.from(world);

			for (var vec : this.segments) {
				var pos = new BlockPos(vec.getX(), vec.getY(), vec.getZ());

				bridgeComponent.remove(pos);

				this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}

			this.segments.clear();
		}
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		if (tag.contains("ChildPosition")) {
			this.childPosition = BlockPos.fromLong(tag.getLong("ChildPosition"));
		}

		if (tag.contains("ParentPosition")) {
			this.parentPosition = BlockPos.fromLong(tag.getLong("ParentPosition"));
		}

		if (tag.contains("Color")) {
			var colorTag = tag.getCompound("Color");

			color = new Color(colorTag.getFloat("Red"), colorTag.getFloat("Green"), colorTag.getFloat("Blue"), colorTag.getFloat("Alpha"));
		}

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (this.child != null) {
			tag.putLong("ChildPosition", this.child.getPos().asLong());
		} else if (this.childPosition != null) {
			tag.putLong("ChildPosition", this.childPosition.asLong());
		}

		if (this.parent != null) {
			tag.putLong("ParentPosition", this.parent.getPos().asLong());
		} else if (this.parentPosition != null) {
			tag.putLong("ParentPosition", this.parentPosition.asLong());
		}

		var colorTag = new CompoundTag();
		colorTag.putFloat("Red", color.getR());
		colorTag.putFloat("Green", color.getG());
		colorTag.putFloat("Blue", color.getB());
		colorTag.putFloat("Alpha", color.getA());

		tag.put("Color", colorTag);

		return super.toTag(tag);
	}

	@Override
	public void loadClientData(BlockState state, CompoundTag tag) {
		this.fromTag(state, tag);

		this.destroyBridge();

		if (this.childPosition != null) {
			this.child = (HoloBridgeProjectorBlockEntity) this.world.getBlockEntity(this.childPosition);
		}

		this.buildBridge();
	}

	@Override
	public CompoundTag saveClientData(CompoundTag compoundTag) {
		return this.toTag(compoundTag);
	}
}
