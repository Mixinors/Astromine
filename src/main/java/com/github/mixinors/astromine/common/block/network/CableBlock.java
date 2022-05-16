/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.block.network;

import com.github.mixinors.astromine.common.block.entity.cable.CableBlockEntity;
import com.github.mixinors.astromine.common.component.world.WorldNetworkComponent;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.NetworkType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.util.NetworkUtils;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import com.google.common.collect.ImmutableMap;
import dev.architectury.hooks.block.BlockEntityHooks;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.minecraft.state.property.Properties.WATERLOGGED;

/**
 * A {@link Block} which is composed of six connections, which are responsible for its {@link VoxelShape} and model.
 * <p>
 * It will search and trace a network of {@link #getNetworkType()} on {@link BlockState} change, placement or removal.
 */
public abstract class CableBlock extends BlockWithEntity implements Waterloggable {
	public static final Map<Direction, VoxelShape> SIDE_SHAPE_MAP = ImmutableMap.<Direction, VoxelShape>builder()
																				.put(Direction.UP, Block.createCuboidShape(6D, 10D, 6D, 10D, 16D, 10D))
																				.put(Direction.DOWN, Block.createCuboidShape(6D, 0D, 6D, 10D, 6D, 10D))
																				.put(Direction.NORTH, Block.createCuboidShape(6D, 6D, 0D, 10D, 10D, 6D))
																				.put(Direction.SOUTH, Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 16D))
																				.put(Direction.EAST, Block.createCuboidShape(10D, 6D, 6D, 16D, 10D, 10D))
																				.put(Direction.WEST, Block.createCuboidShape(0D, 6D, 6D, 6D, 10D, 10D))
																				.build();
	
	public static final Map<Direction, VoxelShape> CONNECTOR_SHAPE_MAP = ImmutableMap.<Direction, VoxelShape>builder()
																					 .put(Direction.UP, VoxelShapes.cuboid(0.375D, 0.875D, 0.3125D, 0.75D, 1.0625D, 0.6875D))
																					 .put(Direction.DOWN, VoxelShapes.cuboid(0.3125D, -0.0625D, 0.3125D, 0.6875D, 0.125D, 0.6875D))
																					 .put(Direction.NORTH, VoxelShapes.cuboid(0.3125D, 0.3125D, -0.0625D, 0.6875D, 0.6875D, 0.125D))
																					 .put(Direction.SOUTH, VoxelShapes.cuboid(0.3125D, 0.3125D, 0.875D, 0.6875D, 0.6875D, 1.0625D))
																					 .put(Direction.EAST, VoxelShapes.cuboid(0.875D, 0.3125D, 0.3125D, 1.0625D, 0.6875D, 0.6875D))
																					 .put(Direction.WEST, VoxelShapes.cuboid(-0.0625D, 0.3125D, 0.3125D, 0.125D, 0.6875D, 0.6875D))
																					 .build();
	
	public static final VoxelShape CENTER_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	
	protected static final Int2ObjectArrayMap<VoxelShape> SHAPE_CACHE = new Int2ObjectArrayMap<>();
	
	/**
	 * Instantiates a {@link CableBlock}.
	 */
	public CableBlock(Settings settings) {
		super(settings);
		
		setDefaultState(getDefaultState().with(WATERLOGGED, false));
	}
	
	/**
	 * Returns this {@link CableBlock}'s {@link CableBlockEntity}.
	 */
	public CableBlockEntity getBlockEntity(World world, BlockPos pos) {
		return (CableBlockEntity) world.getBlockEntity(pos);
	}
	
	/**
	 * Updates this {@link CableBlock}'s connections.
	 */
	public void updateConnections(CableBlockEntity blockEntity, World world, BlockPos pos) {
		if (blockEntity == null) {
			return;
		}
		
		var connections = blockEntity.getConnections();
		
		if (connections == null) {
			return;
		}
		
		for (var direction : Direction.values()) {
			connections.setSide(direction, false);
			connections.setConnection(direction, false);
			
			var offsetState = world.getBlockState(pos.offset(direction));
			var offsetBlock = offsetState.getBlock();
			
			if (offsetBlock == this) {
				connections.setSide(direction, true);
			}
			
			var offsetStorage = getNetworkType().find(new WorldPos(world, pos.offset(direction)), direction.getOpposite());
			
			if (offsetStorage != null) {
				connections.setSide(direction, true);
				connections.setConnection(direction, true);
			}
		}
		
		BlockEntityHooks.syncData(blockEntity);
	}
	
	/**
	 * Updates this {@link CableBlock}'s connections.
	 */
	public void updateConnections(World world, BlockPos pos) {
		updateConnections((CableBlockEntity) world.getBlockEntity(pos), world, pos);
	}
	
	/**
	 * Toggles this {@link CableBlock}'s connection type.
	 */
	public void toggleConnectionType(World world, BlockPos pos, Direction direction) {
		if (!world.isClient) {
			var blockEntity = (CableBlockEntity) world.getBlockEntity(pos);
			
			if (blockEntity == null) {
				return;
			}
			
			if (System.currentTimeMillis() - blockEntity.getLastToggledMillis() < 50) {
				return;
			}
			
			blockEntity.setLastToggledMillis(System.currentTimeMillis());
			
			var connections = blockEntity.getConnections();
			
			if (connections == null) {
				return;
			}
			
			var networkComponent = WorldNetworkComponent.get(world);
			
			if (networkComponent == null) {
				return;
			}
			
			var network = networkComponent.get(getNetworkType(), pos);
			
			if (network == null) {
				return;
			}
			
			var foundMember = (Network.Member) null;
			
			for (var member : network.members) {
				var newMember = (Network.Member) member;
				
				if (newMember.getBlockPos().equals(pos.offset(direction))) {
					foundMember = newMember;
				}
			}
			
			if (foundMember == null) {
				return;
			}
			
			if (connections.isInsertExtract(direction)) {
				connections.setInsertExtract(direction, false);
				connections.setExtract(direction, false);
				connections.setInsert(direction, false);
				
				foundMember.setSiding(StorageSiding.NONE);
			} else if (connections.isExtract(direction)) {
				connections.setInsertExtract(direction, true);
				connections.setExtract(direction, false);
				connections.setInsert(direction, false);
				
				foundMember.setSiding(StorageSiding.INSERT_EXTRACT);
			} else if (connections.isInsert(direction)) {
				connections.setInsertExtract(direction, false);
				connections.setExtract(direction, true);
				connections.setInsert(direction, false);
				
				foundMember.setSiding(StorageSiding.EXTRACT);
			} else {
				connections.setInsertExtract(direction, false);
				connections.setExtract(direction, false);
				connections.setInsert(direction, true);
				
				foundMember.setSiding(StorageSiding.INSERT);
			}
			
			BlockEntityHooks.syncData(blockEntity);
		}
	}
	
	/**
	 * Returns this {@link CableBlock}'s {@link NetworkType}.
	 */
	public abstract <T extends NetworkType> T getNetworkType();
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (getNetworkType().hasSiding()) {
			if (!world.isClient) {
				// Hit position is relative to the world.
				var hitPos = hit.getPos();
				
				// Make it local.
				hitPos = hitPos.subtract(pos.getX(), pos.getY(), pos.getZ());
				
				for (var direction : Direction.values()) {
					if (CONNECTOR_SHAPE_MAP.get(direction).getBoundingBox().expand(0.01F).contains(hitPos)) {
						toggleConnectionType(world, pos, direction);
						
						return ActionResult.SUCCESS;
					}
				}
			}
		}
		
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	/**
	 * Override behavior to update the {@link BlockState} properties and re-trace the network.
	 */
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		
		if (!world.isClient) {
			NetworkUtils.trace(getNetworkType(), new WorldPos(world, pos));
			
			updateConnections(world, pos);
		}
	}
	
	/**
	 * Override behavior to update the connections of neighbors and re-trace the network.
	 */
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		
		if (!world.isClient) {
			if (state.getBlock() == newState.getBlock()) {
				return;
			}
			
			var networkComponent = WorldNetworkComponent.get(world);
			
			var network = networkComponent.get(getNetworkType(), pos);
			
			if (network != null) {
				network.nodes.clear();
				network.members.clear();
				
				networkComponent.remove(network);
			}
			
			for (var direction : Direction.values()) {
				var offsetPos = pos.offset(direction);
				var offsetBlock = world.getBlockState(offsetPos).getBlock();
				
				if (!(offsetBlock instanceof CableBlock)) {
					continue;
				}
				if (((CableBlock) offsetBlock).getNetworkType() != getNetworkType()) {
					continue;
				}
				
				updateConnections(world, offsetPos);
			}
			
			updateConnections(world, pos);
		}
	}
	
	/**
	 * Override behavior to update the connections and re-trace the network.
	 */
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPosition, boolean moved) {
		super.neighborUpdate(state, world, pos, block, neighborPosition, moved);
		
		if (!world.isClient) {
			var networkComponent = WorldNetworkComponent.get(world);
			
			var network = networkComponent.get(getNetworkType(), pos);
			
			if (network != null) {
				network.nodes.clear();
				network.members.clear();
				
				networkComponent.remove(network);
			}
			
			NetworkUtils.trace(getNetworkType(), new WorldPos(world, pos));
			
			updateConnections(world, pos);
		}
	}
	
	/**
	 * Override behavior to add the {@link Properties#WATERLOGGED} property and our cardinal properties.
	 */
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
	
	/**
	 * Override behavior to return a {@link VoxelShape} based on the {@link BlockState}'s properties.
	 */
	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext entityContext) {
		if (!(world instanceof World)) {
			return VoxelShapes.empty();
		}
		
		var sideDirections = new HashSet<Direction>();
		var connectorDirections = new HashSet<Direction>();
		
		var blockEntity = getBlockEntity((World) world, pos);
		
		if (blockEntity == null) {
			return VoxelShapes.empty();
		}
		
		var connections = blockEntity.getConnections();
		
		if (connections == null) {
			return VoxelShapes.empty();
		}
		
		for (var direction : Direction.values()) {
			if (connections.hasSide(direction)) {
				sideDirections.add(direction);
			}
			
			if (connections.hasConnector(direction)) {
				connectorDirections.add(direction);
			}
		}
		
		return getVoxelShape(sideDirections, connectorDirections);
	}
	
	/**
	 * Returns a {@link VoxelShape} with {@code directions} as {@link CableBlock} shapes.
	 */
	private static VoxelShape toVoxelShape(int directions, VoxelShape shape) {
		for (var direction : Direction.values()) {
			if ((directions & (0x1 << direction.getId())) != 0) {
				shape = VoxelShapes.union(shape, CableBlock.SIDE_SHAPE_MAP.get(direction));
			}
			
			if ((directions & (0x1 << (direction.getId() + 6))) != 0) {
				shape = VoxelShapes.union(shape, CableBlock.CONNECTOR_SHAPE_MAP.get(direction));
			}
		}
		return shape;
	}
	
	/**
	 * Returns a {@link VoxelShape} with {@code directions} as {@link CableBlock} shapes, also caches the shapes.
	 */
	public static VoxelShape getVoxelShape(Set<Direction> sideDirections, Set<Direction> connectorDirections) {
		var i = 0;
		
		for (var direction : sideDirections) {
			i |= 1 << direction.getId();
		}
		
		for (var direction : connectorDirections) {
			i |= 1 << (direction.getId() + 6);
		}
		
		return getVoxelShape(i);
	}
	
	/**
	 * Returns a {@link VoxelShape} with {@code directions} as {@link CableBlock} shapes, also caches the shapes.
	 */
	public static VoxelShape getVoxelShape(int directions) {
		var shape = SHAPE_CACHE.get(directions);
		
		if (shape != null) {
			return shape;
		}
		
		SHAPE_CACHE.put(directions, toVoxelShape(directions, CableBlock.CENTER_SHAPE));
		
		return SHAPE_CACHE.get(directions);
	}
	
	/**
	 * Override behavior to implement {@link Waterloggable}.
	 */
	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}
	
	/**
	 * Override behavior to implement {@link Waterloggable}.
	 */
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(WATERLOGGED, false);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@org.jetbrains.annotations.Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CableBlockEntity(pos, state);
	}
}
