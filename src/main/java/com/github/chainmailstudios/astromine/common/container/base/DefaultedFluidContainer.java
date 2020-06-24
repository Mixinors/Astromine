package com.github.chainmailstudios.astromine.common.container.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedFluidBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.container.BaseContainer;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class DefaultedFluidContainer extends DefaultedBlockEntityContainer {
	public final Collection<WSlot> playerSlots;

	public DefaultedFluidBlockEntity blockEntity;

	public DefaultedFluidContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		blockEntity = (DefaultedFluidBlockEntity) world.getBlockEntity(position);
	}
}
