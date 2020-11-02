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

package com.github.chainmailstudios.astromine.registry.client;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.NumberUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import team.reborn.energy.EnergyHandler;

public class AstromineClientCallbacks {
	public static void initialize() {
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof FluidVolumeItem) {
				FluidComponent fluidComponent = FluidComponent.get(stack);

				if (fluidComponent != null) {
					fluidComponent.forEach((entry) -> {
						int slot = entry.getKey();

						FluidVolume volume = entry.getValue();

						tooltip.add(new LiteralText(slot + " - " + NumberUtilities.shorten(volume.getAmount().doubleValue(), "") + "/" + NumberUtilities.shorten(volume.getSize().doubleValue(), "") + " " + new TranslatableText(String.format("block.%s.%s", volume.getFluidId()
							.getNamespace(), volume.getFluidId().getPath())).getString()).formatted(Formatting.GRAY));
					});
				}
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof EnergyVolumeItem) {
				EnergyHandler handler = EnergyUtilities.ofNullable(stack);
				tooltip.add(Math.min(tooltip.size(), 1), new LiteralText(NumberUtilities.shorten(handler.getEnergy(), "") + "/" + NumberUtilities.shorten(((EnergyVolumeItem) stack.getItem()).getMaxStoredPower(), "")).formatted(Formatting.GRAY));
			}
		});
	}
}
