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

package com.github.mixinors.astromine.client.rei.electricsmelting;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ElectricSmeltingCategory extends DefaultCookingCategory {
	public ElectricSmeltingCategory() {
		super(AMRoughlyEnoughItemsPlugin.ELECTRIC_SMELTING, EntryStacks.of(AMBlocks.ADVANCED_ELECTRIC_FURNACE.get()), "category.astromine.electric_smelting");
	}
	
	@Override
	public List<Widget> setupDisplay(DefaultCookingDisplay display, Rectangle bounds) {
		var startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
		
		var cookingTime = display.getCookingTime();
		
		var format = new DecimalFormat("###.##");
		
		var widgets = new ArrayList<Widget>();
		
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 19)));
		
		if (display instanceof ElectricSmeltingDisplay electricSmeltingDisplay) {
			widgets.addAll(AMRoughlyEnoughItemsPlugin.createEnergyDisplay(new Rectangle(bounds.getX() + 10, bounds.getCenterY() - 23, 12, 48), electricSmeltingDisplay.getEnergyRequired(), false, (int) (cookingTime / 10 * 500)));
		}
		
		widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5), Text.translatable("category.astromine.cooking.time", format.format(cookingTime / 40.0F))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 18)).animationDurationTicks(cookingTime / 3));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 19)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 66;
	}
}
