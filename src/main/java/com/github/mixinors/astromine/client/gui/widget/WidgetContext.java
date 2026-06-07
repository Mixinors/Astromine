/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import net.minecraft.client.gui.Font;

public record WidgetContext(int left, int top, int mouseX, int mouseY, Font font) {
	public WidgetContext offset(int x, int y) {
		return new WidgetContext(left + x, top + y, mouseX, mouseY, font);
	}
}
