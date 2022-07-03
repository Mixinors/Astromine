package com.github.mixinors.astromine.common.block.dynamic;

import com.github.mixinors.astromine.registry.common.AMProperties;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallShape;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class DynamicWallBlock extends WallBlock {
	public DynamicWallBlock(Settings settings) {
		super(settings);
		
		setDefaultState(getDefaultState().with(AMProperties.DYNAMIC, true));
		
		this.shapeMap = this.getShapeMap(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
		this.collisionShapeMap = this.getShapeMap(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
		
		var shapesToAddMap = new HashMap<BlockState, VoxelShape>();
		var collisionShapesToAddMap = new HashMap<BlockState, VoxelShape>();
		
		for (var entry : shapeMap.entrySet()) {
			shapesToAddMap.put(entry.getKey().with(AMProperties.DYNAMIC, false), entry.getValue());
		}
		
		for (var entry : collisionShapeMap.entrySet()) {
			collisionShapesToAddMap.put(entry.getKey().with(AMProperties.DYNAMIC, false), entry.getValue());
		}
		
		this.shapeMap = ImmutableMap.<BlockState, VoxelShape>builder().putAll(shapeMap).putAll(shapesToAddMap).build();
		this.collisionShapeMap = ImmutableMap.<BlockState, VoxelShape>builder().putAll(collisionShapeMap).putAll(collisionShapesToAddMap).build();
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		
		builder.add(AMProperties.DYNAMIC);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(AMProperties.DYNAMIC, false);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var shape = this.shapeMap.get(state);
		
		if (shape == null) {
			return VoxelShapes.empty();
		} else {
			return shape;
		}
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var shape = this.collisionShapeMap.get(state);
		
		if (shape == null) {
			return VoxelShapes.empty();
		} else {
			return shape;
		}
	}
}
