package com.github.mixinors.astromine.common.screen.handler.rocket;

import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class RocketControllerScreenHandler extends ExtendedBlockEntityScreenHandler {
	public RocketControllerScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.ROCKET_CONTROLLER, syncId, player, position);
	}
}
