package com.github.chainmailstudios.astromine.common.screenhandler.base;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class DefaultedEnergyItemScreenHandler extends DefaultedBlockEntityScreenHandler {
	public final Collection<WSlot> playerSlots;

	public DefaultedEnergyItemBlockEntity blockEntity;

	public DefaultedEnergyItemScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		blockEntity = (DefaultedEnergyItemBlockEntity) world.getBlockEntity(position);

		addInventory(1, ItemInventoryFromInventoryComponent.of(blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));
	}
}
