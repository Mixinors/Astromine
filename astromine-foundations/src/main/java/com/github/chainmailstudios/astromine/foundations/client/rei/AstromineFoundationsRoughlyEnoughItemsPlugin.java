package com.github.chainmailstudios.astromine.foundations.client.rei;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.foundations.common.recipe.WireCuttingRecipe;
import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.BuiltinPlugin;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.plugin.crafting.DefaultCustomDisplay;
import net.minecraft.resources.ResourceLocation;
import java.util.Collections;

public class AstromineFoundationsRoughlyEnoughItemsPlugin extends AstromineRoughlyEnoughItemsPlugin {
	@Override
	public ResourceLocation getPluginIdentifier() {
		return AstromineCommon.identifier("discoveries_rei_plugin");
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(BuiltinPlugin.CRAFTING, WireCuttingRecipe.class, recipe -> {
			return new DefaultCustomDisplay(recipe,
				EntryStack.ofIngredients(ImmutableList.of(recipe.getInput(), recipe.getTool())),
				Collections.singletonList(EntryStack.create(recipe.getResultItem())));
		});
	}
}
