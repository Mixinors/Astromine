package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.network.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class PipeCableBlock extends Block implements NetworkMember {
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");

    public static final Map<Direction, BooleanProperty> PROPERTY_MAP = new HashMap<Direction, BooleanProperty>() {{
        put(Direction.EAST, EAST);
        put(Direction.WEST, WEST);
        put(Direction.NORTH, NORTH);
        put(Direction.SOUTH, SOUTH);
        put(Direction.UP, UP);
        put(Direction.DOWN, DOWN);
    }};

    public static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = new HashMap<BooleanProperty, VoxelShape>() {{
        put(UP, Block.createCuboidShape(6D, 10D, 6D, 10D, 16D, 10D));
        put(DOWN, Block.createCuboidShape(6D, 0D, 6D, 10D, 6D, 10D));
        put(NORTH, Block.createCuboidShape(6D, 6D, 0D, 10D, 10D, 6D));
        put(SOUTH, Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 16D));
        put(EAST, Block.createCuboidShape(10D, 6D, 6D, 16D, 10D, 10D));
        put(WEST, Block.createCuboidShape(0D, 6D, 6D, 6D, 10D, 10D));
    }};

    protected static final Map<Integer, VoxelShape> SHAPE_CACHE = new HashMap<>();

    protected static final VoxelShape CENTER_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

    public PipeCableBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos position, BlockState stateA, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, position, stateA, placer, itemStack);

        NetworkTracer.Tracer tracer = new NetworkTracer.Tracer();
        tracer.trace(getNetworkType(), position, world);

        NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
        modeller.scanNeighbours(position, world);

        world.setBlockState(position, modeller.applyToBlockState(stateA));

        for (Direction direction : Direction.values()) {
            BlockPos posA = position.offset(direction);

            NetworkTracer.Modeller modellerB = new NetworkTracer.Modeller();
            modellerB.scanNeighbours(posA, world);
            world.setBlockState(posA, modellerB.applyToBlockState(world.getBlockState(posA)));
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, position, newState, moved);

        if (state.getBlock() == newState.getBlock()) return;

        NetworkManager.INSTANCE.remove(NetworkManager.INSTANCE.get(getNetworkType(), position));

        for (Direction directionA : Direction.values()) {
            BlockPos posA = position.offset(directionA);

            NetworkTracer.Tracer tracer = new NetworkTracer.Tracer();
            tracer.trace(getNetworkType(), posA, world);

            NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
            modeller.scanNeighbours(posA, world);

            BlockState stateB = world.getBlockState(posA);

            world.setBlockState(posA, modeller.applyToBlockState(stateB));
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos position, Block block, BlockPos neighborPosition, boolean moved) {
        super.neighborUpdate(state, world, position, block, neighborPosition, moved);

        if (world.getBlockState(neighborPosition).getBlock() == block) return;

        NetworkManager.INSTANCE.get(getNetworkType(), position).removeMember(NetworkNode.of(neighborPosition));
        NetworkManager.INSTANCE.get(getNetworkType(), position).removeNode(NetworkNode.of(position));

        NetworkTracer.Tracer tracer = new NetworkTracer.Tracer();
        tracer.trace(getNetworkType(), position, world);

        NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
        modeller.scanNeighbours(position, world);

        world.setBlockState(position, modeller.applyToBlockState(state));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos position, ShapeContext entityContext) {
        VoxelShape returnShape = CENTER_SHAPE;
        NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
        modeller.scanBlockState(blockState);
        returnShape = modeller.applyToVoxelShape(returnShape);
        return returnShape;
    }
}
