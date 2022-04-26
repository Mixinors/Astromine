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
import com.google.common.collect.Lists;

import com.github.mixinors.astromine.client.render.sky.SpaceSkyProperties;
import com.github.mixinors.astromine.common.callback.SkyPropertiesCallback;
import com.github.mixinors.astromine.common.item.HolographicConnectorItem;
import com.github.mixinors.astromine.common.item.base.EnergyStorageItem;
import com.github.mixinors.astromine.common.util.TextUtils;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import team.reborn.energy.api.base.SimpleBatteryItem;

import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class AMClientCallbacks {
	public static void init() {
		ItemTooltipCallback.EVENT.register( ( stack, context, tooltip ) -> {
			// if (stack.getItem() instanceof FluidVolumeItem) {
			// 	var fluidStorage = SimpleFluidStorage.get(stack);
//
			// 	if (fluidStorage != null) {
			// 		var volume = fluidStorage.getFirst();
			// 		var fluidId = volume.getFluidId();
//
			// 		tooltip.addAll(Math.min(tooltip.size(), 1), Lists.newArrayList(
			// 				((MutableText) TextUtils.getFluidVolume(FluidVolume.of(volume.getAmount() / 81L, volume.getSize() / 81L, volume.getFluid()))).append(new LiteralText(" ")).append(
			// 						((MutableText) TextUtils.getFluid(fluidId)).formatted(Formatting.GRAY))));
			// 	}
			// }
		});

		ItemTooltipCallback.EVENT.register( ( stack, context, tooltip ) -> {
			if (stack.getItem() instanceof EnergyStorageItem item) {
				tooltip.addAll(Math.min(tooltip.size(), 1), Lists.newArrayList(
						TextUtils.getEnergy(SimpleBatteryItem.getStoredEnergyUnchecked(stack), item.getEnergyCapacity())
				));
			}
		});

		ItemTooltipCallback.EVENT.register( ( stack, context, tooltip ) -> {
			if (stack.getItem() instanceof HolographicConnectorItem) {
				var pair = ((HolographicConnectorItem) stack.getItem()).readBlock(stack);
				
				if (pair != null) {
					tooltip.add(Text.of(null));
					tooltip.add(new TranslatableText("text.astromine.selected.dimension.pos", pair.getLeft().getValue(), pair.getRight().getX(), pair.getRight().getY(), pair.getRight().getZ()).formatted(Formatting.GRAY));
				}
			}
		});
		
		ItemTooltipCallback.EVENT.register( ( stack, context, tooltip ) -> {
			if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof EnergyCableBlock cableBlock) {
				tooltip.add(new TranslatableText("text.astromine.tooltip.cable.speed", cableBlock.getNetworkType().getTransferRate()).formatted(Formatting.GRAY));
			}
		});

		ItemTooltipCallback.EVENT.register( ( stack, context, tooltip ) -> {
			// if (stack.getItem() instanceof SpaceSuitItem) {
			// 	if (stack.getItem() == AMItems.SPACE_SUIT_CHESTPLATE.get()) {
			// 		var fluidStorage = SimpleFluidStorage.get(stack);
			// 		if (fluidStorage != null) {
			// 			fluidStorage.forEachIndexed((slot, volume) -> {
			// 				tooltip.add(((MutableText) TextUtils.getFluidVolume(volume)).append(new LiteralText(" ")).append(((MutableText) TextUtils.getFluid(volume.getFluidId())).formatted(Formatting.GRAY)));
			// 			});
			// 		}
			// 	}
			// }
		});
		
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AMWorlds.EARTH_SPACE_ID, new SpaceSkyProperties()));
	}
}
