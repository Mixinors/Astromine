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

package com.github.chainmailstudios.astromine.common.volume.handler;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.function.Consumer;

public class ItemHandler {
	private final ItemInventoryComponent component;

	private ItemHandler(ItemInventoryComponent component) {
		this.component = component;
	}

	public ItemStack getStack(int slot) {
		return component.getStack(slot);
	}

	public void setVolume(int slot, ItemStack stack) {
		component.setStack(slot, stack);
	}

	public ItemHandler withStack(int slot, Consumer<Optional<ItemStack>> consumer) {
		consumer.accept(Optional.ofNullable(component.getStack(slot)));

		return this;
	}

	public ItemStack getFirst() {
		return getStack(0);
	}

	public ItemStack getSecond() {
		return getStack(1);
	}

	public ItemStack getThird() {
		return getStack(2);
	}

	public ItemStack getFourth() {
		return getStack(3);
	}

	public ItemStack getFifth() {
		return getStack(4);
	}

	public ItemStack getSixth() {
		return getStack(5);
	}

	public ItemStack getSeventh() {
		return getStack(6);
	}

	public ItemStack getEight() {
		return getStack(7);
	}

	public void setFirst(ItemStack volume) {
		setVolume(0, volume);
	}

	public void setSecond(ItemStack volume) {
		setVolume(1, volume);
	}

	public void setThird(ItemStack volume) {
		setVolume(2, volume);
	}

	public void setFourth(ItemStack volume) {
		setVolume(3, volume);
	}

	public void setFifth(ItemStack volume) {
		setVolume(4, volume);
	}

	public void setSixth(ItemStack volume) {
		setVolume(5, volume);
	}

	public void setSeventh(ItemStack volume) {
		setVolume(6, volume);
	}

	public void setEight(ItemStack volume) {
		setVolume(7, volume);
	}

	public void setNinth(ItemStack volume) {
		setVolume(8, volume);
	}

	public ItemStack getNinth() {
		return getStack(8);
	}

	public ItemHandler forEach(Consumer<ItemStack> stack) {
		component.getContents().values().forEach(stack);

		return this;
	}

	public static ItemHandler of(Object object) {
		return ofOptional(object).get();
	}

	public static Optional<ItemHandler> ofOptional(Object object) {
		if (object instanceof ComponentProvider) {
			ComponentProvider provider = (ComponentProvider) object;

			if (provider.hasComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)) {
				ItemInventoryComponent component = provider.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);

				if (component != null) {
					return Optional.of(new ItemHandler(component));
				}
			}
		} else if (object instanceof ItemInventoryComponent) {
			return Optional.of(new ItemHandler((ItemInventoryComponent) object));
		}

		return Optional.empty();
	}
}
