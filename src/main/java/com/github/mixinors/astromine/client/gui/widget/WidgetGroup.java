/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;

public class WidgetGroup extends Widget {
	protected final List<Widget> children = new ArrayList<>();
	
	public WidgetGroup(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public <T extends Widget> T add(T widget) {
		children.add(widget);
		return widget;
	}
	
	public List<Widget> children() {
		return children;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (hidden) {
			return;
		}
		
		var childContext = context.offset(x, y);
		renderSelf(graphics, context);
		
		for (var child : children) {
			child.render(graphics, childContext);
		}
	}
	
	protected void renderSelf(GuiGraphics graphics, WidgetContext context) {
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		if (hidden) {
			return;
		}
		
		var childContext = context.offset(x, y);
		
		for (var i = children.size() - 1; i >= 0; --i) {
			children.get(i).renderTooltip(graphics, childContext);
		}
	}
	
	@Override
	public boolean mouseClicked(WidgetContext context, double mouseX, double mouseY, int button) {
		if (hidden) {
			return false;
		}
		
		var childContext = context.offset(x, y);
		
		for (var i = children.size() - 1; i >= 0; --i) {
			if (children.get(i).mouseClicked(childContext, mouseX, mouseY, button)) {
				return true;
			}
		}
		
		return false;
	}
}
