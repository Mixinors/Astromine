package com.github.mixinors.astromine.common.block.dynamic;

import com.github.mixinors.astromine.registry.common.AMProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.Nullable;

public class DynamicStairsBlock extends StairsBlock {
	public DynamicStairsBlock(BlockState baseBlockState, Settings settings) {
		super(baseBlockState, settings);
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
}
