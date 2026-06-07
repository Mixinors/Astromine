/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiSprites;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyBarWidget extends Widget {
	private final Supplier<IEnergyStorage> storage;
	
	public EnergyBarWidget(int x, int y, Supplier<IEnergyStorage> storage) {
		super(x, y, GuiSprites.BAR_WIDTH, GuiSprites.BAR_HEIGHT);
		this.storage = storage;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (hidden) {
			return;
		}
		
		var current = current();
		var maximum = Math.max(1L, maximum());
		var filled = Mth.clamp((int) Math.round((double) current / maximum * height), 0, height);
		var screenX = context.left() + x;
		var screenY = context.top() + y;
		graphics.blit(GuiSprites.ENERGY_BAR_BACKGROUND, screenX, screenY, 0.0F, 0.0F, width, height, width, height);
		
		if (filled > 0) {
			var innerHeight = height - 2;
			var clipped = Math.min(filled, innerHeight);
			var yOffset = innerHeight - clipped;
			graphics.blit(GuiSprites.ENERGY_BAR_FILLED, screenX + 1, screenY + 1 + yOffset, 1.0F, 1.0F + yOffset, width - 2, clipped, width, height);
		}
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		if (!hidden && contains(context, context.mouseX(), context.mouseY())) {
			graphics.renderComponentTooltip(context.font(), List.of(Component.translatable("text.astromine.tooltip.compound_energy_value", current(), maximum())), context.mouseX(), context.mouseY());
		}
	}
	
	private long current() {
		return LongEnergyStorage.getAmount(storage.get());
	}
	
	private long maximum() {
		return LongEnergyStorage.getCapacity(storage.get());
	}
}
