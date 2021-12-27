package com.github.mixinors.astromine.client.rei.base.output;

import java.util.List;

import com.github.mixinors.astromine.client.rei.base.input.EnergyInputCategory;
import com.github.mixinors.astromine.client.rei.base.input.EnergyInputDisplay;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;

public interface SingleItemOutputCategory<T extends EnergyInputDisplay> extends EnergyInputCategory<T> {
	@Override
	default void addOutputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds) {
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 19)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
	}
}
