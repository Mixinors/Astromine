package com.github.chainmailstudios.astromine.client.rei;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.rei.sorting.SortingCategory;
import com.github.chainmailstudios.astromine.client.rei.sorting.SortingDisplay;
import com.github.chainmailstudios.astromine.recipe.SortingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AstromineREIPlugin implements REIPluginV0 {
	public static final Identifier SORTING = AstromineCommon.identifier("sorting");
	public static final Identifier LIQUID_GENERATOR = AstromineCommon.identifier("liquid_generator");
	
	@Override
	public Identifier getPluginIdentifier() {
		return AstromineCommon.identifier("rei_plugin");
	}
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new SortingCategory());
	}
	
	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(SORTING, SortingRecipe.class, SortingDisplay::new);
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(SORTING, EntryStack.create(AstromineBlocks.SORTER));
	}
}
