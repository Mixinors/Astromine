package com.github.mixinors.astromine.client.rei.base.input;

import java.util.List;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.client.rei.base.AMCategory;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;

public interface EnergyInputCategory<T extends EnergyInputDisplay> extends AMCategory<T> {
	@Override
	default void addEnergyInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds) {
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createEnergyDisplay(new Rectangle(bounds.getX() + 10, bounds.getCenterY() - 23, 12, 48), display.getEnergyRequired(), false, display.getTimeRequired() * 500));
	}
}
