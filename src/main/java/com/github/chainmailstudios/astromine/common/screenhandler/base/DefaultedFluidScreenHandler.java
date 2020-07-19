package com.github.chainmailstudios.astromine.common.screenhandler.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedFluidBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class DefaultedFluidScreenHandler extends DefaultedBlockEntityScreenHandler {
	public final Collection<WSlot> playerSlots;

	public DefaultedFluidBlockEntity blockEntity;

	public DefaultedFluidScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		blockEntity = (DefaultedFluidBlockEntity) world.getBlockEntity(position);
	}
}
