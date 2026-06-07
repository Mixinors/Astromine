/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemWidget extends Widget {
	private final Supplier<ItemStack> stack;
	private final Supplier<List<Component>> tooltip;
	
	public ItemWidget(int x, int y, Supplier<ItemStack> stack, Supplier<List<Component>> tooltip) {
		super(x, y, 16, 16);
		this.stack = stack;
		this.tooltip = tooltip;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (!hidden) {
			graphics.renderItem(stack.get(), context.left() + x, context.top() + y);
		}
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		if (!hidden && contains(context, context.mouseX(), context.mouseY())) {
			graphics.renderComponentTooltip(context.font(), tooltip.get(), context.mouseX(), context.mouseY());
		}
	}
}
