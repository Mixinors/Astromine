/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.screen.handler;

import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RecipeCreatorScreenHandler extends AbstractContainerMenu {
	private final Container inventory = new SimpleContainer(10);
	
	public Container getInventory() {
		return inventory;
	}
	
	public RecipeCreatorScreenHandler(int syncId, @NotNull Player player) {
		super(AMScreenHandlers.RECIPE_CREATOR.get(), syncId);
		
		for (var row = 0; row < 3; ++row) {
			for (var column = 0; column < 3; ++column) {
				addSlot(new Slot(inventory, column + row * 3, 8 + column * 18, 17 + row * 18));
			}
		}
		
		addSlot(new Slot(inventory, 9, 69, 35));
		addPlayerInventory(player.getInventory(), 8, 97);
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
	
	private void addPlayerInventory(Inventory inventory, int x, int y) {
		for (var row = 0; row < 3; ++row) {
			for (var column = 0; column < 9; ++column) {
				addSlot(new Slot(inventory, column + row * 9 + 9, x + column * 18, y + row * 18));
			}
		}
		
		for (var column = 0; column < 9; ++column) {
			addSlot(new Slot(inventory, column, x + column * 18, y + 58));
		}
	}
}
