package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.bridge.HolographicBridgeManager;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import spinnery.widget.api.Color;

import java.util.ArrayList;

public class HolographicBridgeProjectorBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	public ArrayList<Vector3f> segments = null;
	public ArrayList<Vec3i> members = null;

	private HolographicBridgeProjectorBlockEntity child = null;
	private HolographicBridgeProjectorBlockEntity parent = null;

	private BlockPos childPosition = null;
	private BlockPos parentPosition = null;

	private boolean hasCheckedChild = false;
	private boolean hasCheckedParent = false;

	public Direction direction = Direction.NORTH;

	public Color color = Color.of(0x7e2fd3da);

	public long last = 0;

	public HolographicBridgeProjectorBlockEntity() {
		super(AstromineBlockEntities.HOLOGRAPHIC_BRIDGE);
	}

	public HolographicBridgeProjectorBlockEntity getChild() {
		return child;
	}

	public void setChild(HolographicBridgeProjectorBlockEntity child) {
		if (child != null && child.parent != null) {
			child.parent.destroyBridge();
		}

		this.child = child;
		markDirty();
	}

	public boolean hasChild() {
		return this.child != null;
	}

	public HolographicBridgeProjectorBlockEntity getParent() {
		return parent;
	}

	public void setParent(HolographicBridgeProjectorBlockEntity parent) {
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

		segments = (ArrayList<Vector3f>) LineUtilities.getBezierSegments(
				new Vector3f(bOP.getX(), bOP.getY() + 1, bOP.getZ()),
				new Vector3f(nCP.getX(), nCP.getY() + 1, nCP.getZ()),
				new Vector3f(nCP.getX(), (bOP.getY() + nCP.getY()) / 2f, bCP.getZ()), distance * 16);

		members = new ArrayList<>();

		for (Vector3f v : segments) {
			BlockPos nP = new BlockPos(v.getX(), v.getY(), v.getZ());

			world.setBlockState(nP, AstromineBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.getDefaultState());

			HolographicBridgeManager.add(world, direction, nP, new Vec3i(
					(v.getX() - (int) v.getX()) * 16f,
					(v.getY() - (int) v.getY()) * 16f,
					(v.getZ() - (int) v.getZ()) * 16f
			));

			members.add(nP);
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

				if (childEntity instanceof HolographicBridgeProjectorBlockEntity) {
					this.child = (HolographicBridgeProjectorBlockEntity) childEntity;
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

				if (parentEntity instanceof HolographicBridgeProjectorBlockEntity) {
					this.parent = (HolographicBridgeProjectorBlockEntity) parentEntity;
					this.parent.setChild(this);
					this.parent.buildBridge();
					hasCheckedParent = true;
				} else if (parentEntity != null) {
					hasCheckedParent = true;
				}
			}
		}

		long current = System.currentTimeMillis();

		if (current - last >= 11187) {
			world.playSound(getPos().getX(), getPos().getY(), getPos().getZ(), AstromineSounds.HUMMING, SoundCategory.BLOCKS, 0.005f, 0.5f, true);
			last = current;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (child != null) tag.putLong("child_position", child.getPos().asLong());
		if (parent != null) tag.putLong("parent_position", parent.getPos().asLong());

		tag.putInt("direction", direction.getId());
		tag.putInt("color", color.ARGB);

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		if (tag.contains("child_position")) childPosition = BlockPos.fromLong(tag.getLong("child_position"));
		if (tag.contains("parent_position")) parentPosition = BlockPos.fromLong(tag.getLong("parent_position"));

		direction = Direction.byId(tag.getInt("direction"));
		color = Color.of(tag.getInt("color"));

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
