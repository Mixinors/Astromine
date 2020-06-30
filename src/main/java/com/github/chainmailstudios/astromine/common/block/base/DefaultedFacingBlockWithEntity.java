package com.github.chainmailstudios.astromine.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

public abstract class DefaultedFacingBlockWithEntity extends DefaultedBlockWithEntity {
	public DefaultedFacingBlockWithEntity(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(getDirectionProperty());
		super.appendProperties(builder);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(getDirectionProperty(), context.getPlayerLookDirection().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(getDirectionProperty(), rotation.rotate(state.get(getDirectionProperty())));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(getDirectionProperty())));
	}
	
	protected DirectionProperty getDirectionProperty() {
		return Properties.FACING;
	}
}
