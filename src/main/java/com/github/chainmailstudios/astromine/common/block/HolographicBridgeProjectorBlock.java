package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.HolographicBridgeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.world.BlockView;

public class HolographicBridgeProjectorBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public HolographicBridgeProjectorBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.appendProperties(builder);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new HolographicBridgeBlockEntity();
	}

	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
	}
}
