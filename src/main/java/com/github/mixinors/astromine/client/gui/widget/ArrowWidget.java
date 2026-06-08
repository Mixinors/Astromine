/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiSprites;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class ArrowWidget extends Widget {
	private final Supplier<Double> progress;
	
	public ArrowWidget(int x, int y, Supplier<Double> progress) {
		super(x, y, GuiSprites.ARROW_WIDTH, GuiSprites.ARROW_HEIGHT);
		this.progress = progress;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (hidden) {
			return;
		}
		
		var screenX = context.left() + x;
		var screenY = context.top() + y;
		var filled = Mth.clamp((int) Math.round(progress.get() * width), 0, width);
		graphics.blit(GuiSprites.ARROW_BACKGROUND, screenX, screenY, 0.0F, 0.0F, width, height, width, height);
		
		if (filled > 0) {
			graphics.blit(GuiSprites.ARROW_FOREGROUND, screenX, screenY, 0.0F, 0.0F, filled, height, width, height);
		}
	}
}
