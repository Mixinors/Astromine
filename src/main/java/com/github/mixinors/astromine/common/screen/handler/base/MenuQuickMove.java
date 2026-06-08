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

package com.github.mixinors.astromine.common.screen.handler.base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public final class MenuQuickMove {
	private static final int PLAYER_SLOT_COUNT = 36;
	private static final int PLAYER_MAIN_SLOT_COUNT = 27;

	private MenuQuickMove() {
	}

	public static ItemStack moveWithPlayerSlotsFirst(AbstractContainerMenu menu, Player player, int index, StackMover mover) {
		return move(menu, player, index, 0, PLAYER_SLOT_COUNT, PLAYER_SLOT_COUNT, menu.slots.size(), mover);
	}

	public static ItemStack moveWithPlayerSlotsLast(AbstractContainerMenu menu, Player player, int index, StackMover mover) {
		var playerStart = Math.max(0, menu.slots.size() - PLAYER_SLOT_COUNT);
		return move(menu, player, index, playerStart, menu.slots.size(), 0, playerStart, mover);
	}

	private static ItemStack move(AbstractContainerMenu menu, Player player, int index, int playerStart, int playerEnd, int containerStart, int containerEnd, StackMover mover) {
		if (index < 0 || index >= menu.slots.size()) {
			return ItemStack.EMPTY;
		}

		var slot = menu.slots.get(index);

		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		var stack = slot.getItem();
		var original = stack.copy();
		var fromPlayer = index >= playerStart && index < playerEnd;

		if (fromPlayer) {
			if (!mover.move(stack, containerStart, containerEnd, false) && !moveWithinPlayerInventory(index, stack, playerStart, mover)) {
				return ItemStack.EMPTY;
			}
		} else if (!mover.move(stack, playerStart, playerEnd, true)) {
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		if (stack.getCount() == original.getCount()) {
			return ItemStack.EMPTY;
		}

		slot.onTake(player, stack);

		return original;
	}

	private static boolean moveWithinPlayerInventory(int index, ItemStack stack, int playerStart, StackMover mover) {
		var mainStart = playerStart;
		var mainEnd = playerStart + PLAYER_MAIN_SLOT_COUNT;
		var hotbarStart = mainEnd;
		var hotbarEnd = playerStart + PLAYER_SLOT_COUNT;

		if (index < mainEnd) {
			return mover.move(stack, hotbarStart, hotbarEnd, false);
		}

		return mover.move(stack, mainStart, mainEnd, false);
	}

	@FunctionalInterface
	public interface StackMover {
		boolean move(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection);
	}
}
