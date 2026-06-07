/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiRenderers;
import com.github.mixinors.astromine.client.gui.GuiSprites;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidFilterWidget extends Widget {
	private final Supplier<FluidStack> stack;
	private final Supplier<List<Component>> tooltip;
	
	public FluidFilterWidget(int x, int y, Supplier<FluidStack> stack, Supplier<List<Component>> tooltip) {
		super(x, y, 8, 8);
		this.stack = stack;
		this.tooltip = tooltip;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		var screenX = context.left() + x;
		var screenY = context.top() + y;
		graphics.blit(GuiSprites.FLUID_FILTER_BACKGROUND, screenX, screenY, 0.0F, 0.0F, width, height, width, height);
		
		var fluid = stack.get();
		
		if (!fluid.isEmpty()) {
			GuiRenderers.tiledFluid(graphics, fluid, screenX + 2, screenY + 2, 4, 4);
		}
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		if (!hidden && contains(context, context.mouseX(), context.mouseY())) {
			graphics.renderComponentTooltip(context.font(), tooltip.get(), context.mouseX(), context.mouseY());
		}
	}
}
