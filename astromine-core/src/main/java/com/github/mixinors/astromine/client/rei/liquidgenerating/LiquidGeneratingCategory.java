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

package com.github.mixinors.astromine.client.rei.liquidgenerating;

import com.github.mixinors.astromine.registry.AMBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.client.rei.AMREIPlugin;
import com.github.mixinors.astromine.client.rei.generating.AbstractEnergyGeneratingCategory;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class LiquidGeneratingCategory extends AbstractEnergyGeneratingCategory<LiquidGeneratingDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AMREIPlugin.LIQUID_GENERATING;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.astromine.fluid_generating");
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AMBlocks.ADVANCED_LIQUID_GENERATOR);
	}

	@Override
	public List<Widget> setupDisplay(LiquidGeneratingDisplay recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = super.setupDisplay(recipeDisplay, bounds);
		Rectangle innerBounds = new Rectangle(bounds.getCenterX() - 55, bounds.y, 110, bounds.height);
		widgets.addAll(AMREIPlugin.createFluidDisplay(new Rectangle(innerBounds.getX() + 24, innerBounds.getCenterY() - 23, 12, 48), Collections.singletonList(EntryStack.create(recipeDisplay.getFluid(), AMREIPlugin.convertToFraction(
			recipeDisplay.getAmount()))), false, 5000));
		widgets.add(Widgets.createArrow(new Point(innerBounds.getX() + 45, innerBounds.getY() + 26)));
		return widgets;
	}
}
