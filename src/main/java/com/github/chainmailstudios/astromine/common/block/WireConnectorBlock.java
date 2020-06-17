package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.WireConnectorBlockEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public abstract class WireConnectorBlock extends Block implements BlockEntityProvider {
	public static final DirectionProperty FACING = FacingBlock.FACING;

	public WireConnectorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (WireConnectorBlockEntity.getSelected(world) != null) {
			if (player.getStackInHand(hand).getItem() == AstromineItems.WIRE_COIL) {
				WireConnectorBlockEntity child = (WireConnectorBlockEntity) world.getBlockEntity(pos);
				WireConnectorBlockEntity parent = WireConnectorBlockEntity.getSelected(world);

				int squared = (int) pos.getSquaredDistance(parent.getPos());

				if (squared > 32) {
					player.sendMessage(new TranslatableText("text.astromine.message.wire_cable_connection_failed", pos.getX(), pos.getY(), pos.getZ(), parent.getPos().getX(), parent.getPos().getY(), parent.getPos().getZ()), true);
					WireConnectorBlockEntity.setSelected(world, null);

					return ActionResult.FAIL;
				}

				List<Vector3f> positions = (List<Vector3f>) WireConnectorBlockEntity.getSegments(parent, child, squared);

				for (Vector3f position : positions) {
					BlockPos blockPosition = new BlockPos(position.getX(), position.getY(), position.getZ());

					if (world.getBlockState(blockPosition).getBlock() != Blocks.AIR && !(world.getBlockState(blockPosition).getBlock() instanceof WireConnectorBlock)) {
						player.sendMessage(new TranslatableText("text.astromine.message.wire_cable_connection_failed", pos.getX(), pos.getY(), pos.getZ(), parent.getPos().getX(), parent.getPos().getY(), parent.getPos().getZ()), true);
						world.addParticle(ParticleTypes.BARRIER, blockPosition.getX() + 0.5f, blockPosition.getY() + 0.5f, blockPosition.getZ() + 0.5f, 0, 0, 0);
						WireConnectorBlockEntity.setSelected(world, null);

						return ActionResult.FAIL;
					}
				}

				player.sendMessage(new TranslatableText("text.astromine.message.wire_cable_connection_successful", pos.getX(), pos.getY(), pos.getZ(), parent.getPos().getX(), parent.getPos().getY(), parent.getPos().getZ()), true);

				child.parents.add(NetworkNode.of(parent.getPos()));
				parent.children.add(NetworkNode.of(child.getPos()));

				parent.markDirty();
				child.markDirty();

				if (!world.isClient()) {
					parent.sync();
					child.sync();
				}

				WireConnectorBlockEntity.setSelected(world, null);

				player.getStackInHand(hand).decrement(1);

				return ActionResult.SUCCESS;
			} else {
				WireConnectorBlockEntity.setSelected(world, null);

				return ActionResult.FAIL;
			}

		} else {
			if (player.getStackInHand(hand).getItem() == AstromineItems.WIRE_COIL) {
				player.sendMessage(new TranslatableText("text.astromine.message.wire_cable_connection_select", pos.getX(), pos.getY(), pos.getZ()), true);
				WireConnectorBlockEntity.setSelected(world, (WireConnectorBlockEntity) world.getBlockEntity(pos));

				return ActionResult.SUCCESS;
			} else {
				WireConnectorBlockEntity.setSelected(world, null);

				return ActionResult.FAIL;
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(position);

			if (blockEntity != null) {
				WireConnectorBlockEntity removedEntity = (WireConnectorBlockEntity) blockEntity;

				onConnectorBroken(removedEntity, world);

				for (NetworkNode node : removedEntity.children) {
					BlockEntity blockEntityA = world.getBlockEntity(node.getPosition());

					if (blockEntityA != null) {
						WireConnectorBlockEntity wireEntity = (WireConnectorBlockEntity) blockEntityA;
						wireEntity.sync();
					}
				}

				for (NetworkNode node : removedEntity.parents) {
					BlockEntity blockEntityA = world.getBlockEntity(node.getPosition());

					if (blockEntityA != null) {
						WireConnectorBlockEntity wireEntity = (WireConnectorBlockEntity) blockEntityA;
						wireEntity.sync();
					}
				}
			}
		}

		super.onStateReplaced(state, world, position, newState, moved);
	}

	public static void onConnectorBroken(BlockPos connectorPosition, List<BlockPos> friends, World world) {
		for (BlockPos friendPosition : friends) {
			BlockEntity blockEntity = world.getBlockEntity(friendPosition);

			if (blockEntity != null) {
				WireConnectorBlockEntity friend = (WireConnectorBlockEntity) blockEntity;

				friend.children.remove(connectorPosition);
				friend.parents.remove(connectorPosition);
			}
		}
	}

	public static void onConnectorBroken(BlockEntity blockEntity, World world) {
		WireConnectorBlockEntity wireEntity = (WireConnectorBlockEntity) blockEntity;

		for (NetworkNode node : wireEntity.parents) {
			WireConnectorBlockEntity parent = (WireConnectorBlockEntity) world.getBlockEntity(node.getPosition());

			if (parent != null) parent.children.remove(blockEntity.getPos());
		}

		for (NetworkNode node : wireEntity.children) {
			WireConnectorBlockEntity child = (WireConnectorBlockEntity) world.getBlockEntity(node.getPosition());

			if (child != null) child.parents.remove(blockEntity.getPos());
		}
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos) {
		return true;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction direction = context.getSide();
		BlockState blockState = context.getWorld().getBlockState(context.getBlockPos().offset(direction.getOpposite()));
		return blockState.getBlock() == this && blockState.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite()) : this.getDefaultState().with(FACING, direction);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING);
	}
}
