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

package com.github.mixinors.astromine.common.item.base;

import dev.vini2003.hammer.core.api.common.util.FluidTextUtils;
import dev.vini2003.hammer.core.api.common.util.TextUtils;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FluidStorageItem extends Item {
	private final long capacity;

	private FluidStorageItem(Item.Settings settings, long capacity) {
		super(settings);

		this.capacity = capacity;
	}

	public static FluidStorageItem of(Item.Settings settings, long size) {
		return new FluidStorageItem(settings, size);
	}

	public long getCapacity() {
		return capacity;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		var storages = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
		
		var emptyTooltip = new ArrayList<Text>();
		
		try (var transaction = Transaction.openOuter()) {
			for (var storage : storages.iterable(transaction)) {
				if (storage.isResourceBlank()) {
					emptyTooltip.add(TextUtils.EMPTY);
				} else {
					if (context.isAdvanced()) {
						tooltip.addAll(FluidTextUtils.getDetailedStorageTooltips(storage));
					} else {
						tooltip.addAll(FluidTextUtils.getShortenedStorageTooltips(storage));
					}
				}
			}
			
			transaction.abort();
		}
		
		tooltip.addAll(emptyTooltip);
	}
}
