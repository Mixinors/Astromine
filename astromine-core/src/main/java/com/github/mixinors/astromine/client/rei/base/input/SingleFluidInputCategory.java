package com.github.mixinors.astromine.client.rei.base.input;

import java.util.List;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.client.rei.base.AMCategory;
import com.github.mixinors.astromine.client.rei.base.AMDisplay;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;

public interface SingleFluidInputCategory<T extends AMDisplay> extends AMCategory<T> {
	@Override
	default void addInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds) {
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(startPoint.x + 4, bounds.getCenterY() - 23, 12, 48), display.getInputEntries().get(0), false, 5000));
	}
}
