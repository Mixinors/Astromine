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

package com.github.chainmailstudios.astromine.technologies.client.rei.electrolyzing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

import com.google.common.collect.Lists;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ElectrolyzingCategory implements RecipeCategory<ElectrolyzingDisplay> {
	private final Identifier id;
	private final String translationKey;
	private final EntryStack logo;

	public ElectrolyzingCategory(Identifier id, String translationKey, EntryStack logo) {
		this.id = id;
		this.translationKey = translationKey;
		this.logo = logo;
	}

	@Override
	public Identifier getIdentifier() {
		return id;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate(translationKey);
	}

	@Override
	public EntryStack getLogo() {
		return logo;
	}

	@Override
	public List<Widget> setupDisplay(ElectrolyzingDisplay recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		Rectangle innerBounds = new Rectangle(bounds.getCenterX() - 55, bounds.y, 110, bounds.height);
		widgets.add(Widgets.createRecipeBase(innerBounds));
		widgets.addAll(AstromineRoughlyEnoughItemsPlugin.createEnergyDisplay(new Rectangle(innerBounds.x + 10, bounds.getCenterY() - 23, 12, 48), recipeDisplay.getEnergy(), false, 12500));
		widgets.addAll(AstromineRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(innerBounds.x + 24, bounds.getCenterY() - 23, 12, 48), recipeDisplay.getInputEntries().get(0), false, 5000));
		widgets.add(Widgets.createArrow(new Point(innerBounds.getX() + 45, innerBounds.getY() + 26)));
		widgets.addAll(AstromineRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(innerBounds.getMaxX() - 32, bounds.getCenterY() - 23, 12, 48), recipeDisplay.getResultingEntries().get(0), true, 5000));
		widgets.addAll(AstromineRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(innerBounds.getMaxX() - 32 + 14, bounds.getCenterY() - 23, 12, 48), recipeDisplay.getResultingEntries().get(1), true, 5000));
		return widgets;
	}
}
