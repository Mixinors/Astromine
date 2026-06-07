/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiRenderers;
import net.minecraft.client.gui.GuiGraphics;

public class PanelWidget extends WidgetGroup {
	public PanelWidget(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	protected void renderSelf(GuiGraphics graphics, WidgetContext context) {
		GuiRenderers.panel(graphics, context.left() + x, context.top() + y, width, height);
	}
}
