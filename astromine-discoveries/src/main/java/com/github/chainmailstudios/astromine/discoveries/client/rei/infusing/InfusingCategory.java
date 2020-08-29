/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.discoveries.client.rei.infusing;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import com.github.chainmailstudios.astromine.discoveries.client.rei.AstromineDiscoveriesRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

import com.google.common.collect.Lists;
import java.util.List;

public class InfusingCategory implements RecipeCategory<InfusingDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AstromineDiscoveriesRoughlyEnoughItemsPlugin.INFUSING;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.astromine.infusing");
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AstromineDiscoveriesBlocks.ALTAR);
	}

	@Override
	public List<Widget> setupDisplay(InfusingDisplay display, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createCategoryBase(bounds));

		float radius = 50f;
		float degrees = 360f / display.getInputEntries().size();
		System.out.println(degrees);
		for (int i = 0; i < display.getInputEntries().size(); i++) {
			List<EntryStack> stacks = display.getInputEntries().get(i);
			int x = (int) (radius * 1.05f * MathHelper.cos(degrees * i * 0.0174532925F));
			int y = (int) (radius * MathHelper.sin(degrees * i * 0.0174532925F));
			widgets.add(Widgets.createSlot(new Point(bounds.x + 65 + x, bounds.y + 54 + y + 15)).entry(EntryStack.create(AstromineDiscoveriesBlocks.ALTAR_PEDESTAL)).noFavoritesInteractable().noInteractable().disableHighlight().disableTooltips().disableBackground());
			widgets.add(Widgets.createSlot(new Point(bounds.x + 65 + x, bounds.y + 54 + y)).entries(stacks).disableBackground().markInput());
		}

		widgets.add(Widgets.createSlot(new Point(bounds.x + 65, bounds.y + 54 + 15)).entry(EntryStack.create(AstromineDiscoveriesBlocks.ALTAR)).noFavoritesInteractable().noInteractable().disableHighlight().disableTooltips().disableBackground());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 65, bounds.y + 54)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}

	public int getDisplayHeight() {
		return 140;
	}
}
