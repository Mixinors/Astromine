/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiSprites;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TextWidget extends Widget {
	private final Supplier<Component> text;
	private final int color;
	
	public TextWidget(int x, int y, Supplier<Component> text) {
		this(x, y, text, GuiSprites.TEXT);
	}
	
	public TextWidget(int x, int y, Supplier<Component> text, int color) {
		super(x, y, 0, 9);
		this.text = text;
		this.color = color;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (!hidden) {
			graphics.drawString(context.font(), text.get(), context.left() + x, context.top() + y, color, false);
		}
	}
}
