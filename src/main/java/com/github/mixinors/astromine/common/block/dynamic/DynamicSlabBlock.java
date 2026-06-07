package com.github.mixinors.astromine.common.block.dynamic;

import com.github.mixinors.astromine.registry.common.AMProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class DynamicSlabBlock extends SlabBlock {
	public DynamicSlabBlock(Properties settings) {
		super(settings);
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
}
