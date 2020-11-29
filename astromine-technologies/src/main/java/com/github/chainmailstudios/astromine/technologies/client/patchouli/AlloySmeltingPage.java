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

package com.github.chainmailstudios.astromine.technologies.client.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import com.github.chainmailstudios.astromine.client.patchouli.BasicEnergyConsumingPage;
import com.github.chainmailstudios.astromine.technologies.common.recipe.AlloySmeltingRecipe;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class AlloySmeltingPage extends PageDoubleRecipeRegistry<AlloySmeltingRecipe> {
	public AlloySmeltingPage() {
		super(AlloySmeltingRecipe.Type.INSTANCE);
	}

	@Override
	public void drawRecipe(PoseStack ms, AlloySmeltingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		mc.getTextureManager().bind(book.craftingTexture);
		RenderSystem.enableBlend();
		GuiComponent.blit(ms, recipeX, recipeY, 11, 135, 96, 43, 128, 256);
		parent.drawCenteredStringNoShadow(ms, getTitle(second).getVisualOrderText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);

		parent.renderIngredient(ms, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.getFirstInput().asIngredient());
		parent.renderIngredient(ms, recipeX + 4, recipeY + 23, mouseX, mouseY, recipe.getSecondInput().asIngredient());
		parent.renderItemStack(ms, recipeX + 40, recipeY + 13, mouseX, mouseY, recipe.getToastSymbol());
		parent.renderItemStack(ms, recipeX + 76, recipeY + 13, mouseX, mouseY, recipe.getResultItem());
		parent.drawCenteredStringNoShadow(ms, new TranslatableComponent("category.astromine.consuming.energy", "" + recipe.getEnergyInput()).getVisualOrderText(), GuiBook.PAGE_WIDTH / 2, recipeY + 45, BasicEnergyConsumingPage.ENERGY_CONSUMED_TEXT_COLOR);
	}

	@Override
	protected ItemStack getRecipeOutput(AlloySmeltingRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		}

		return recipe.getResultItem();
	}

	@Override
	protected int getRecipeHeight() {
		return 70;
	}
}
