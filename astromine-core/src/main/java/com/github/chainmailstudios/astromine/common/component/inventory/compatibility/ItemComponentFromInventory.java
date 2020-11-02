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

package com.github.chainmailstudios.astromine.common.component.inventory.compatibility;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemComponentFromInventory extends SimpleItemComponent {
	Inventory inventory;
	List<Runnable> listeners = new ArrayList<>();

	private ItemComponentFromInventory(Inventory inventory) {
		super(inventory.size());
		this.inventory = inventory;
	}

	public static ItemComponentFromInventory of(Inventory inventory) {
		return new ItemComponentFromInventory(inventory);
	}

	@Override
	public Map<Integer, ItemStack> getContents() {
		Int2ObjectArrayMap<ItemStack> contents = new Int2ObjectArrayMap<>();
		for (int i = 0; i < this.inventory.size(); ++i) {
			contents.put(i, this.inventory.getStack(i));
		}
		contents.defaultReturnValue(ItemStack.EMPTY);
		return contents;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.inventory.setStack(slot, stack);
	}

	@Override
	public int getSize() {
		return this.inventory.size();
	}

	@Override
	public List<Runnable> getListeners() {
		return this.listeners;
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.getStack(slot);
	}

	@Override
	public boolean canInsert(@Nullable Direction direction, ItemStack stack, int slot) {
		return this.inventory.isValid(slot, stack);
	}
}
