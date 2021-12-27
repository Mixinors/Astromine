package com.github.mixinors.astromine.client.rei.base.input;

import java.util.List;

import com.github.mixinors.astromine.client.rei.base.AMCategory;
import com.github.mixinors.astromine.client.rei.base.AMDisplay;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;

public interface SingleItemInputCategory<T extends AMDisplay> extends AMCategory<T> {
	@Override
	default void addInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds) {
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 19)).entries(display.getInputEntries().get(0)).markInput());
	}
}
