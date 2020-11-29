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

package com.github.chainmailstudios.astromine.technologies.registry.client;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientCallbacks;
import com.github.chainmailstudios.astromine.technologies.common.item.HolographicConnectorItem;

public class AstromineTechnologiesClientCallbacks extends AstromineClientCallbacks {
	public static void initialize() {
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if (stack.getItem() instanceof HolographicConnectorItem) {
				Tuple<ResourceKey<Level>, BlockPos> pair = ((HolographicConnectorItem) stack.getItem()).readBlock(stack);
				if (pair != null) {
					tooltip.add(Component.nullToEmpty(null));
					tooltip.add(new TranslatableComponent("text.astromine.selected.dimension.pos", pair.getA().location(), pair.getB().getX(), pair.getB().getY(), pair.getB().getZ()).withStyle(ChatFormatting.GRAY));
				}
			}
		}));
	}
}
