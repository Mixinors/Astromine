package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.WireConnectorBlock;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public abstract class WireConnectorBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	private static final Map<World, WireConnectorBlockEntity> selection = new HashMap<>();

	public List<NetworkNode> children = new ArrayList<>();
	public List<NetworkNode> parents = new ArrayList<>();

	public static final Map<NetworkNode, Collection<Vector3f>> points = new HashMap<>();

	public WireConnectorBlockEntity() {
		super(AstromineBlockEntityTypes.ENERGY_WIRE_CONNECTOR);
	}

	public static WireConnectorBlockEntity getSelected(World world) {
		return selection.get(world);
	}

	public static void setSelected(World world, WireConnectorBlockEntity wireNodeBlockEntity) {
		selection.put(world, wireNodeBlockEntity);
	}

	public static Collection<Vector3f> getSegments(WireConnectorBlockEntity parent, WireConnectorBlockEntity child, float distance) {
		if (points.containsKey(NetworkNode.of(child.getPos()))) {
			points.get(NetworkNode.of(child.getPos()));
		}

		List<Vector3f> positions = new ArrayList<>();

		if (child == null || parent == null) return positions;
		if (child.world == null || parent.world == null) return positions;

		float oY = parent.getCableOffset();

		float x1 = parent.getPos().getX();
		float y1 = parent.getPos().getY();
		float z1 = parent.getPos().getZ();

		float[] x1y1z1 = WireConnectorBlockEntity.applyTransformations(parent.world.getBlockState(parent.getPos()).get(WireConnectorBlock.FACING), x1, y1, z1, oY);
		x1 = x1y1z1[0];
		y1 = x1y1z1[1];
		z1 = x1y1z1[2];

		oY = child.getCableOffset();

		float x3 = child.getPos().getX();
		float y3 = child.getPos().getY();
		float z3 = child.getPos().getZ();

		float[] x3y3z3 = WireConnectorBlockEntity.applyTransformations(parent.world.getBlockState(parent.getPos()).get(WireConnectorBlock.FACING), x3, y3, z3, oY);
		x3 = x3y3z3[0];
		y3 = x3y3z3[1];
		z3 = x3y3z3[2];

		Collection<Vector3f> segments = LineUtilities.getBezierSegments(new Vector3f(x1, y1, z1), new Vector3f(x3, y3, z3), null, distance * 4);

		points.put(NetworkNode.of(child.getPos()), segments);

		return segments;
	}

	public static float[] applyTransformations(Direction direction, float x1, float y1, float z1, float oY) {
		switch (direction) {
			case NORTH:
				x1 += 0.5f;
				y1 += 0.5f;
				z1 += 1f - oY;
				break;
			case SOUTH:
				x1 += 0.5f;
				y1 += 0.5f;
				z1 += oY;
				break;
			case WEST:
				x1 += 1f - oY;
				y1 += 0.5f;
				z1 += 0.5f;
				break;
			case EAST:
				x1 += oY;
				y1 += 0.5f;
				z1 += 0.5f;
				break;
			case UP:
				x1 += 0.5f;
				y1 += oY;
				z1 += 0.5f;
				break;
			case DOWN:
				x1 += 0.5f;
				y1 += 1 - oY;
				z1 += 0.5f;
				break;
		}

		return new float[]{x1, y1, z1};
	}

	public abstract float getCableOffset();

	public abstract int getColor();

	@Override
	public double getSquaredRenderDistance() {
		return Math.pow(2, 15);
	}

	/**
	 * Implement persistent data (de)serialization.
	 */
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag childrenSubtag = new CompoundTag();
		CompoundTag parentSubtag = new CompoundTag();

		for (int i = 0; i < children.size(); ++i) {
			childrenSubtag.put(String.valueOf(i), children.get(i).toTag(new CompoundTag()));
		}

		for (int i = 0; i < parents.size(); ++i) {
			parentSubtag.put(String.valueOf(i), parents.get(i).toTag(new CompoundTag()));
		}

		tag.put("children", childrenSubtag);
		tag.put("parents", parentSubtag);

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		this.parents.clear();
		this.children.clear();

		CompoundTag childrenSubtag = tag.getCompound("children");
		CompoundTag parentSubtag = (CompoundTag) tag.get("parents");

		for (String k : childrenSubtag.getKeys()) {
			children.add(NetworkNode.fromTag(childrenSubtag.getCompound(k)));
		}

		for (String k : parentSubtag.getKeys()) {
			parents.add(NetworkNode.fromTag(parentSubtag.getCompound(k)));
		}

		super.fromTag(state, tag);
	}

	/**
	 * Implement client synchronization data (de)serialization.
	 */
	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		this.fromTag(null, compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return this.toTag(compoundTag);
	}
}
