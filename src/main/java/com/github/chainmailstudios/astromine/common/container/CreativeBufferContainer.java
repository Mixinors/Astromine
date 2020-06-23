package com.github.chainmailstudios.astromine.common.container;

import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedItemContainer;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import spinnery.widget.WSlot;

public class CreativeBufferContainer extends DefaultedItemContainer {
	public CreativeBufferContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		addInventory(1, ItemInventoryFromInventoryComponent.of(blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));

		getInterface().createChild(WSlot::new).setSlotNumber(0).setInventoryNumber(1);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.CREATIVE_BUFFER;
	}
}
