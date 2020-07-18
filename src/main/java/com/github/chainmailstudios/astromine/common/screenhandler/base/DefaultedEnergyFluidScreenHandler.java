package com.github.chainmailstudios.astromine.common.screenhandler.base;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class DefaultedEnergyFluidScreenHandler extends DefaultedBlockEntityScreenHandler {
	public final Collection<WSlot> playerSlots;

	public DefaultedEnergyFluidBlockEntity blockEntity;

	public DefaultedEnergyFluidScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		blockEntity = (DefaultedEnergyFluidBlockEntity) world.getBlockEntity(position);
	}
}
