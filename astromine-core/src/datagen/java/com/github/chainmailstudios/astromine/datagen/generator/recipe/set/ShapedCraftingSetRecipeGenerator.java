package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.HashMap;
import java.util.Map;

public class ShapedCraftingSetRecipeGenerator extends CraftingSetRecipeGenerator {
	public final Map<Character, Ingredient> ingredients;
	public final String[] pattern;

	public ShapedCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount, String... pattern) {
		super(input, output, outputCount);
		this.ingredients = new HashMap<>();
		this.pattern = pattern;
	}

	public ShapedCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, String... pattern) {
		this(input, output, 1, pattern);
	}

	public void addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
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
