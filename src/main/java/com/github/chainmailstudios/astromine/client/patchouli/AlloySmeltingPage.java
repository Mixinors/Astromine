package com.github.chainmailstudios.astromine.client.patchouli;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

import com.mojang.blaze3d.systems.RenderSystem;

import com.github.chainmailstudios.astromine.common.recipe.AlloySmeltingRecipe;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class AlloySmeltingPage extends PageDoubleRecipeRegistry<AlloySmeltingRecipe> {
	public AlloySmeltingPage() {
		super(AlloySmeltingRecipe.Type.INSTANCE);
	}

	@Override
	public void drawRecipe(MatrixStack ms, AlloySmeltingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		mc.getTextureManager().bindTexture(book.craftingTexture);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX, recipeY, 11, 135, 96, 43, 128, 256);
		parent.drawCenteredStringNoShadow(ms, getTitle(second), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);

		parent.renderIngredient(ms, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.getFirstInput().asIngredient());
		parent.renderIngredient(ms, recipeX + 4, recipeY + 23, mouseX, mouseY, recipe.getSecondInput().asIngredient());
		parent.renderItemStack(ms, recipeX + 40, recipeY + 13, mouseX, mouseY, recipe.getRecipeKindIcon());
		parent.renderItemStack(ms, recipeX + 76, recipeY + 13, mouseX, mouseY, recipe.getOutput());
		parent.drawCenteredStringNoShadow(ms, new TranslatableText("category.astromine.consuming.energy", EnergyUtilities.simpleDisplay(recipe.getEnergyConsumed())), GuiBook.PAGE_WIDTH / 2, recipeY + 45, BasicEnergyConsumingPage.ENERGY_CONSUMED_TEXT_COLOR);
	}

	@Override
	protected ItemStack getRecipeOutput(AlloySmeltingRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		}

		return recipe.getOutput();
	}

	@Override
	protected int getRecipeHeight() {
		return 70;
	}
}
