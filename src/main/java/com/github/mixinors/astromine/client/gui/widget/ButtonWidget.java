/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiRenderers;
import com.github.mixinors.astromine.client.gui.GuiSprites;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ButtonWidget extends Widget {
	private final Supplier<Component> label;
	private final BooleanSupplier enabled;
	private final Runnable action;
	
	public ButtonWidget(int x, int y, int width, int height, Supplier<Component> label, BooleanSupplier enabled, Runnable action) {
		super(x, y, width, height);
		this.label = label;
		this.enabled = enabled;
		this.action = action;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (hidden) {
			return;
		}
		
		var screenX = context.left() + x;
		var screenY = context.top() + y;
		var hovered = contains(context, context.mouseX(), context.mouseY());
		GuiRenderers.button(graphics, screenX, screenY, width, height, hovered, enabled.getAsBoolean());
		
		var text = label.get();
		var textX = screenX + (width - context.font().width(text)) / 2;
		var textY = screenY + (height - 8) / 2;
		graphics.drawString(context.font(), text, textX, textY, GuiSprites.BUTTON_TEXT, true);
	}
	
	@Override
	public boolean mouseClicked(WidgetContext context, double mouseX, double mouseY, int button) {
		if (button == 0 && enabled.getAsBoolean() && contains(context, mouseX, mouseY)) {
			action.run();
			return true;
		}
		
		return false;
	}
}
