package com.github.chainmailstudios.astromine.common.container.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.container.BaseContainer;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class EpsilonContainer extends BaseContainer {
	public final Collection<WSlot> playerSlots;

	BlockState state;

	public EpsilonContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		state = world.getBlockState(position);
	}
}
