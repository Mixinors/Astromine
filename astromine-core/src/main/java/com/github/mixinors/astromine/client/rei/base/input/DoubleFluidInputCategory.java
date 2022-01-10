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

package com.github.mixinors.astromine.client.rei.base.input;

import java.util.List;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.client.rei.base.AMCategory;
import com.github.mixinors.astromine.client.rei.base.AMDisplay;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;

public interface DoubleFluidInputCategory<T extends AMDisplay> extends AMCategory<T> {
	@Override
	default void addInputWidgets(List<Widget> widgets, T display, Point startPoint, Rectangle bounds) {
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(startPoint.x + 4, bounds.getCenterY() - 23, 12, 48), display.getInputEntries().get(0), false, 5000));
		widgets.addAll(AMRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(startPoint.x + 16, bounds.getCenterY() - 23, 12, 48), display.getInputEntries().get(1), false, 5000));
	}
}
