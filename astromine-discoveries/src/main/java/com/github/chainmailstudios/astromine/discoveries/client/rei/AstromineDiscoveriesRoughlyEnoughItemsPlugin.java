package com.github.chainmailstudios.astromine.discoveries.client.rei;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.discoveries.client.rei.infusing.InfusingCategory;
import com.github.chainmailstudios.astromine.discoveries.client.rei.infusing.InfusingDisplay;
import com.github.chainmailstudios.astromine.discoveries.common.recipe.AltarRecipe;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.util.Identifier;

public class AstromineDiscoveriesRoughlyEnoughItemsPlugin extends AstromineRoughlyEnoughItemsPlugin {
	public static final Identifier INFUSING = AstromineCommon.identifier("infusing");

	@Override
	public Identifier getPluginIdentifier() {
		return AstromineCommon.identifier("discoveries_rei_plugin");
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
		recipeHelper.registerWorkingStations(INFUSING, EntryStack.create(AstromineDiscoveriesBlocks.ALTAR));
	}
}
