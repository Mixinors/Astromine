package com.github.chainmailstudios.astromine.common.block.conveyor;

import com.github.chainmailstudios.astromine.common.utilities.RotationUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class CatwalkStairsBlock extends HorizontalFacingBlock {
    public CatwalkStairsBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ConveyorProperties.LEFT, false).with(ConveyorProperties.RIGHT, false));
	}

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ConveyorProperties.LEFT, ConveyorProperties.RIGHT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        return this.getDefaultState().with(FACING, itemPlacementContext.getPlayer().isSneaking() ? itemPlacementContext.getPlayerFacing().getOpposite() : itemPlacementContext.getPlayerFacing());
    }

    public boolean isAdjacentBlockOfMyType(WorldAccess world, BlockPos position, Direction facing) {

		assert null != world : "world cannot be null";
		assert null != position : "position cannot be null";
		assert null != this : "type cannot be null";

		BlockPos newPosition = position.offset(facing);
		BlockState blockState = world.getBlockState(newPosition);
		Block block = (null == blockState) ? null : blockState.getBlock();

		return this == block;
	}

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
	    BlockState newState = state;

        newState = state.with(ConveyorProperties.RIGHT, this.isAdjacentBlockOfMyType(world, pos, state.get(FACING).rotateYClockwise())).with(ConveyorProperties.LEFT, this.isAdjacentBlockOfMyType(world, pos, state.get(FACING).rotateYCounterclockwise()));
	    
	    return newState;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext entityContext) {
        Direction facing = state.get(FACING);
		Box step1 = new Box(0, 0, (12F / 16F), 1, (3F / 16F), 1);
		Box step2 = new Box(0, 0, (8F / 16F), 1, (7F / 16F), (12F / 16F));
		Box step3 = new Box(0, 0, (4F / 16F), 1, (11F / 16F), (8F / 16F));
		Box step4 = new Box(0, 0, 0, 1, (15F / 16F), (4F / 16F));
		VoxelShape shape = VoxelShapes.union(RotationUtilities.getRotatedShape(step1, facing), RotationUtilities.getRotatedShape(step2, facing), RotationUtilities.getRotatedShape(step3, facing), RotationUtilities.getRotatedShape(step4, facing));
        
        return shape;
    }
}
