package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import spinnery.common.handler.BaseScreenHandler;
import spinnery.common.inventory.BaseInventory;
import spinnery.widget.WSlot;

public class CraftingRecipeCreatorScreenHandler extends BaseScreenHandler {
	private static final int INPUT = 1;
	private static final int OUTPUT = 2;

	public CraftingRecipeCreatorScreenHandler(int synchronizationID, PlayerInventory playerInventory) {
		super(synchronizationID, playerInventory);

		addInventory(INPUT, new BaseInventory(9));
		addInventory(OUTPUT, new BaseInventory(1));

		WSlot.addHeadlessPlayerInventory(getInterface());

		WSlot.addHeadlessArray(getInterface(), 0, INPUT, 3, 3);

		getInterface().createChild(WSlot::new).setInventoryNumber(OUTPUT).setSlotNumber(0);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.CRAFTING_RECIPE_CREATOR;
	}
}
