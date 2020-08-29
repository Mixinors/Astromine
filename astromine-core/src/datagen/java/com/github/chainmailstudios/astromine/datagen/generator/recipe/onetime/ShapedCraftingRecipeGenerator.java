package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.HashMap;
import java.util.Map;

public class ShapedCraftingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Map<Character, Ingredient> ingredients;
	public final String[] pattern;

	public ShapedCraftingRecipeGenerator(Identifier output, int outputCount, String... pattern) {
		super(output, outputCount);
		this.ingredients = new HashMap<>();
		this.pattern = pattern;
	}

	public ShapedCraftingRecipeGenerator(Identifier output, String... pattern) {
		this(output, 1, pattern);
	}

	public ShapedCraftingRecipeGenerator addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
		return this;
	}

	@Override
	public void generate(RecipeData recipes) {
		if (pattern.length == 0) throw new IllegalStateException("recipe must have at least one ingredient");
		else {
			ShapedRecipeJsonFactory factory = ShapedRecipeJsonFactory
					.create(Registry.ITEM.get(output))
					.criterion("impossible", new ImpossibleCriterion.Conditions());
			for (String s : pattern) {
				factory.pattern(s);
			}
			ingredients.forEach(factory::input);
			factory.offerTo(recipes, getRecipeId());
		}
	}

	@Override
	public String getGeneratorName() {
		return "shaped";
	}
}
