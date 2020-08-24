package com.github.chainmailstudios.astromine.foundations.client.rei;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.foundations.client.rei.infusing.InfusingCategory;
import com.github.chainmailstudios.astromine.foundations.client.rei.infusing.InfusingDisplay;
import com.github.chainmailstudios.astromine.foundations.common.recipe.AltarRecipe;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.util.Identifier;

public class AstromineFoundationsRoughlyEnoughItemsPlugin extends AstromineRoughlyEnoughItemsPlugin {
	public static final Identifier INFUSING = AstromineCommon.identifier("infusing");

	@Override
	public Identifier getPluginIdentifier() {
		return AstromineCommon.identifier("foundations_rei_plugin");
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategories(new InfusingCategory());
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(INFUSING, AltarRecipe.class, InfusingDisplay::new);
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(INFUSING, EntryStack.create(AstromineFoundationsBlocks.ALTAR));
	}
}
