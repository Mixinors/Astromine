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

import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.utilities.NetworkUtilities;
import com.github.chainmailstudios.astromine.common.utilities.capability.block.CableWrenchable;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
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
public abstract class CableBlock extends Block implements SimpleWaterloggedBlock, CableWrenchable {
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");

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

	public static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = ImmutableMap.<BooleanProperty, VoxelShape>builder()
		.put(UP, Block.box(6D, 10D, 6D, 10D, 16D, 10D))
		.put(DOWN, Block.box(6D, 0D, 6D, 10D, 6D, 10D))
		.put(NORTH, Block.box(6D, 6D, 0D, 10D, 10D, 6D))
		.put(SOUTH, Block.box(6D, 6D, 10D, 10D, 10D, 16D))
		.put(EAST, Block.box(10D, 6D, 6D, 16D, 10D, 10D))
		.put(WEST, Block.box(0D, 6D, 6D, 6D, 10D, 10D))
		.build();

	public static final VoxelShape CENTER_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	/**
	 * Instantiates a {@link CableBlock}.
	 */
	public CableBlock(BlockBehaviour.Properties settings) {
		super(settings);

		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
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
	public void setPlacedBy(Level world, BlockPos position, BlockState stateA, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, position, stateA, placer, stack);

		NetworkUtilities.Tracer.trace(getNetworkType(), WorldPos.of(world, position));

		Set<Direction> set = NetworkUtilities.Modeller.of(getNetworkType(), position, world);

		world.setBlockAndUpdate(position, NetworkUtilities.Modeller.toBlockState(set, stateA));

		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = position.relative(direction);
			WorldPos offsetBlock = WorldPos.of(world, offsetPos);

			if (!(offsetBlock.getBlock() instanceof CableBlock))
				continue;
			NetworkMember member = NetworkMemberRegistry.get(offsetBlock, direction.getOpposite());
			if (member.acceptsType(getNetworkType()))
				continue;

			Set<Direction> directions = NetworkUtilities.Modeller.of(((CableBlock) offsetBlock.getBlock()).getNetworkType(), offsetPos, world);

			world.setBlockAndUpdate(offsetPos, NetworkUtilities.Modeller.toBlockState(directions, world.getBlockState(offsetPos)));
		}
	}

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * of neighbors and re-trace the network.
	 */
	@Override
	public void onRemove(BlockState state, Level world, BlockPos position, BlockState newState, boolean moved) {
		super.onRemove(state, world, position, newState, moved);

		if (state.getBlock() == newState.getBlock())
			return;

		WorldNetworkComponent networkComponent = WorldNetworkComponent.get(world);

		networkComponent.remove(networkComponent.get(getNetworkType(), position));

		for (Direction directionA : Direction.values()) {
			BlockPos offsetPos = position.relative(directionA);
			Block offsetBlock = world.getBlockState(offsetPos).getBlock();

			if (!(offsetBlock instanceof CableBlock))
				continue;
			if (((CableBlock) offsetBlock).getNetworkType() != getNetworkType())
				continue;

			NetworkUtilities.Tracer.trace(getNetworkType(), WorldPos.of(world, offsetPos));

			Set<Direction> directions = NetworkUtilities.Modeller.of(getNetworkType(), offsetPos, world);
			world.setBlockAndUpdate(offsetPos, NetworkUtilities.Modeller.toBlockState(directions, world.getBlockState(offsetPos)));
		}
	}

	/**
	 * Override behavior to update the {@link BlockState} properties
	 * and re-trace the network.
	 */
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos position, Block block, BlockPos neighborPosition, boolean moved) {
		super.neighborChanged(state, world, position, block, neighborPosition, moved);

		WorldNetworkComponent networkComponent = WorldNetworkComponent.get(world);

		networkComponent.remove(networkComponent.get(getNetworkType(), position));
		NetworkUtilities.Tracer.trace(getNetworkType(), WorldPos.of(world, position));

		Set<Direction> directions = NetworkUtilities.Modeller.of(getNetworkType(), position, world);
		world.setBlockAndUpdate(position, NetworkUtilities.Modeller.toBlockState(directions, world.getBlockState(position)));
	}

	/**
	 * Override behavior to add the {@link BlockStateProperties#WATERLOGGED} property
	 * and our cardinal properties.
	 */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN, BlockStateProperties.WATERLOGGED);
	}

	/**
	 * Override behavior to return a {@link VoxelShape} based on
	 * the {@link BlockState}'s properties.
	 */
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter world, BlockPos position, CollisionContext entityContext) {
		return NetworkUtilities.Modeller.getVoxelShape(NetworkUtilities.Modeller.of(blockState));
	}

	/**
	 * Override behavior to implement {@link SimpleWaterloggedBlock}.
	 */
	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
	}

	/**
	 * Override behavior to implement {@link SimpleWaterloggedBlock}.
	 */
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getBlockState(context.getClickedPos()).getBlock() == Blocks.WATER);
	}

}
