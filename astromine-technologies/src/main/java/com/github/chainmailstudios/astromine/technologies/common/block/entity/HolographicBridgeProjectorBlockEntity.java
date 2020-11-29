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

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.common.utilities.VectorUtilities;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import com.github.vini2003.blade.common.miscellaneous.Color;
import com.mojang.math.Vector3f;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HolographicBridgeProjectorBlockEntity extends BlockEntity implements TickableBlockEntity, BlockEntityClientSerializable {
	public ArrayList<Vector3f> segments = null;

	public Color color = Color.of("0x7e80cad4");

	private HolographicBridgeProjectorBlockEntity child = null;
	private HolographicBridgeProjectorBlockEntity parent = null;

	private BlockPos childPosition = null;
	private BlockPos parentPosition = null;

	private boolean hasCheckedChild = false;
	private boolean hasCheckedParent = false;

	public HolographicBridgeProjectorBlockEntity() {
		super(AstromineTechnologiesBlockEntityTypes.HOLOGRAPHIC_BRIDGE);
	}

	public boolean hasChild() {
		return this.child != null;
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide)
			return;

		if (!this.hasCheckedChild && this.childPosition != null) {
			BlockEntity childEntity = this.level.getBlockEntity(this.childPosition);

			if (childEntity instanceof HolographicBridgeProjectorBlockEntity) {
				this.child = (HolographicBridgeProjectorBlockEntity) childEntity;
				this.hasCheckedChild = true;

				this.buildBridge();
			} else if (childEntity != null) {
				this.hasCheckedChild = true;
			}
		}

		if (!this.hasCheckedParent && this.parentPosition != null) {
			BlockEntity parentEntity = this.level.getBlockEntity(parentPosition);

			if (parentEntity instanceof HolographicBridgeProjectorBlockEntity) {
				this.parent = (HolographicBridgeProjectorBlockEntity) parentEntity;
				this.hasCheckedParent = true;

				this.buildBridge();
			} else if (parentEntity != null) {
				this.hasCheckedParent = true;
			}
		}
	}

	public void buildBridge() {
		if (this.child == null || this.level == null) {
			return;
		}

		BlockPos bCP = this.getChild().getBlockPos();
		BlockPos bOP = this.getBlockPos();

		BlockPos nCP = bCP;

		Direction cD = this.getChild().getBlockState().getValue(HorizontalDirectionalBlock.FACING);

		if (cD == Direction.EAST) {
			nCP = nCP.offset(1, 0, 0);
		} else if (cD == Direction.SOUTH) {
			nCP = nCP.offset(0, 0, 1);
		}

		int distance = (int) Math.sqrt(this.getBlockPos().distSqr(this.getChild().getBlockPos()));

		if (distance == 0) {
			return;
		}

		this.segments = (ArrayList<Vector3f>) LineUtilities.getBresenhamSegments(VectorUtilities.toVector3f(bOP.relative(Direction.UP)), VectorUtilities.toVector3f(nCP.relative(Direction.UP)), 32);

		for (Vector3f v : this.segments) {
			BlockPos nP = new BlockPos(v.x(), v.y(), v.z());

			if ((nP.getX() != bCP.getX() && nP.getX() != bOP.getX()) || (nP.getZ() != bCP.getZ() && nP.getZ() != bOP.getZ())) {
				this.level.setBlockAndUpdate(nP, AstromineTechnologiesBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.defaultBlockState());
			}

			WorldBridgeComponent bridgeComponent = WorldBridgeComponent.get(level);

			bridgeComponent.add(nP, new Vec3i((v.x() - (int) v.x()) * 16f, (v.y() - (int) v.y()) * 16f, (v.z() - (int) v.z()) * 16f));
		}
	}

	public HolographicBridgeProjectorBlockEntity getChild() {
		return this.child;
	}

	public void setChild(HolographicBridgeProjectorBlockEntity child) {
		this.child = child;

		if (this.child != null) {
			this.child.setParent(this);
			this.child.setChild(null);
		}

		this.setChanged();
	}

	public HolographicBridgeProjectorBlockEntity getParent() {
		return parent;
	}

	public void setParent(HolographicBridgeProjectorBlockEntity parent) {
		this.parent = parent;
		this.setChild(null);

		this.setChanged();
	}

	@Override
	public double getViewDistance() {
		return Math.pow(2, 15);
	}

	@Override
	public void setRemoved() {
		if (this.child != null) {
			this.destroyBridge();

			this.setChild(null);

			if (!level.isClientSide) {
				this.sync();
			}
		}

		if (this.parent != null) {
			this.parent.destroyBridge();

			this.parent.setChild(null);

			if (!level.isClientSide) {
				this.parent.sync();
			}
		}


		super.setRemoved();
	}

	public void destroyBridge() {
		if (this.segments != null && this.level != null) {
			WorldBridgeComponent bridgeComponent = WorldBridgeComponent.get(level);

			for (Vector3f vec : this.segments) {
				BlockPos pos = new BlockPos(vec.x(), vec.y(), vec.z());

				bridgeComponent.remove(pos);

				this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}

			this.segments.clear();
		}
	}

	@Override
	public void load(BlockState state, @NotNull CompoundTag tag) {
		if (tag.contains("child_position")) {
			this.childPosition = BlockPos.of(tag.getLong("child_position"));
		}

		if (tag.contains("parent_position")) {
			this.parentPosition = BlockPos.of(tag.getLong("parent_position"));
		}

		if (tag.contains("color")) {
			CompoundTag colorTag = tag.getCompound("color");

			color = new Color(colorTag.getFloat("r"), colorTag.getFloat("g"), colorTag.getFloat("b"), colorTag.getFloat("a"));
		}

		super.load(state, tag);
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		if (this.child != null) {
			tag.putLong("child_position", this.child.getBlockPos().asLong());
		} else if (this.childPosition != null) {
			tag.putLong("child_position", this.childPosition.asLong());
		}

		if (this.parent != null) {
			tag.putLong("parent_position", this.parent.getBlockPos().asLong());
		} else if (this.parentPosition != null) {
			tag.putLong("parent_position", this.parentPosition.asLong());
		}

		CompoundTag colorTag = new CompoundTag();
		colorTag.putFloat("r", color.getR());
		colorTag.putFloat("g", color.getG());
		colorTag.putFloat("b", color.getB());
		colorTag.putFloat("a", color.getA());

		tag.put("color", colorTag);

		return super.save(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.load(null, tag);

		this.destroyBridge();

		if (this.childPosition != null) {
			this.child = (HolographicBridgeProjectorBlockEntity) this.level.getBlockEntity(this.childPosition);
		}

		this.buildBridge();
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return this.save(compoundTag);
	}
}
