package com.github.mixinors.astromine.common.block.dynamic;

import com.github.mixinors.astromine.registry.common.AMProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DynamicWallBlock extends WallBlock {
	public DynamicWallBlock(Properties settings) {
		super(settings);
		
		registerDefaultState(defaultBlockState().setValue(AMProperties.DYNAMIC, true));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		
		builder.add(AMProperties.DYNAMIC);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(AMProperties.DYNAMIC, false);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return super.getShape(shapeState(state), world, pos, context);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return super.getCollisionShape(shapeState(state), world, pos, context);
	}
	
	private static BlockState shapeState(BlockState state) {
		return state.hasProperty(AMProperties.DYNAMIC) ? state.setValue(AMProperties.DYNAMIC, false) : state;
	}
}
