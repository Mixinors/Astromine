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

package com.github.chainmailstudios.astromine.common.item.base;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;

import java.util.List;

public class FluidVolumeItem extends BaseVolumeItem {
	public FluidVolumeItem(Settings settings) {
		super(settings);
	}

	public static FluidVolumeItem of(Item.Settings settings, Fraction size) {
		FluidVolumeItem item = new FluidVolumeItem(settings);
		item.size = size;
		return item;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		FluidInventoryComponent fluidComponent = ComponentProvider.fromItemStack(stack).getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		fluidComponent.getContents().forEach((key, value) -> {
			tooltip.add(new LiteralText(value.getFraction().toFractionalString() + " | " + new TranslatableText(value.getFluid().getDefaultState().getBlockState().getBlock().getTranslationKey()).getString()).formatted(Formatting.GRAY));
		});
	}
}
