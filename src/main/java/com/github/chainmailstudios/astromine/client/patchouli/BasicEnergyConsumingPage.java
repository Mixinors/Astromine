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
		parent.drawCenteredStringNoShadow(ms, new TranslatableText("category.astromine.consuming.energy", EnergyUtilities.simpleDisplay(recipe.getEnergyConsumed())), GuiBook.PAGE_WIDTH / 2, recipeY + 25, ENERGY_CONSUMED_TEXT_COLOR);
	}

	@Override
	protected int getRecipeHeight() {
		return 50;
	}
}
