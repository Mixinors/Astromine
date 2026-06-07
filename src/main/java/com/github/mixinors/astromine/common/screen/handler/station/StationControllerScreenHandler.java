package com.github.mixinors.astromine.common.screen.handler.station;

import com.github.mixinors.astromine.common.block.entity.station.StationControllerBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class StationControllerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final StationControllerBlockEntity stationController;
	
	public StationControllerScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.STATION_CONTROLLER, syncId, player, position);
		
		stationController = (StationControllerBlockEntity) blockEntity;
	}
}
