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

import com.github.mixinors.astromine.cardinalcomponents.common.component.base.NetworkComponent;
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

import com.github.mixinors.astromine.cardinalcomponents.common.component.base.NetworkComponentImpl;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.techreborn.common.util.NetworkUtils;
import com.github.mixinors.astromine.techreborn.common.util.data.position.WorldPos;

import javax.annotation.Nullable;
import java.util.Map;

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

	public static final Map<Direction, BooleanProperty> PROPERTIES = Map.of(
			Direction.EAST, EAST,
			Direction.WEST, WEST,
			Direction.NORTH, NORTH,
			Direction.SOUTH, SOUTH,
			Direction.UP, UP,
			Direction.DOWN, DOWN
	);

	public static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = Map.of(
			UP, Block.createCuboidShape(6.0D, 10.0D, 6.0D, 10.0D, 16.0D, 10.0D),
			DOWN, Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D),
			NORTH, Block.createCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 6.0D),
			SOUTH, Block.createCuboidShape(6.0D, 6.0D, 10.0D, 10.0D, 10.0D, 16.0D),
			EAST, Block.createCuboidShape(10.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D),
			WEST, Block.createCuboidShape(0.0D, 6.0D, 6.0D, 6.0D, 10.0D, 10.0D)
	);

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
	public abstract NetworkType getNetworkType();

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * and re-trace the network.
	 */
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);

		NetworkUtils.Tracer.trace(getNetworkType(), WorldPos.of(world, pos));

		var set = NetworkUtils.Modeller.of(getNetworkType(), pos, world);

		world.setBlockState(pos, NetworkUtils.Modeller.toBlockState(set, state));

		for (var direction : Direction.values()) {
			var offsetPos = pos.offset(direction);
			var offsetBlock = WorldPos.of(world, offsetPos);

			if (!(offsetBlock.getBlock() instanceof CableBlock cableBlock))
				continue;
			
			var member = NetworkMemberRegistry.get(offsetBlock, direction.getOpposite());
			
			if (member.acceptsType(getNetworkType()))
				continue;

			var directions = NetworkUtils.Modeller.of(cableBlock.getNetworkType(), offsetPos, world);

			world.setBlockState(offsetPos, NetworkUtils.Modeller.toBlockState(directions, world.getBlockState(offsetPos)));
		}
	}

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * of neighbors and re-trace the network.
	 */
	@Override
	public void onStateReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(oldState, world, pos, newState, moved);

		if (oldState.getBlock() == newState.getBlock())
			return;

		var networkComponent = NetworkComponent.from(world);

		networkComponent.remove(networkComponent.get(getNetworkType(), pos));

		for (var directionA : Direction.values()) {
			var offsetPos = pos.offset(directionA);
			var offsetBlock = world.getBlockState(offsetPos).getBlock();

			if (!(offsetBlock instanceof CableBlock cableBlock) || cableBlock.getNetworkType() != getNetworkType())
				continue;

			NetworkUtils.Tracer.trace(getNetworkType(), WorldPos.of(world, offsetPos));

			var directions = NetworkUtils.Modeller.of(getNetworkType(), offsetPos, world);
			
			world.setBlockState(offsetPos, NetworkUtils.Modeller.toBlockState(directions, world.getBlockState(offsetPos)));
		}
	}

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * and re-trace the network.
	 */
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
		super.neighborUpdate(state, world, pos, block, neighborPos, moved);

		var networkComponent = NetworkComponent.from(world);

		networkComponent.remove(networkComponent.get(getNetworkType(), pos));
		
		NetworkUtils.Tracer.trace(getNetworkType(), WorldPos.of(world, pos));

		var directions = NetworkUtils.Modeller.of(getNetworkType(), pos, world);
		
		world.setBlockState(pos, NetworkUtils.Modeller.toBlockState(directions, world.getBlockState(pos)));
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
	public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext entityCtx) {
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
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock() == Blocks.WATER);
	}

}
