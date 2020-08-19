package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class ShapedCraftingRecipeGenerator extends CraftingRecipeGenerator {
	public final Map<Character, Ingredient> ingredients;
	public final String[] pattern;

	public ShapedCraftingRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount, String... pattern) {
		super(input, output, outputCount);
		this.ingredients = new HashMap<>();
		this.pattern = pattern;
	}

	public ShapedCraftingRecipeGenerator(MaterialItemType input, MaterialItemType output, String... pattern) {
		this(input, output, 1, pattern);
	}

	public void addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
	}

	@Override
	public void generateRecipe(RecipeData recipes, MaterialSet set) {
		ShapedRecipeJsonFactory factory = ShapedRecipeJsonFactory
			.create(set.getItem(output))
			.criterion("impossible", new ImpossibleCriterion.Conditions());
		for (String s : pattern) {
			factory.pattern(s);
		}
		factory.input('#', set.getIngredient(input));
		ingredients.forEach(factory::input);
		factory.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getGeneratorName() {
		return "shaped";
	}
}
