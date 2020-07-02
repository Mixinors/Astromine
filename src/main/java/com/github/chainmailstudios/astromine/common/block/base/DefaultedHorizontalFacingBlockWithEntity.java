package com.github.chainmailstudios.astromine.common.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;

public abstract class DefaultedHorizontalFacingBlockWithEntity extends DefaultedFacingBlockWithEntity {
	public DefaultedHorizontalFacingBlockWithEntity(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(getDirectionProperty(), context.getPlayerFacing().getOpposite());
	}
	
	@Override
	protected DirectionProperty getDirectionProperty() {
		return Properties.HORIZONTAL_FACING;
	}
}
