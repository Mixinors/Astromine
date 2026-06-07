package com.github.mixinors.astromine.common.screen.handler.rocket;

import static com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityMenuLayout.*;

import com.github.mixinors.astromine.common.block.entity.rocket.RocketControllerBlockEntity;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidUtil;

public class RocketControllerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final RocketControllerBlockEntity rocketController;
	
	public RocketControllerScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.ROCKET_CONTROLLER, syncId, player, position);
		
		rocketController = (RocketControllerBlockEntity) blockEntity;
		
		var payloadX = (int) (TABS_WIDTH / 2.0F - SLOT_WIDTH / 2.0F);
		var payloadY = 54;
		
		var leftBarX = payloadX - (int) (BAR_WIDTH + PAD_3);
		var leftBarY = payloadY + (int) (SLOT_HEIGHT + PAD_3);
		var leftInputX = leftBarX - (int) (SLOT_WIDTH + PAD_3);
		var leftBufferX = leftInputX - (int) (SLOT_WIDTH + PAD_3);
		var tankInputY = leftBarY;
		var tankBufferY = tankInputY + (int) (SLOT_HEIGHT - 4.0F);
		var tankOutputY = tankInputY + (int) (SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT + PAD_3);
		
		var rightBarX = payloadX + (int) (SLOT_WIDTH + PAD_3);
		var rightInputX = rightBarX + (int) (BAR_WIDTH + PAD_3);
		var rightBufferX = rightInputX + (int) (SLOT_WIDTH + PAD_3);
		
		addBlockEntityWildOutputSlot(Rocket.PAYLOAD_SLOT, payloadX, payloadY);
		addFluidBar(Rocket.OXYGEN_TANK_FLUID_IN, leftBarX, leftBarY);
		addBlockEntitySlot(Rocket.OXYGEN_TANK_UNLOAD_SLOT, leftInputX, tankInputY, RocketControllerScreenHandler::hasFluidHandler);
		addBlockEntityWildOutputSlot(Rocket.OXYGEN_TANK_BUFFER_SLOT, leftBufferX, tankBufferY);
		addBlockEntityWildSlot(Rocket.OXYGEN_TANK_OUTPUT_SLOT, leftInputX, tankOutputY, RocketControllerScreenHandler::hasFluidHandler);
		
		addFluidBar(Rocket.FUEL_TANK_FLUID_IN, rightBarX, leftBarY);
		addBlockEntitySlot(Rocket.FUEL_TANK_UNLOAD_SLOT, rightInputX, tankInputY, RocketControllerScreenHandler::hasFluidHandler);
		addBlockEntityWildOutputSlot(Rocket.FUEL_TANK_BUFFER_SLOT, rightBufferX, tankBufferY);
		addBlockEntityWildSlot(Rocket.FUEL_TANK_OUTPUT_SLOT, rightInputX, tankOutputY, RocketControllerScreenHandler::hasFluidHandler);
		
		var partY = leftBarY + (int) (BAR_HEIGHT + PAD_25);
		var hintY = partY - (int) (SLOT_HEIGHT + PAD_3);
		var hullX = leftBarX - (int) (SLOT_WIDTH + PAD_3);
		var landingMechanismX = hullX - (int) (SLOT_WIDTH + PAD_3);
		var shieldingX = rightBarX + (int) (SLOT_WIDTH + PAD_3);
		var thrusterX = shieldingX + (int) (SLOT_WIDTH + PAD_3);
		
		addBlockEntitySlot(Rocket.FUEL_TANK_SLOT, leftBarX, partY);
		addBlockEntitySlot(Rocket.HULL_SLOT, hullX, partY);
		addBlockEntitySlot(Rocket.LANDING_MECHANISM_SLOT, landingMechanismX, partY);
		addBlockEntitySlot(Rocket.LIFE_SUPPORT_SLOT, rightBarX, partY);
		addBlockEntitySlot(Rocket.SHIELDING_SLOT, shieldingX, partY);
		addBlockEntitySlot(Rocket.THRUSTER_SLOT, thrusterX, partY);
		
		addItemHint(AMItems.LOW_CAPACITY_ROCKET_FUEL_TANK.get().getDefaultInstance(), leftBarX, hintY, Component.translatable("text.astromine.fuel_tank"));
		addItemHint(AMItems.LOW_DURABILITY_ROCKET_HULL.get().getDefaultInstance(), hullX, hintY, Component.translatable("text.astromine.hull"));
		addItemHint(AMItems.STANDING_ROCKET_LANDING_MECHANISM.get().getDefaultInstance(), landingMechanismX, hintY, Component.translatable("text.astromine.landing_mechanism"));
		addItemHint(AMItems.ROCKET_LIFE_SUPPORT.get().getDefaultInstance(), rightBarX, hintY, Component.translatable("text.astromine.life_support"));
		addItemHint(AMItems.LOW_TEMPERATURE_ROCKET_SHIELDING.get().getDefaultInstance(), shieldingX, hintY, Component.translatable("text.astromine.shielding"));
		addItemHint(AMItems.LOW_EFFICIENCY_ROCKET_THRUSTER.get().getDefaultInstance(), thrusterX, hintY, Component.translatable("text.astromine.thruster"));
	}
	
	private static boolean hasFluidHandler(ItemStack stack) {
		return FluidUtil.getFluidHandler(stack).isPresent();
	}
}
