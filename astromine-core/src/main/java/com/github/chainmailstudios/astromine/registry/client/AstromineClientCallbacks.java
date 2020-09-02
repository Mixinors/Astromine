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

import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.volume.handler.EnergyHandler;
import com.github.chainmailstudios.astromine.common.volume.handler.FluidHandler;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.BlockItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class AstromineClientCallbacks {
	public static void initialize() {
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof EnergyVolumeItem) {
				FluidHandler.ofOptional(stack).ifPresent(handler -> {
					handler.withVolume(0, optionalVolume -> {
						optionalVolume.ifPresent(volume -> {
							tooltip.add(new LiteralText("" + volume.getAmount().doubleValue() + ":" + volume.getSize()));
						});
					});
				});
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof EnergyVolumeItem) {
				EnergyHandler.ofOptional(stack).ifPresent(handler -> {
					handler.withVolume(0, optionalVolume -> {
						optionalVolume.ifPresent(volume -> {
							tooltip.add(EnergyUtilities.compoundDisplayColored(volume.getAmount(), volume.getSize()));
						});
					});
				});
			}
		});
	}
}
