package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.EnergyWireConnectorBlockEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Arrays;

public class EnergyWireConnectorBlock extends WireConnectorBlock implements BlockEntityProvider {
	private static final ImmutableMap<Direction, VoxelShape> SHAPES = ImmutableMap.<Direction, VoxelShape>builder()
			.put(Direction.UP, Block.createCuboidShape(5, 0, 5, 11, 4, 11))
			.put(Direction.DOWN, Block.createCuboidShape(5, 12, 5, 11, 16, 11))
			.put(Direction.SOUTH, Block.createCuboidShape(5, 5, 0, 11, 11, 4))
			.put(Direction.NORTH, Block.createCuboidShape(5, 5, 12, 11, 11, 16))
			.put(Direction.EAST, Block.createCuboidShape(0, 5, 5, 4, 11, 11))
			.put(Direction.WEST, Block.createCuboidShape(12, 5, 5, 16, 11, 11)).build();

	public EnergyWireConnectorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new EnergyWireConnectorBlockEntity();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(FacingBlock.FACING));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(FacingBlock.FACING));
	}
}
