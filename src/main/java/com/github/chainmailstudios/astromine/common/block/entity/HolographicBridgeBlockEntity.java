package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.bridge.HolographicBridgeManager;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
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
import java.util.UUID;

public class HolographicBridgeBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	public ArrayList<Vector3f> segments = null;
	public ArrayList<Vec3i> members = null;

	private HolographicBridgeBlockEntity child = null;
	private HolographicBridgeBlockEntity parent = null;

	private BlockPos childPosition = null;
	private BlockPos parentPosition = null;

	private boolean hasCheckedChild = false;
	private boolean hasCheckedParent = false;

	public HolographicBridgeBlockEntity() {
		super(AstromineBlockEntities.HOLOGRAPHIC_BRIDGE);
	}

	public HolographicBridgeBlockEntity getChild() {
		return child;
	}

	public void setChild(HolographicBridgeBlockEntity child) {
		if (child != null && child.parent != null) {
			child.parent.destroyBridge();
		}

		this.child = child;
		markDirty();
	}

	public boolean hasChild() {
		return this.child != null;
	}

	public HolographicBridgeBlockEntity getParent() {
		return parent;
	}

	public void setParent(HolographicBridgeBlockEntity parent) {
		if (parent != null) {
			parent.destroyBridge();
		}

		this.parent = parent;
		markDirty();
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	public void buildBridge() {
		if (child == null || world == null) return;

		BlockPos bCP = getChild().getPos();
		BlockPos bOP = getPos();

		BlockPos nCP = bCP;

		Direction cD = getChild().getCachedState().get(HorizontalFacingBlock.FACING);

		if (cD == Direction.EAST) {
			nCP = nCP.add(1, 0, 0);
		} else if (cD == Direction.SOUTH) {
			nCP = nCP.add(0, 0, 1);
		}

		int distance = (int) Math.sqrt(getPos().getSquaredDistance(getChild().getPos()));

		if (distance == 0) return;

		segments = (ArrayList<Vector3f>) LineUtilities.getBezierSegments(bOP.add(0, 1, 0), nCP.add(0, 1, 0), distance * 2);

		members = new ArrayList<>();

		Vector3f o = segments.get(0);

		BlockPos oP = getPos();

		float oF = 0;

		for (Vector3f v : segments) {
			if ((bOP.getX() != v.getX() || bOP.getZ() != v.getZ()) && (bCP.getX() != v.getX() || bCP.getZ() != v.getZ())) {
				BlockPos nP = new BlockPos(v.getX(), Math.min(bCP.getY(), v.getY()), v.getZ());

				float f;

				if (o.getY() >= v.getY()) {
					f = o.getY() - v.getY();
				} else {
					f = v.getY() - o.getY();
				}

				if (!nP.equals(oP)) {
					oP = nP;

					oF = f;

					HolographicBridgeManager.add(world, nP, (int) (16f * f));

					world.setBlockState(nP, AstromineBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.getDefaultState());

					members.add(nP);
				}
			}
		}
	}

	public void destroyBridge() {
		if (segments != null && world != null) {
			for (Vec3i v : members) {
				HolographicBridgeManager.remove(world, (BlockPos) v);

				world.setBlockState((BlockPos) v, Blocks.AIR.getDefaultState());
			}
		}
	}

	@Override
	public void markRemoved() {
		if (child != null) {
			child.setParent(null);
			this.destroyBridge();

			setChild(null);
		}

		if (parent != null) {
			parent.setChild(null);
			parent.destroyBridge();

			setParent(null);
		}

		super.markRemoved();
	}

	@Override
	public double getSquaredRenderDistance() {
		return Math.pow(2, 15);
	}

	@Override
	public void tick() {
		if (!hasCheckedChild && childPosition != null) {
			if (world != null) {
				BlockEntity childEntity = world.getBlockEntity(childPosition);

				if (childEntity instanceof HolographicBridgeBlockEntity) {
					this.child = (HolographicBridgeBlockEntity) childEntity;
					this.child.setParent(this);
					this.buildBridge();
					hasCheckedChild = true;
				} else if (childEntity != null) {
					hasCheckedChild = true;
				}
			}
		}

		if (!hasCheckedParent && parentPosition != null) {
			if (world != null) {
				BlockEntity parentEntity = world.getBlockEntity(parentPosition);

				if (parentEntity instanceof HolographicBridgeBlockEntity) {
					this.parent = (HolographicBridgeBlockEntity) parentEntity;
					this.parent.setChild(this);
					this.parent.buildBridge();
					hasCheckedParent = true;
				} else if (parentEntity != null) {
					hasCheckedParent = true;
				}
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (child != null) tag.putLong("child_position", child.getPos().asLong());
		if (parent != null) tag.putLong("parent_position", parent.getPos().asLong());

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		if (tag.contains("child_position")) childPosition = BlockPos.fromLong(tag.getLong("child_position"));
		if (tag.contains("parent_position")) parentPosition = BlockPos.fromLong(tag.getLong("parent_position"));

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(null, compoundTag);
	}
}
