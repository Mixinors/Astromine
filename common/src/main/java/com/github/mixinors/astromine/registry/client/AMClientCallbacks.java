/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import com.github.mixinors.astromine.client.render.sky.SpaceSkyProperties;
import com.github.mixinors.astromine.common.callback.SkyPropertiesEvents;
import com.github.mixinors.astromine.common.item.HolographicConnectorItem;
import com.github.mixinors.astromine.common.item.SpaceSuitItem;
import com.github.mixinors.astromine.common.network.type.EnergyNetworkType;
import com.github.mixinors.astromine.common.util.TextUtils;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMDimensions;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.google.common.collect.Lists;
import me.shedaniel.architectury.event.events.TooltipEvent;

import net.minecraft.item.BlockItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.item.base.EnergyVolumeItem;
import com.github.mixinors.astromine.common.item.base.FluidVolumeItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

public class AMClientCallbacks {
	public static void init() {
		TooltipEvent.ITEM.register((stack, tooltip, context) -> {
			if (stack.getItem() instanceof FluidVolumeItem) {
				FluidComponent fluidComponent = FluidComponent.get(stack);
				
				FluidVolume volume = fluidComponent.getFirst();
				Identifier fluidId = volume.getFluidId();
				
				tooltip.addAll(Math.min(tooltip.size(), 1), Lists.newArrayList(
						((MutableText) TextUtils.getFluidVolume(FluidVolume.of(volume.getAmount() / 81L, volume.getSize() / 81L, volume.getFluid()))).append(new LiteralText(" ")).append(
								((MutableText) TextUtils.getFluid(fluidId)).formatted(Formatting.GRAY))));
			}
		});

		TooltipEvent.ITEM.register((stack, tooltip, context) -> {
			if (stack.getItem() instanceof EnergyVolumeItem) {
				EnergyHandler handler = Energy.of(stack);
				
				tooltip.addAll(Math.min(tooltip.size(), 1), Lists.newArrayList(
						TextUtils.getEnergyVolume(EnergyVolume.of(handler.getEnergy(), handler.getMaxStored()))
				));
			}
		});

		TooltipEvent.ITEM.register((stack, tooltip, context) -> {
			if (stack.getItem() instanceof HolographicConnectorItem) {
				Pair<RegistryKey<World>, BlockPos> pair = ((HolographicConnectorItem) stack.getItem()).readBlock(stack);
				
				if (pair != null) {
					tooltip.add(Text.of(null));
					tooltip.add(new TranslatableText("text.astromine.selected.dimension.pos", pair.getLeft().getValue(), pair.getRight().getX(), pair.getRight().getY(), pair.getRight().getZ()).formatted(Formatting.GRAY));
				}
			}
		});

		TooltipEvent.ITEM.register((stack, tooltip, context) -> {
			if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof EnergyNetworkType.NodeSpeedProvider) {
				tooltip.add(new TranslatableText("text.astromine.tooltip.cable.speed", ((EnergyNetworkType.NodeSpeedProvider) ((BlockItem) stack.getItem()).getBlock()).getNodeSpeed()).formatted(Formatting.GRAY));
			}
		});

		TooltipEvent.ITEM.register((stack, tooltip, context) -> {
			if (stack.getItem() instanceof SpaceSuitItem) {
				if (stack.getItem() == AMItems.SPACE_SUIT_CHESTPLATE.get()) {
					FluidComponent fluidComponent = FluidComponent.get(stack);
					
					fluidComponent.forEachIndexed((slot, volume) -> {
						tooltip.add(((MutableText) TextUtils.getFluidVolume(volume)).append(new LiteralText(" ")).append(((MutableText) TextUtils.getFluid(volume.getFluidId())).formatted(Formatting.GRAY)));
					});
				}
			}
		});
		
		SkyPropertiesEvents.EVENT.register((properties) -> properties.put(AMDimensions.EARTH_SPACE_ID, new SpaceSkyProperties()));
	}
}
