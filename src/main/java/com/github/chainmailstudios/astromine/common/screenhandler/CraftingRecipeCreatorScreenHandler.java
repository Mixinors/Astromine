/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
