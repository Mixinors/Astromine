package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedItemScreenHandler;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import spinnery.widget.WSlot;

public class BufferScreenHandler extends DefaultedItemScreenHandler {
	public final BufferType bufferType;

	public BufferScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position, BufferType bufferType) {
		super(synchronizationID, playerInventory, position);

		addInventory(1, ItemInventoryFromInventoryComponent.of(blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));

		WSlot.addHeadlessArray(getInterface(), 0, 1, 9, bufferType.getHeight());

		this.bufferType = bufferType;
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.BUFFER;
	}
}
