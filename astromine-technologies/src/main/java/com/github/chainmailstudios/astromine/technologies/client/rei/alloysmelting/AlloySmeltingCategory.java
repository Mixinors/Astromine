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

package com.github.chainmailstudios.astromine.technologies.client.rei.alloysmelting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.client.rei.AstromineTechnologiesRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

import com.google.common.collect.Lists;
import java.text.DecimalFormat;
import java.util.List;

@Environment(EnvType.CLIENT)
public class AlloySmeltingCategory implements RecipeCategory<AlloySmeltingDisplay> {
	@Override
	public ResourceLocation getIdentifier() {
		return AstromineTechnologiesRoughlyEnoughItemsPlugin.ALLOY_SMELTING;
	}

	@Override
	public String getCategoryName() {
		return I18n.get("category.astromine.alloy_smelting");
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER);
	}

	@Override
	public List<Widget> setupDisplay(AlloySmeltingDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
		DecimalFormat df = new DecimalFormat("###.##");
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.addAll(AstromineRoughlyEnoughItemsPlugin.createEnergyDisplay(new Rectangle(bounds.getX() + 10, bounds.getCenterY() - 23, 12, 48), display.getEnergyRequired(), false, display.getTimeRequired() * 500));
		widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5), new TranslatableComponent("category.astromine.cooking.time", df.format(display.getTimeRequired() / 20d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 18)).animationDurationTicks(display.getTimeRequired()));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 19)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 19 - 9)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 19 + 9)).entries(display.getInputEntries().get(1)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 19)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}
}
