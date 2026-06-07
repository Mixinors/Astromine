/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiRenderers;
import java.util.function.Predicate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class SlotBackgroundsWidget extends Widget {
	private final AbstractContainerMenu menu;
	private final Predicate<Slot> visible;
	
	public SlotBackgroundsWidget(AbstractContainerMenu menu, Predicate<Slot> visible) {
		super(0, 0, 0, 0);
		this.menu = menu;
		this.visible = visible;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		for (var slot : menu.slots) {
			if (visible.test(slot)) {
				GuiRenderers.slot(graphics, context.left() + slot.x - 1, context.top() + slot.y - 1);
			}
		}
	}
}
