package com.github.chainmailstudios.astromine.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;

public abstract class DefaultedHorizontalFacingBlock extends HorizontalFacingBlock {
	public DefaultedHorizontalFacingBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.appendProperties(builder);
	}
}
