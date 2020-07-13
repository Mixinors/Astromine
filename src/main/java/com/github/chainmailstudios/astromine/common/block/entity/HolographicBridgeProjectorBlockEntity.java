package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.utilities.VectorUtilities;
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

import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import org.jetbrains.annotations.NotNull;
import spinnery.widget.api.Color;

import java.util.ArrayList;

public class HolographicBridgeProjectorBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	public ArrayList<Vector3f> segments = null;

	public Color color = Color.of(0x7e80cad4);

	private HolographicBridgeProjectorBlockEntity child = null;
	private HolographicBridgeProjectorBlockEntity parent = null;

	private BlockPos childPosition = null;

	private boolean hasCheckedChild = false;

	public HolographicBridgeProjectorBlockEntity() {
		super(AstromineBlockEntityTypes.HOLOGRAPHIC_BRIDGE);
	}

	public boolean hasChild() {
		return this.child != null;
	}

	@Override
	public void tick() {
		if (this.world == null || this.world.isClient) return;
		if (!this.hasCheckedChild && this.childPosition != null) {
			BlockEntity childEntity = this.world.getBlockEntity(this.childPosition);

			if (childEntity instanceof HolographicBridgeProjectorBlockEntity) {
				this.child = (HolographicBridgeProjectorBlockEntity) childEntity;
				this.hasCheckedChild = true;

				this.buildBridge();
			} else if (childEntity != null) {
				this.hasCheckedChild = true;
			}
		}
	}

	public void buildBridge() {
		if (this.child == null || this.world == null) {
			return;
		}

		BlockPos bCP = this.getChild().getPos();
		BlockPos bOP = this.getPos();

		BlockPos nCP = bCP;

		Direction cD = this.getChild().getCachedState().get(HorizontalFacingBlock.FACING);

		if (cD == Direction.EAST) {
			nCP = nCP.add(1, 0, 0);
		} else if (cD == Direction.SOUTH) {
			nCP = nCP.add(0, 0, 1);
		}

		int distance = (int) Math.sqrt(this.getPos().getSquaredDistance(this.getChild().getPos()));

		if (distance == 0) {
			return;
		}

		this.segments = (ArrayList<Vector3f>) LineUtilities.getBresenhamSegments(VectorUtilities.toVector3f(bOP.offset(Direction.UP)), VectorUtilities.toVector3f(nCP.offset(Direction.UP)), 32);

		for (Vector3f v : this.segments) {
			BlockPos nP = new BlockPos(v.getX(), v.getY(), v.getZ());

			if ((nP.getX() != bCP.getX() && nP.getX() != bOP.getX()) || (nP.getZ() != bCP.getZ() && nP.getZ() != bOP.getZ())) {
				this.world.setBlockState(nP, AstromineBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.getDefaultState());
			}

			ComponentProvider componentProvider = ComponentProvider.fromWorld(world);

			WorldBridgeComponent bridgeComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT);

			bridgeComponent.add(nP, new Vec3i((v.getX() - (int) v.getX()) * 16f, (v.getY() - (int) v.getY()) * 16f, (v.getZ() - (int) v.getZ()) * 16f));
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

		this.markDirty();
	}

	public HolographicBridgeProjectorBlockEntity getParent() {
		return parent;
	}

	public void setParent(HolographicBridgeProjectorBlockEntity parent) {
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
			if (this.parent != null) {
				this.parent.destroyBridge();
			}

			this.destroyBridge();

			this.setChild(null);
		}

		super.markRemoved();
	}

	public void destroyBridge() {
		if (this.segments != null && this.world != null) {
			ComponentProvider componentProvider = ComponentProvider.fromWorld(world);

			WorldBridgeComponent bridgeComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT);

			for (Vector3f vec : this.segments) {
				BlockPos pos = new BlockPos(vec.getX(), vec.getY(), vec.getZ());

				bridgeComponent.remove(pos);

				this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		if (tag.contains("child_position")) {
			this.childPosition = BlockPos.fromLong(tag.getLong("child_position"));
		}

		this.color = Color.of(tag.getInt("color"));

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (this.child != null) {
			tag.putLong("child_position", this.child.getPos().asLong());
		} else if (this.childPosition != null) {
			tag.putLong("child_position", this.childPosition.asLong());
		}

		tag.putInt("color", this.color.ARGB);

		return super.toTag(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.fromTag(null, tag);

		this.destroyBridge();

		if (this.childPosition != null) {
			this.child = (HolographicBridgeProjectorBlockEntity) this.world.getBlockEntity(this.childPosition);
		}

		this.buildBridge();
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return this.toTag(compoundTag);
	}
}
