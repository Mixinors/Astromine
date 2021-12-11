/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.client.rei.infusing;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class InfusingCategory implements DisplayCategory<InfusingDisplay> {
	@Override
	public CategoryIdentifier<? extends InfusingDisplay> getCategoryIdentifier() {
		return AMRoughlyEnoughItemsPlugin.INFUSING;
	}

	@Override
	public Text getTitle() {
		return new TranslatableText("category.astromine.infusing");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(AMBlocks.ALTAR.get());
	}

	@Override
	public List<Widget> setupDisplay(InfusingDisplay display, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createCategoryBase(bounds));

		float radius = 50f;
		float degrees = 360f / display.getInputEntries().size();
		for (int i = 0; i < display.getInputEntries().size(); i++) {
			List<EntryStack<?>> stacks = display.getInputEntries().get(i);
			int x = (int) (radius * 1.05f * MathHelper.cos(degrees * i * 0.0174532925F));
			int y = (int) (radius * MathHelper.sin(degrees * i * 0.0174532925F));
			widgets.add(Widgets.createSlot(new Point(bounds.x + 65 + x, bounds.y + 54 + y + 15)).entry(EntryStacks.of(AMBlocks.ALTAR_PEDESTAL.get())).noFavoritesInteractable().noInteractable().disableHighlight().disableTooltips().disableBackground());
			widgets.add(Widgets.createSlot(new Point(bounds.x + 65 + x, bounds.y + 54 + y)).entries(stacks).disableBackground().markInput());
		}

		widgets.add(Widgets.createSlot(new Point(bounds.x + 65, bounds.y + 54 + 15)).entry(EntryStacks.of(AMBlocks.ALTAR.get())).noFavoritesInteractable().noInteractable().disableHighlight().disableTooltips().disableBackground());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 65, bounds.y + 54)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}

	public int getDisplayHeight() {
		return 140;
	}
}
