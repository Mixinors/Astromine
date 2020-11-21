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

package com.github.chainmailstudios.astromine.technologies.client.rei.solidifying;

import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.client.rei.AstromineTechnologiesRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.client.rei.generating.AbstractEnergyGeneratingCategory;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SolidifyingCategory implements RecipeCategory<SolidifyingDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AstromineTechnologiesRoughlyEnoughItemsPlugin.SOLIDIFYING;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.astromine.fluid_generating");
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_SOLIDIFIER);
	}

	@Override
	public List<Widget> setupDisplay(SolidifyingDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		Rectangle innerBounds = new Rectangle(bounds.getCenterX() - 55, bounds.y, 110, bounds.height);
		widgets.addAll(AstromineRoughlyEnoughItemsPlugin.createFluidDisplay(new Rectangle(innerBounds.getX() + 24, innerBounds.getCenterY() - 23, 12, 48), Collections.singletonList(EntryStack.create(display.getFluid(), AstromineRoughlyEnoughItemsPlugin.convertA2R(
			display.getAmount()))), false, 5000));
		widgets.add(Widgets.createArrow(new Point(innerBounds.getX() + 45, innerBounds.getY() + 26)));
		widgets.add(Widgets.createSlot(new Point(innerBounds.getX() + 61, innerBounds.getY() + 19)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}
}
