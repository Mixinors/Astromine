/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.block.base;

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

import com.github.mixinors.astromine.common.component.world.WorldNetworkComponent;
import com.github.mixinors.astromine.common.network.NetworkMember;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.common.util.NetworkUtils;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Block} which is composed of six {@link BlockState} properties,
 * which are responsible for its {@link VoxelShape} and model.
 * <p>
 * It will search and trace a network of {@link #getNetworkType()} on
 * {@link BlockState} change, placement or removal.
 */
public abstract class CableBlock extends Block implements Waterloggable {
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty UP = BooleanProperty.of("up");
	public static final BooleanProperty DOWN = BooleanProperty.of("down");

	public static final Map<Direction, BooleanProperty> PROPERTIES = new HashMap<>() {
		{
			put(Direction.EAST, EAST);
			put(Direction.WEST, WEST);
			put(Direction.NORTH, NORTH);
			put(Direction.SOUTH, SOUTH);
			put(Direction.UP, UP);
			put(Direction.DOWN, DOWN);
		}
	};

	public static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = ImmutableMap.<BooleanProperty, VoxelShape>builder()
		.put(UP, Block.createCuboidShape(6D, 10D, 6D, 10D, 16D, 10D))
		.put(DOWN, Block.createCuboidShape(6D, 0D, 6D, 10D, 6D, 10D))
		.put(NORTH, Block.createCuboidShape(6D, 6D, 0D, 10D, 10D, 6D))
		.put(SOUTH, Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 16D))
		.put(EAST, Block.createCuboidShape(10D, 6D, 6D, 16D, 10D, 10D))
		.put(WEST, Block.createCuboidShape(0D, 6D, 6D, 6D, 10D, 10D))
		.build();

	public static final VoxelShape CENTER_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	/**
	 * Instantiates a {@link CableBlock}.
	 */
	public CableBlock(AbstractBlock.Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}

	/**
	 * Returns this {@link CableBlock}'s {@link NetworkType}.
	 */
	public abstract <T extends NetworkType> T getNetworkType();

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * and re-trace the network.
	 */
	@Override
	public void onPlaced(World world, BlockPos position, BlockState stateA, LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, position, stateA, placer, stack);

		NetworkUtils.Tracer.trace(getNetworkType(), WorldPos.of(world, position));

		Set<Direction> set = NetworkUtils.Modeller.of(getNetworkType(), position, world);

		world.setBlockState(position, NetworkUtils.Modeller.toBlockState(set, stateA));

		for (Direction direction : Direction.values()) {
			var offsetPos = position.offset(direction);
			var offsetBlock = WorldPos.of(world, offsetPos);

			if (!(offsetBlock.getBlock() instanceof CableBlock))
				continue;
			var member = NetworkMemberRegistry.get(offsetBlock, direction.getOpposite());
			if (member.acceptsType(getNetworkType()))
				continue;

			Set<Direction> directions = NetworkUtils.Modeller.of(((CableBlock) offsetBlock.getBlock()).getNetworkType(), offsetPos, world);

			world.setBlockState(offsetPos, NetworkUtils.Modeller.toBlockState(directions, world.getBlockState(offsetPos)));
		}
	}

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * of neighbors and re-trace the network.
	 */
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, position, newState, moved);

		if (state.getBlock() == newState.getBlock())
			return;

		var networkComponent = WorldNetworkComponent.get(world);

		networkComponent.remove(networkComponent.get(getNetworkType(), position));

		for (Direction directionA : Direction.values()) {
			var offsetPos = position.offset(directionA);
			var offsetBlock = world.getBlockState(offsetPos).getBlock();

			if (!(offsetBlock instanceof CableBlock))
				continue;
			if (((CableBlock) offsetBlock).getNetworkType() != getNetworkType())
				continue;

			NetworkUtils.Tracer.trace(getNetworkType(), WorldPos.of(world, offsetPos));

			Set<Direction> directions = NetworkUtils.Modeller.of(getNetworkType(), offsetPos, world);
			world.setBlockState(offsetPos, NetworkUtils.Modeller.toBlockState(directions, world.getBlockState(offsetPos)));
		}
	}

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * and re-trace the network.
	 */
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos position, Block block, BlockPos neighborPosition, boolean moved) {
		super.neighborUpdate(state, world, position, block, neighborPosition, moved);

		var networkComponent = WorldNetworkComponent.get(world);

		networkComponent.remove(networkComponent.get(getNetworkType(), position));
		NetworkUtils.Tracer.trace(getNetworkType(), WorldPos.of(world, position));

		Set<Direction> directions = NetworkUtils.Modeller.of(getNetworkType(), position, world);
		world.setBlockState(position, NetworkUtils.Modeller.toBlockState(directions, world.getBlockState(position)));
	}

	/**
	 * Override behavior to add the {@link Properties#WATERLOGGED} property
	 * and our cardinal properties.
	 */
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN, Properties.WATERLOGGED);
	}

	/**
	 * Override behavior to return a {@link VoxelShape} based on
	 * the {@link BlockState}'s properties.
	 */
	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos position, ShapeContext entityContext) {
		return NetworkUtils.Modeller.getVoxelShape(NetworkUtils.Modeller.of(blockState));
	}

	/**
	 * Override behavior to implement {@link Waterloggable}.
	 */
	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	/**
	 * Override behavior to implement {@link Waterloggable}.
	 */
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WATER);
	}

}
