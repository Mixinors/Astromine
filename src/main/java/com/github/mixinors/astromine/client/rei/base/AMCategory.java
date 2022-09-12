/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.client.rei.base;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public interface AMCategory<T extends AMDisplay> extends DisplayCategory<T> {
	@Override
	default List<Widget> setupDisplay(T display, Rectangle bounds) {
		var startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
		
		var format = new DecimalFormat("###.##");
		
		var widgets = new ArrayList<Widget>();
		
		widgets.add(Widgets.createRecipeBase(bounds));
		
		addEnergyInputWidgets(widgets, display, startPoint, bounds);
		
		widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5), Text.translatable("category.astromine.cooking.time", format.format(display.getTimeRequired() / 20.0D))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 18)).animationDurationTicks(display.getTimeRequired()));
		
		addInputWidgets(widgets, display, startPoint, bounds);
		addOutputWidgets(widgets, display, startPoint, bounds);
		
		return widgets;
	}
	
	void addEnergyInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds);
	
	void addInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds);
	
	void addOutputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds);
}
