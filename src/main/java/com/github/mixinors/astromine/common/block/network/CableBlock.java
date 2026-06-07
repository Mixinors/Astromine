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
import com.github.mixinors.astromine.common.component.world.NetworksComponent;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.util.DirectionUtils;
import com.github.mixinors.astromine.common.util.NetworkUtils;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public abstract class CableBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final Map<Direction, VoxelShape> SIDE_SHAPE_MAP = ImmutableMap.of(Direction.UP, Block.box(6D, 10D, 6D, 10D, 16D, 10D),
																		   			Direction.DOWN, Block.box(6D, 0D, 6D, 10D, 6D, 10D),
																		   			Direction.NORTH, Block.box(6D, 6D, 0D, 10D, 10D, 6D),
																		   			Direction.SOUTH, Block.box(6D, 6D, 10D, 10D, 10D, 16D),
																		   			Direction.EAST, Block.box(10D, 6D, 6D, 16D, 10D, 10D),
																		   			Direction.WEST, Block.box(0D, 6D, 6D, 6D, 10D, 10D));
	
	public static final Map<Direction, VoxelShape> CONNECTOR_SHAPE_MAP = ImmutableMap.of(Direction.UP, Shapes.box(0.375D, 0.875D, 0.3125D, 0.75D, 1.0625D, 0.6875D),
																						 Direction.DOWN, Shapes.box(0.3125D, -0.0625D, 0.3125D, 0.6875D, 0.125D, 0.6875D),
																						 Direction.NORTH, Shapes.box(0.3125D, 0.3125D, -0.0625D, 0.6875D, 0.6875D, 0.125D),
																						 Direction.SOUTH, Shapes.box(0.3125D, 0.3125D, 0.875D, 0.6875D, 0.6875D, 1.0625D),
																						 Direction.EAST, Shapes.box(0.875D, 0.3125D, 0.3125D, 1.0625D, 0.6875D, 0.6875D),
																						 Direction.WEST, Shapes.box(-0.0625D, 0.3125D, 0.3125D, 0.125D, 0.6875D, 0.6875D));
	
	public static final VoxelShape CENTER_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	
	protected static final Int2ObjectArrayMap<VoxelShape> SHAPE_CACHE = new Int2ObjectArrayMap<>();
	
	public CableBlock(Properties settings) {
		super(settings);
		
		registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return MapCodec.unit(this);
	}
	
	public CableBlockEntity getBlockEntity(Level world, BlockPos pos) {
		return (CableBlockEntity) world.getBlockEntity(pos);
	}
	
	public void updateConnections(CableBlockEntity blockEntity, Level world, BlockPos pos) {
		if (blockEntity == null) {
			return;
		}
		
		var connections = blockEntity.getConnections();
		
		if (connections == null) {
			return;
		}
		
		for (var direction : DirectionUtils.VALUES) {
			connections.setSide(direction, false);
			connections.setConnection(direction, false);
			
			var offsetState = world.getBlockState(pos.relative(direction));
			var offsetBlock = offsetState.getBlock();
			
			if (offsetBlock == this) {
				connections.setSide(direction, true);
			}
			
			var offsetStorage = getNetworkType().find(world, pos.relative(direction), direction.getOpposite());
			
			if (offsetStorage != null) {
				connections.setSide(direction, true);
				connections.setConnection(direction, true);
			}
		}
		
		blockEntity.syncData();
	}
	
	public void updateConnections(Level world, BlockPos pos) {
		updateConnections((CableBlockEntity) world.getBlockEntity(pos), world, pos);
	}
	
	public void toggleConnectionType(Level world, BlockPos pos, Direction direction) {
		if (!world.isClientSide) {
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
			
			var networkComponent = NetworksComponent.get(world);
			
			if (networkComponent == null) {
				return;
			}
			
			var network = networkComponent.get(getNetworkType(), pos);
			
			if (network == null) {
				return;
			}
			
			var foundMember = (Network.Member) null;
			
			for (var member : network.getMembers()) {
				var newMember = (Network.Member) member;
				
				if (newMember.blockPos().equals(pos.relative(direction))) {
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
				
				network.getMembers().remove(foundMember);
				
				network.getMembers().add(new Network.Member(foundMember.blockPos(), foundMember.direction(), StorageSiding.NONE));
			} else if (connections.isExtract(direction)) {
				connections.setInsertExtract(direction, true);
				connections.setExtract(direction, false);
				connections.setInsert(direction, false);
				
				network.getMembers().remove(foundMember);
				
				network.getMembers().add(new Network.Member(foundMember.blockPos(), foundMember.direction(), StorageSiding.INSERT_EXTRACT));
			} else if (connections.isInsert(direction)) {
				connections.setInsertExtract(direction, false);
				connections.setExtract(direction, true);
				connections.setInsert(direction, false);
				
				network.getMembers().remove(foundMember);
				
				network.getMembers().add(new Network.Member(foundMember.blockPos(), foundMember.direction(), StorageSiding.EXTRACT));
			} else {
				connections.setInsertExtract(direction, false);
				connections.setExtract(direction, false);
				connections.setInsert(direction, true);
				
				network.getMembers().remove(foundMember);
				
				network.getMembers().add(new Network.Member(foundMember.blockPos(), foundMember.direction(), StorageSiding.INSERT));
			}
			
			blockEntity.syncData();
		}
	}
	
	public abstract <T extends NetworkType> T getNetworkType();
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (getNetworkType().hasSiding()) {
			if (!world.isClientSide) {
				// Hit position is relative to the world.
				var hitPos = hit.getLocation();
				
				// Make it local.
				hitPos = hitPos.subtract(pos.getX(), pos.getY(), pos.getZ());
				
				for (var direction : DirectionUtils.VALUES) {
					if (CONNECTOR_SHAPE_MAP.get(direction).bounds().inflate(0.01F).contains(hitPos)) {
						toggleConnectionType(world, pos, direction);
						
						return ItemInteractionResult.SUCCESS;
					}
				}
			}
		}
		
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		
		if (!world.isClientSide) {
			NetworkUtils.trace(getNetworkType(), world, pos);
			
			updateConnections(world, pos);
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		super.onRemove(state, world, pos, newState, moved);
		
		if (!world.isClientSide) {
			if (state.getBlock() == newState.getBlock()) {
				return;
			}
			
			var networkComponent = NetworksComponent.get(world);
			
			var network = networkComponent.get(getNetworkType(), pos);
			
			if (network != null) {
				network.getNodes().clear();
				network.getMembers().clear();
				
				networkComponent.remove(network);
			}
			
			for (var direction : DirectionUtils.VALUES) {
				var offsetPos = pos.relative(direction);
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
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPosition, boolean moved) {
		super.neighborChanged(state, world, pos, block, neighborPosition, moved);
		
		if (!world.isClientSide) {
			var networkComponent = NetworksComponent.get(world);
			
			var network = networkComponent.get(getNetworkType(), pos);
			
			if (network != null) {
				network.getNodes().clear();
				network.getMembers().clear();
				
				networkComponent.remove(network);
			}
			
			NetworkUtils.trace(getNetworkType(), world, pos);
			
			updateConnections(world, pos);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter world, BlockPos pos, CollisionContext entityContext) {
		if (!(world instanceof Level)) {
			return Shapes.empty();
		}
		
		var sideDirections = new HashSet<Direction>();
		var connectorDirections = new HashSet<Direction>();
		
		var blockEntity = getBlockEntity((Level) world, pos);
		
		if (blockEntity == null) {
			return Shapes.empty();
		}
		
		var connections = blockEntity.getConnections();
		
		if (connections == null) {
			return Shapes.empty();
		}
		
		for (var direction : DirectionUtils.VALUES) {
			if (connections.hasSide(direction)) {
				sideDirections.add(direction);
			}
			
			if (connections.hasConnector(direction)) {
				connectorDirections.add(direction);
			}
		}
		
		return getVoxelShape(sideDirections, connectorDirections);
	}
	
	private static VoxelShape toVoxelShape(int directions, VoxelShape shape) {
		for (var direction : DirectionUtils.VALUES) {
			if ((directions & (0x1 << direction.get3DDataValue())) != 0) {
				shape = Shapes.or(shape, CableBlock.SIDE_SHAPE_MAP.get(direction));
			}
			
			if ((directions & (0x1 << (direction.get3DDataValue() + 6))) != 0) {
				shape = Shapes.or(shape, CableBlock.CONNECTOR_SHAPE_MAP.get(direction));
			}
		}
		return shape;
	}
	
	public static VoxelShape getVoxelShape(Set<Direction> sideDirections, Set<Direction> connectorDirections) {
		var i = 0;
		
		for (var direction : sideDirections) {
			i |= 1 << direction.get3DDataValue();
		}
		
		for (var direction : connectorDirections) {
			i |= 1 << (direction.get3DDataValue() + 6);
		}
		
		return getVoxelShape(i);
	}

	public static VoxelShape getVoxelShape(int directions) {
		var shape = SHAPE_CACHE.get(directions);
		
		if (shape != null) {
			return shape;
		}
		
		SHAPE_CACHE.put(directions, toVoxelShape(directions, CableBlock.CENTER_SHAPE));
		
		return SHAPE_CACHE.get(directions);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.hasProperty(WATERLOGGED) && state.getValue(WATERLOGGED)) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(WATERLOGGED, false);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CableBlockEntity(pos, state);
	}
}
