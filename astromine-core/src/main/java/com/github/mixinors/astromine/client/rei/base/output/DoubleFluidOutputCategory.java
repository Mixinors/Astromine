package com.github.mixinors.astromine.client.rei.base.output;

import java.util.List;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.client.rei.base.input.EnergyInputCategory;
import com.github.mixinors.astromine.client.rei.base.input.EnergyInputDisplay;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;

public interface DoubleFluidOutputCategory<T extends EnergyInputDisplay> extends EnergyInputCategory<T> {
	@Override
	default void addOutputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds) {
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(startPoint.x + 61, bounds.getCenterY() - 23, 12, 48), display.getOutputEntries().get(0), true, 5000));
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(startPoint.x + 73, bounds.getCenterY() - 23, 12, 48), display.getOutputEntries().get(1), true, 5000));
	}
}
