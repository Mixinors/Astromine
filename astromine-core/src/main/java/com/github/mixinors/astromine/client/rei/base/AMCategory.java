package com.github.mixinors.astromine.client.rei.base;

import java.text.DecimalFormat;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.text.TranslatableText;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;

public interface AMCategory<T extends AMDisplay> extends DisplayCategory<T> {
	@Override
	default List<Widget> setupDisplay(T display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
		DecimalFormat df = new DecimalFormat("###.##");
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		addEnergyInputWidgets(widgets, display, startPoint, bounds);
		widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5), new TranslatableText("category.astromine.cooking.time", df.format(display.getTimeRequired() / 20d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 18)).animationDurationTicks(display.getTimeRequired()));
		addInputWidgets(widgets, display, startPoint, bounds);
		addOutputWidgets(widgets, display, startPoint, bounds);
		return widgets;
	}

	void addEnergyInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds);
	void addInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds);
	void addOutputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds);
}
