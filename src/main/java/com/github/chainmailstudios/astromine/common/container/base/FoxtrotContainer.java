package com.github.chainmailstudios.astromine.common.container.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.EpsilionBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.container.BaseContainer;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class FoxtrotContainer extends BaseContainer {
	public final Collection<WSlot> playerSlots;

	public EpsilionBlockEntity blockEntity;

	public FoxtrotContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		blockEntity = (EpsilionBlockEntity) world.getBlockEntity(position);
	}
}
