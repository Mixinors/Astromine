package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.client.render.block.HolographicBridgeBlockEntityRenderer;
import com.github.chainmailstudios.astromine.common.bridge.HolographicBridgeManager;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.Collection;

public class HolographicBridgeBlockEntity extends BlockEntity implements Tickable {
	public Collection<Vector3f> segments = null;

	public HolographicBridgeBlockEntity() {
		super(AstromineBlockEntities.HOLOGRAPHIC_BRIDGE);
	}

	private BlockEntity friend = null;

	@Override
	public void setPos(BlockPos position) {
		super.setPos(position);

		updateFriend(position);
	}

	public BlockEntity getFriend() {
		return friend;
	}

	public void setFriend(BlockEntity friend) {
		this.friend = friend;
	}

	public boolean hasFriend() {
		return this.friend != null;
	}

	public void updateFriend(BlockPos position) {
		Direction direction = world.getBlockState(position).get(HorizontalFacingBlock.FACING);

		ArrayList<BlockPos> positions = new ArrayList<>();

		BlockPos checkPosition = position;

		for (int i = 0; i < 1024; ++i) {
			checkPosition = checkPosition.offset(direction);


			if (world.getBlockEntity(checkPosition) instanceof HolographicBridgeBlockEntity) {
				friend = world.getBlockEntity(checkPosition);

				segments = LineUtilities.getBezierSegments(getPos(), getFriend().getPos(), 250);

				Vector3f origin = segments.iterator().next();
				Vector3f previous = origin;

				for (Vector3f vector : segments) {
					if (vector != origin) {
						BlockPos newPosition = new BlockPos(vector.getX(), vector.getY() + 1, vector.getZ());

						if (!newPosition.equals(friend.getPos()) && !newPosition.equals(getPos())) {
							float percentage = 0;

							if (origin.getY() - previous.getY() > 1) {
								percentage = previous.getY() + 1 - previous.getY();
							} else {
								percentage = origin.getY() - previous.getY();
							}

							HolographicBridgeManager.add(newPosition, (int) Math.ceil(16f * Math.max(0.0625, percentage)));

							System.out.println(newPosition.toShortString());

							world.setBlockState(newPosition, AstromineBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.getDefaultState());
						}
					}
					previous = vector;
				}

				return;
			} else {
				positions.add(checkPosition);
			}
		}

		friend = null;
	}

	@Override
	public void tick() {
		if (friend == null) {
			updateFriend(getPos());
		}
	}
}
