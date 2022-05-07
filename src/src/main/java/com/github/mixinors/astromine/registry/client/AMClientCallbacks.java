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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.common.block.network.EnergyCableBlock;
import com.github.mixinors.astromine.common.item.base.FluidStorageItem;
import com.google.common.collect.Lists;

import com.github.mixinors.astromine.client.render.sky.SpaceSkyProperties;
import com.github.mixinors.astromine.common.callback.SkyPropertiesCallback;
import com.github.mixinors.astromine.common.item.HolographicConnectorItem;
import com.github.mixinors.astromine.common.item.base.EnergyStorageItem;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.vini2003.hammer.core.api.common.util.TextUtils;
import dev.vini2003.hammer.gui.energy.api.common.util.EnergyTextUtils;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleBatteryItem;

import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class AMClientCallbacks {
	public static void init() {
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof EnergyStorageItem) {
				var energyStorage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
				
				if (energyStorage != null) {
					if (context.isAdvanced()) {
						tooltip.addAll(EnergyTextUtils.getDetailedTooltips(energyStorage));
					} else {
						tooltip.addAll(EnergyTextUtils.getShortenedTooltips(energyStorage));
					}
				}
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof HolographicConnectorItem) {
				var pair = ((HolographicConnectorItem) stack.getItem()).readBlock(stack);
				
				if (pair != null) {
					var key = pair.getLeft();
					var pos = pair.getRight();
					
					tooltip.add(TextUtils.EMPTY);
					tooltip.add(new TranslatableText("text.astromine.selected.dimension.pos", key, pos.getX(), pos.getY(), pos.getZ()).formatted(Formatting.GRAY));
				}
			}
		});
		
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof EnergyCableBlock cableBlock) {
				tooltip.add(new TranslatableText("text.astromine.tooltip.cable.speed", cableBlock.getNetworkType().getTransferRate()).formatted(Formatting.GRAY));
			}
		});
		
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AMWorlds.EARTH_SPACE_ID, new SpaceSkyProperties()));
	}
}
