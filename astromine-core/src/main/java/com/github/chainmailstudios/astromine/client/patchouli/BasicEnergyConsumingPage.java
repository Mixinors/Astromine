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

package com.github.chainmailstudios.astromine.client.patchouli;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;

import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageSimpleProcessingRecipe;

public abstract class BasicEnergyConsumingPage<T extends EnergyConsumingRecipe<?>> extends PageSimpleProcessingRecipe<T> {
	public static final int ENERGY_CONSUMED_TEXT_COLOR = 0x999999;

	public BasicEnergyConsumingPage(RecipeType<T> recipeType) {
		super(recipeType);
	}

	@Override
	public void drawRecipe(MatrixStack ms, T recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		super.drawRecipe(ms, recipe, recipeX, recipeY, mouseX, mouseY, second);
		parent.drawCenteredStringNoShadow(ms, new TranslatableText("category.astromine.consuming.energy", EnergyUtilities.simpleDisplay(recipe.getEnergy())).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY + 25, ENERGY_CONSUMED_TEXT_COLOR);
	}

	@Override
	protected int getRecipeHeight() {
		return 50;
	}
}
