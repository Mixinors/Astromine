package com.github.chainmailstudios.astromine.common.block.conveyor;

import com.github.chainmailstudios.astromine.common.block.conveyor.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.common.block.conveyor.entity.VerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.Conveyable;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.Conveyor;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.ConveyorType;
import com.github.chainmailstudios.astromine.common.utilities.RotationUtilities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class VerticalConveyorBlock extends HorizontalFacingBlock implements BlockEntityProvider, Conveyor {
    private int speed;

    public VerticalConveyorBlock(Settings settings, int speed) {
        super(settings);

        this.speed = speed;
        setDefaultState(getDefaultState().with(ConveyorProperties.FRONT, false).with(ConveyorProperties.CONVEYOR, false));
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public ConveyorType getType() {
        return ConveyorType.VERTICAL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new VerticalConveyorBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(blockPos);

        if (!playerEntity.getStackInHand(hand).isEmpty() && Block.getBlockFromItem(playerEntity.getStackInHand(hand).getItem()) instanceof Conveyor) {
            return ActionResult.PASS;
        } else if (!playerEntity.getStackInHand(hand).isEmpty() && blockEntity.isEmpty()) {
            blockEntity.setStack(playerEntity.getStackInHand(hand));
            playerEntity.setStackInHand(hand, ItemStack.EMPTY);

            return ActionResult.SUCCESS;
        } else if (!blockEntity.isEmpty()) {
            playerEntity.inventory.offerOrDrop(world, blockEntity.getStack());
            blockEntity.removeStack();

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
        Direction direction = blockState.get(FACING);
    }

    @Override
    public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
        Direction facing = blockState.get(FACING);
        if (blockState.getBlock() != blockState2.getBlock()) {
            BlockEntity blockEntity_1 = world.getBlockEntity(blockPos);
            if (blockEntity_1 instanceof VerticalConveyorBlockEntity) {
				((VerticalConveyorBlockEntity) blockEntity_1).setRemoved(true);
                ItemScatterer.spawn(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ((VerticalConveyorBlockEntity) blockEntity_1).getStack());
                world.updateComparators(blockPos, this);
            }

            super.onStateReplaced(blockState, world, blockPos, blockState2, boolean_1);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction fromDirection, BlockState fromState, WorldAccess world, BlockPos blockPos, BlockPos fromPos) {
        BlockState newState = blockState;
        Direction direction = newState.get(FACING);

        BlockPos frontPos = blockPos.offset(direction.getOpposite());
        BlockPos upPos = blockPos.up();
        BlockPos conveyorPos = blockPos.offset(direction).up();

        BlockEntity frontBlockEntity = world.getBlockEntity(frontPos);
        if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).isOutputSide(direction, getType())) {
            newState = newState.with(ConveyorProperties.FRONT, true);
        } else
            newState = newState.with(ConveyorProperties.FRONT, false);

        BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);
		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !((Conveyable) conveyorBlockEntity).hasBeenRemoved() && ((Conveyable) conveyorBlockEntity).validInputSide(direction.getOpposite()))
			newState = newState.with(ConveyorProperties.CONVEYOR, true);
		else
			newState = newState.with(ConveyorProperties.CONVEYOR, false);

        return newState;
    }

	@Override
	public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean boolean_1) {
    	Direction direction = blockState.get(FACING);
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(blockPos);

		BlockPos upPos = blockPos.up();
		BlockPos conveyorPos = blockPos.offset(direction).up();

		BlockEntity upBlockEntity = world.getBlockEntity(upPos);
		if (upBlockEntity instanceof Conveyable && ((Conveyable) upBlockEntity).validInputSide(Direction.DOWN))
			((VerticalConveyorBlockEntity) blockEntity).setUp(true);
		else
			((VerticalConveyorBlockEntity) blockEntity).setUp(false);

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);
		checkForConveyor(world, blockState, conveyorBlockEntity, direction, blockPos, upPos);
	}

	public void checkForConveyor(World world, BlockState blockState, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		BlockState newState = blockState;

		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !((Conveyable) conveyorBlockEntity).hasBeenRemoved() && ((Conveyable) conveyorBlockEntity).validInputSide(direction.getOpposite()))
			newState = newState.with(ConveyorProperties.CONVEYOR, true);
		else
			newState = newState.with(ConveyorProperties.CONVEYOR, false);

		world.setBlockState(pos, newState, 8);
	}

    @Override
    public boolean hasComparatorOutput(BlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
        return ((ConveyorBlockEntity) world.getBlockEntity(blockPos)).isEmpty() ? 0 : 15;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
        stateManagerBuilder.add(FACING, ConveyorProperties.FRONT, ConveyorProperties.CONVEYOR);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		World world = itemPlacementContext.getWorld();
		BlockPos blockPos = itemPlacementContext.getBlockPos();
		BlockState newState = this.getDefaultState().with(FACING, itemPlacementContext.getPlayerFacing());

		newState = newState.getStateForNeighborUpdate(null, newState, world, blockPos, blockPos);

		return newState;
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
        VoxelShape box1 = RotationUtilities.getRotatedShape(new Box(0, 0, 0, 1, 1, (4F / 16F)), blockState.get(FACING));
        VoxelShape box2 = RotationUtilities.getRotatedShape(new Box(0, 0, 0, 1, (4F / 16F), 1), blockState.get(FACING));

        if (blockState.get(ConveyorProperties.FRONT)) {
            return VoxelShapes.union(box1, box2);
        } else {
            return box1;
        }
    }
}
