/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiRenderers;
import com.github.mixinors.astromine.client.gui.GuiSprites;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidBarWidget extends Widget {
	private final Supplier<FluidStack> stack;
	private final IntSupplier capacity;
	
	public FluidBarWidget(int x, int y, Supplier<FluidStack> stack, IntSupplier capacity) {
		super(x, y, GuiSprites.BAR_WIDTH, GuiSprites.BAR_HEIGHT);
		this.stack = stack;
		this.capacity = capacity;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (hidden) {
			return;
		}
		
		var fluid = stack.get();
		var screenX = context.left() + x;
		var screenY = context.top() + y;
		GuiRenderers.nineSlice(graphics, GuiSprites.BAR_BACKGROUND, screenX, screenY, width, height, 18, 18, 1);
		
		if (!fluid.isEmpty()) {
			var filled = Mth.clamp((int) (height / (double) Math.max(1, capacity.getAsInt()) * fluid.getAmount()), 0, height);
			
			if (filled > 0) {
				graphics.enableScissor(screenX, screenY + height - filled, screenX + width, screenY + height);
				GuiRenderers.tiledFluid(graphics, fluid, screenX + 1, screenY + 1, width - 2, height - 2);
				graphics.disableScissor();
			}
		}
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		if (!hidden && contains(context, context.mouseX(), context.mouseY())) {
			var fluid = stack.get();
			var name = fluid.isEmpty() ? Component.translatable("text.astromine.empty") : fluid.getHoverName();
			graphics.renderComponentTooltip(context.font(), List.of(name, Component.translatable("text.astromine.tooltip.fractional_value", fluid.getAmount(), capacity.getAsInt())), context.mouseX(), context.mouseY());
		}
	}
}
