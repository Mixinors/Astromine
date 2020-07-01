package com.github.chainmailstudios.astromine.common.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

import com.github.chainmailstudios.astromine.common.block.HolographicBridgeInvisibleBlock;
import com.github.chainmailstudios.astromine.common.component.world.WorldBridgeComponent;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.client.AstromineSounds;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import org.jetbrains.annotations.NotNull;
import spinnery.widget.api.Color;

import java.util.ArrayList;

public class HolographicBridgeProjectorBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
	public ArrayList<Vector3f> segments = null;
	public ArrayList<Vec3i> members = null;
	public Direction direction = Direction.NORTH;
	public Color color = Color.of(0x7e80cad4);
	public long last = 0;
	private HolographicBridgeProjectorBlockEntity child = null;
	private HolographicBridgeProjectorBlockEntity parent = null;
	private BlockPos childPosition = null;
	private BlockPos parentPosition = null;
	private boolean hasCheckedChild = false;
	private boolean hasCheckedParent = false;

	public HolographicBridgeProjectorBlockEntity() {
		super(AstromineBlockEntityTypes.HOLOGRAPHIC_BRIDGE);
	}

	public boolean hasChild() {
		return this.child != null;
	}

	public HolographicBridgeProjectorBlockEntity getParent() {
		return this.parent;
	}

	public void setParent(HolographicBridgeProjectorBlockEntity parent) {
		if (parent != null) {
			parent.destroyBridge();
		}

		this.parent = parent;
		this.markDirty();
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	@Override
	public void tick() {
		if (!this.hasCheckedChild && this.childPosition != null) {
			if (this.world != null) {
				BlockEntity childEntity = this.world.getBlockEntity(this.childPosition);

				if (childEntity instanceof HolographicBridgeProjectorBlockEntity) {
					this.child = (HolographicBridgeProjectorBlockEntity) childEntity;
					this.child.setParent(this);
					this.buildBridge();
					this.hasCheckedChild = true;
				} else if (childEntity != null) {
					this.hasCheckedChild = true;
				}
			}
		}

		if (!this.hasCheckedParent && this.parentPosition != null) {
			if (this.world != null) {
				BlockEntity parentEntity = this.world.getBlockEntity(this.parentPosition);

				if (parentEntity instanceof HolographicBridgeProjectorBlockEntity) {
					this.parent = (HolographicBridgeProjectorBlockEntity) parentEntity;
					this.parent.setChild(this);
					this.parent.buildBridge();
					this.hasCheckedParent = true;
				} else if (parentEntity != null) {
					this.hasCheckedParent = true;
				}
			}
		}

		long current = System.currentTimeMillis();

		if (current - this.last >= 11187) {
			this.world.playSound(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), AstromineSounds.HUMMING, SoundCategory.BLOCKS, 0.005f, 0.5f, true);
			this.last = current;
		}
	}

	public boolean buildBridge() {
		if (this.child == null || this.world == null) {
			return false;
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
			return false;
		}

		this.segments = (ArrayList<Vector3f>) LineUtilities.getBezierSegments(new Vector3f(bOP.getX(), bOP.getY() + 1, bOP.getZ()),
				new Vector3f(nCP.getX(), nCP.getY() + 0.99f, nCP.getZ()),
				new Vector3f((bOP.getX() + nCP.getX()) / 2f, (bOP.getY() + nCP.getY() + 2f) / 2f, bCP.getZ()),
				distance * 16
		);

		this.members = new ArrayList<>();

		for (Vector3f v : this.segments) {
			BlockPos nP = new BlockPos(v.getX(), v.getY(), v.getZ());

			Material mat = this.world.getBlockState(nP).getMaterial();
			if(mat.isReplaceable() || mat.equals(HolographicBridgeInvisibleBlock.MATERIAL)) {
				if ((nP.getX() != bCP.getX() && nP.getX() != bOP.getX()) || (nP.getZ() != bCP.getZ() && nP.getZ() != bOP.getZ())) {
					this.world.setBlockState(nP, AstromineBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.getDefaultState());
					this.members.add(nP);
				}

				ComponentProvider componentProvider = ComponentProvider.fromWorld(world);

				WorldBridgeComponent bridgeComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT);

				bridgeComponent.add(nP, new Vec3i((v.getX() - (int) v.getX()) * 16f, (v.getY() - (int) v.getY()) * 16f, (v.getZ() - (int) v.getZ()) * 16f));
			} else {
				destroyBridge();
				return false;
			}
		}
		return true;
	}

	public HolographicBridgeProjectorBlockEntity getChild() {
		return this.child;
	}

	public void setChild(HolographicBridgeProjectorBlockEntity child) {
		if (child != null && child.parent != null) {
			child.parent.destroyBridge();
		}

		this.child = child;
		this.markDirty();
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.fromTag(null, tag);

		this.destroyBridge();

		if (this.childPosition != null) {
			this.child = (HolographicBridgeProjectorBlockEntity) this.world.getBlockEntity(this.childPosition);
		}

		if (this.parentPosition != null) {
			this.parent = (HolographicBridgeProjectorBlockEntity) this.world.getBlockEntity(this.parentPosition);
		}

		this.buildBridge();
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		if (tag.contains("child_position")) {
			this.childPosition = BlockPos.fromLong(tag.getLong("child_position"));
		}
		if (tag.contains("parent_position")) {
			this.parentPosition = BlockPos.fromLong(tag.getLong("parent_position"));
		}

		this.direction = Direction.byId(tag.getInt("direction"));
		this.color = Color.of(tag.getInt("color"));

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (this.child != null) {
			tag.putLong("child_position", this.child.getPos().asLong());
		}
		if (this.parent != null) {
			tag.putLong("parent_position", this.parent.getPos().asLong());
		}

		if (this.childPosition != null) {
			tag.putLong("child_position", this.childPosition.asLong());
		}
		if (this.parentPosition != null) {
			tag.putLong("parent_position", this.parentPosition.asLong());
		}


		tag.putInt("direction", this.direction.getId());
		tag.putInt("color", this.color.ARGB);

		return super.toTag(tag);
	}

	@Override
	public double getSquaredRenderDistance() {
		return Math.pow(2, 15);
	}

	@Override
	public void markRemoved() {
		if (this.child != null) {
			this.child.setParent(null);
			this.destroyBridge();

			this.setChild(null);
		}

		if (this.parent != null) {
			this.parent.setChild(null);
			this.parent.destroyBridge();

			this.setParent(null);
		}

		super.markRemoved();
	}

	public void destroyBridge() {
		if (this.segments != null && this.world != null) {
			for (Vec3i vec : this.members) {
				ComponentProvider componentProvider = ComponentProvider.fromWorld(world);

				WorldBridgeComponent bridgeComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_BRIDGE_COMPONENT);

				BlockPos pos = (BlockPos) vec;

				bridgeComponent.remove(pos);

				if(this.world.getBlockState(pos).getBlock() instanceof HolographicBridgeInvisibleBlock) {
					this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
			}
		}
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return this.toTag(compoundTag);
	}


}
