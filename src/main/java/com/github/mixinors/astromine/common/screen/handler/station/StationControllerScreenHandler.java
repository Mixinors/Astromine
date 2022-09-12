package com.github.mixinors.astromine.common.screen.handler.station;

import com.github.mixinors.astromine.common.block.entity.station.StationControllerBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class StationControllerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final StationControllerBlockEntity stationController;
	
	public StationControllerScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.ROCKET_CONTROLLER, syncId, player, position);
		
		stationController = (StationControllerBlockEntity) blockEntity;
	}
	
	@Override
	public void init(int width, int height) {
		super.init(width, height);
	}
}
