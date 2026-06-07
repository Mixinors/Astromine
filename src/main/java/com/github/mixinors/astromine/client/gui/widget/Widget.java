/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import net.minecraft.client.gui.GuiGraphics;

public abstract class Widget {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected boolean hidden;
	
	protected Widget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render(GuiGraphics graphics, WidgetContext context) {
	}
	
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
	}
	
	public boolean mouseClicked(WidgetContext context, double mouseX, double mouseY, int button) {
		return false;
	}
	
	public boolean contains(WidgetContext context, double mouseX, double mouseY) {
		return !hidden && mouseX >= context.left() + x && mouseX < context.left() + x + width && mouseY >= context.top() + y && mouseY < context.top() + y + height;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
