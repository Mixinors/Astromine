package com.github.chainmailstudios.astromine.common.screenhandler.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;

import spinnery.common.handler.BaseScreenHandler;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class DefaultedBlockStateScreenHandler extends BaseScreenHandler {
	public final Collection<WSlot> playerSlots;

	BlockState state;

	public DefaultedBlockStateScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		state = world.getBlockState(position);
	}
}
