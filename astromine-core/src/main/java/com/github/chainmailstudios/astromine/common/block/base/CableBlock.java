/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.common.block.base;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkTracer;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.utilities.capability.block.CableWrenchable;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class CableBlock extends Block implements Waterloggable, CableWrenchable {
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty UP = BooleanProperty.of("up");
	public static final BooleanProperty DOWN = BooleanProperty.of("down");

	public static final Map<Direction, BooleanProperty> PROPERTIES = new HashMap<Direction, BooleanProperty>() {
		{
			put(Direction.EAST, EAST);
			put(Direction.WEST, WEST);
			put(Direction.NORTH, NORTH);
			put(Direction.SOUTH, SOUTH);
			put(Direction.UP, UP);
			put(Direction.DOWN, DOWN);
		}
	};

	public static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = new HashMap<BooleanProperty, VoxelShape>() {
		{
			put(UP, Block.createCuboidShape(6D, 10D, 6D, 10D, 16D, 10D));
			put(DOWN, Block.createCuboidShape(6D, 0D, 6D, 10D, 6D, 10D));
			put(NORTH, Block.createCuboidShape(6D, 6D, 0D, 10D, 10D, 6D));
			put(SOUTH, Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 16D));
			put(EAST, Block.createCuboidShape(10D, 6D, 6D, 16D, 10D, 10D));
			put(WEST, Block.createCuboidShape(0D, 6D, 6D, 6D, 10D, 10D));
		}
	};

	protected static final Map<Integer, VoxelShape> SHAPE_CACHE = new HashMap<>();
	protected static final VoxelShape CENTER_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public CableBlock(AbstractBlock.Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}

	public abstract <T extends NetworkType> T getNetworkType();

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WATER);
	}

	@Override
	public void onPlaced(World world, BlockPos position, BlockState stateA, LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, position, stateA, placer, stack);

		NetworkTracer.Tracer.INSTANCE.trace(getNetworkType(), WorldPos.of(world, position));

		NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
		modeller.scanNeighbours(getNetworkType(), position, world);

		world.setBlockState(position, modeller.applyToBlockState(stateA));

		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = position.offset(direction);
			WorldPos offsetBlock = WorldPos.of(world, offsetPos);

			if (!(offsetBlock.getBlock() instanceof CableBlock))
				continue;
			NetworkMember member = NetworkMemberRegistry.get(offsetBlock, direction.getOpposite());
			if (member.acceptsType(getNetworkType()))
				continue;

			NetworkTracer.Modeller offsetModeller = new NetworkTracer.Modeller();
			offsetModeller.scanNeighbours(((CableBlock) offsetBlock.getBlock()).getNetworkType(), offsetPos, world);

			world.setBlockState(offsetPos, offsetModeller.applyToBlockState(world.getBlockState(offsetPos)));
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, position, newState, moved);

		if (state.getBlock() == newState.getBlock())
			return;

		WorldNetworkComponent networkComponent = WorldNetworkComponent.get(world);

		networkComponent.removeInstance(networkComponent.getInstance(getNetworkType(), position));

		for (Direction directionA : Direction.values()) {
			BlockPos offsetPos = position.offset(directionA);
			Block offsetBlock = world.getBlockState(offsetPos).getBlock();

			if (!(offsetBlock instanceof CableBlock))
				continue;
			if (((CableBlock) offsetBlock).getNetworkType() != getNetworkType())
				continue;

			NetworkTracer.Tracer.INSTANCE.trace(getNetworkType(), WorldPos.of(world, offsetPos));

			NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
			modeller.scanNeighbours(getNetworkType(), offsetPos, world);

			world.setBlockState(offsetPos, modeller.applyToBlockState(world.getBlockState(offsetPos)));
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos position, Block block, BlockPos neighborPosition, boolean moved) {
		super.neighborUpdate(state, world, position, block, neighborPosition, moved);

		WorldNetworkComponent networkComponent = WorldNetworkComponent.get(world);

		networkComponent.removeInstance(networkComponent.getInstance(getNetworkType(), position));
		NetworkTracer.Tracer.INSTANCE.trace(getNetworkType(), WorldPos.of(world, position));

		NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
		modeller.scanNeighbours(getNetworkType(), position, world);

		world.setBlockState(position, modeller.applyToBlockState(world.getBlockState(position)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN, Properties.WATERLOGGED);
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
