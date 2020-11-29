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

package com.github.chainmailstudios.astromine. registry.client;

import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import static com.github.chainmailstudios.astromine.common.utilities.NumberUtilities.shorten;

public class AstromineClientCallbacks {
	public static void initialize() {
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof FluidVolumeItem) {
				FluidComponent fluidComponent = FluidComponent.get(stack);

				if (fluidComponent != null) {
					fluidComponent.forEachIndexed((slot, volume) -> {
						MutableComponent text = new TextComponent(shorten(volume.getAmount().doubleValue()) + "/" + shorten(volume.getSize().doubleValue()) + " " + new TranslatableComponent(String.format("block.%s.%s", volume.getFluidId().getNamespace(), volume.getFluidId().getPath())).getString());

						tooltip.add(1, text);
					});
				}
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof EnergyVolumeItem) {
				EnergyHandler handler = Energy.of(stack);

				MutableComponent text = new TextComponent(shorten(handler.getEnergy()) + "/" + shorten(((EnergyVolumeItem) stack.getItem()).getMaxStoredPower()) + " E");

				tooltip.add(1, text);
			}
		});
	}
}
