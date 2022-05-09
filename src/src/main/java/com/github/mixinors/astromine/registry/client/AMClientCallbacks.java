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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.render.sky.SpaceSkyProperties;
import com.github.mixinors.astromine.common.block.network.EnergyCableBlock;
import com.github.mixinors.astromine.common.callback.SkyPropertiesCallback;
import com.github.mixinors.astromine.common.item.HolographicConnectorItem;
import com.github.mixinors.astromine.common.item.base.EnergyStorageItem;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.vini2003.hammer.core.api.common.util.FluidTextUtils;
import dev.vini2003.hammer.core.api.common.util.TextUtils;
import dev.vini2003.hammer.gui.energy.api.common.util.EnergyTextUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;

public class AMClientCallbacks {
	public static void init() {
		ClientTooltipEvent.ITEM.register(((stack, tooltips, context) -> {
			var item = stack.getItem();
			
			var id = Registry.ITEM.getId(item);
			
			if (id.getNamespace().equals(AMCommon.MOD_ID)) {
				var empty = tooltips.stream().filter(text -> text.asString().isEmpty()).findFirst().orElse(null);
				
				var index = empty == null ? tooltips.size() : tooltips.indexOf(empty) + 1;
				
				var fluidStorages = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
				
				if (fluidStorages != null) {
					var emptyTooltip = new ArrayList<Text>();
					
					try (var transaction = Transaction.openOuter()) {
						for (var storage : fluidStorages.iterable(transaction)) {
							if (storage.isResourceBlank()) {
								emptyTooltip.add(TextUtils.EMPTY);
							} else {
								if (context.isAdvanced()) {
									tooltips.addAll(index, FluidTextUtils.getDetailedStorageTooltips(storage));
								} else {
									tooltips.addAll(index, FluidTextUtils.getShortenedStorageTooltips(storage));
								}
							}
						}
						
						transaction.abort();
					}
					
					tooltips.addAll(emptyTooltip);
				}
				
				var energyStorages = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
				
				if (energyStorages != null) {
					if (context.isAdvanced()) {
						tooltips.addAll(index, EnergyTextUtils.getDetailedTooltips(energyStorages));
					} else {
						tooltips.addAll(index, EnergyTextUtils.getShortenedTooltips(energyStorages));
					}
				}
			}
		}));
		
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
