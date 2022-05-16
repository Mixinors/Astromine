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

package com.github.mixinors.astromine.client.rei.base.output;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.client.rei.base.input.EnergyInputCategory;
import com.github.mixinors.astromine.client.rei.base.input.EnergyInputDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;

import java.util.List;

public interface DoubleFluidOutputCategory<T extends EnergyInputDisplay> extends EnergyInputCategory<T> {
	@Override
	default void addOutputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds) {
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(startPoint.x + 61, bounds.getCenterY() - 23, 12, 48), display.getOutputEntries().get(0), true, 5000));
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(startPoint.x + 73, bounds.getCenterY() - 23, 12, 48), display.getOutputEntries().get(1), true, 5000));
	}
}