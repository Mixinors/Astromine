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
		if (child == null) return;

		BlockPos childPosition = getChild().getPos();
		BlockPos ourPosition = getPos();

		Direction childDirection = getChild().getCachedState().get(HorizontalFacingBlock.FACING);
		Direction ourDirection = getCachedState().get(HorizontalFacingBlock.FACING);

		if (childDirection == Direction.EAST) {
			childPosition = childPosition.add(1, 0, 0);
		} else if (childDirection == Direction.SOUTH) {
			childPosition = childPosition.add(0, 0, 1);
		}

		int distance = (int) Math.sqrt(getPos().getSquaredDistance(getChild().getPos()));

		if (distance == 0) return;

		segments = (ArrayList<Vector3f>) LineUtilities.getBezierSegments(ourPosition, childPosition, distance * 5);

		Vector3f origin = segments.get(0);

		Vector3f previous = origin;

		for (int k = 0; k < segments.size(); ++k) {
			Vector3f vector = segments.get(k);

			Vec3i vec3iA = new Vec3i(ourPosition.getX(), ourPosition.getY(), ourPosition.getZ());
			Vec3i vec3iB = new Vec3i(childPosition.getX(), childPosition.getY(), childPosition.getZ());
			Vec3i vec3iC = new Vec3i(vector.getX(), vector.getY(), vector.getZ());


				BlockPos newPosition = new BlockPos(vector.getX(), vector.getY() + 1, vector.getZ());

				float percentage;

				if (origin.getY() - previous.getY() > 1) {
					percentage = previous.getY() + 1 - previous.getY();
				} else {
					percentage = origin.getY() - previous.getY();
				}

				HolographicBridgeManager.add(world, newPosition, (int) Math.ceil(16f * Math.max(0.0625, percentage)));

				world.setBlockState(newPosition, AstromineBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.getDefaultState());

			previous = vector;
		}

	}

	public void destroyBridge() {
		if (segments != null) {
			Vector3f origin = segments.get(0);
			Vector3f end = segments.get(segments.size() - 1);

			for (int k = 0; k < segments.size(); ++k) {
				Vector3f vector = segments.get(k);

				Vec3i vec3iA = new Vec3i(origin.getX(), origin.getY(), origin.getZ());
				Vec3i vec3iB = new Vec3i(end.getX(), end.getY(), end.getZ());
				Vec3i vec3iC = new Vec3i(vector.getX(), vector.getY(), vector.getZ());

				if ((vec3iA.getX() == vec3iC.getX() && vec3iA.getZ() == vec3iC.getZ()) || (vec3iB.getX() == vec3iC.getX() && vec3iB.getZ() == vec3iC.getZ()))
					continue;
				else {
					BlockPos newPosition = new BlockPos(vector.getX(), vector.getY() + 1, vector.getZ());

					world.setBlockState(newPosition, Blocks.AIR.getDefaultState());
				}
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
