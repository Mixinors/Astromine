package com.github.chainmailstudios.astromine.common.container.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.container.BaseContainer;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class DeltaContainer extends BaseContainer {
	public final Collection<WSlot> playerSlots;

	public DefaultedEnergyBlockEntity blockEntity;

	public DeltaContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		blockEntity = (DefaultedEnergyBlockEntity) world.getBlockEntity(position);
	}
}