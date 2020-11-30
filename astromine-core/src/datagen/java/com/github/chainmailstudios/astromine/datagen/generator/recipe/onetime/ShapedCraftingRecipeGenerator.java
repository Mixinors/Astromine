package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;

public class ShapedCraftingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Map<Character, Ingredient> ingredients;
	public final String[] pattern;

	public ShapedCraftingRecipeGenerator(ItemLike output, int outputCount, String... pattern) {
		super(output, outputCount);
		this.ingredients = new HashMap<>();
		this.pattern = pattern;
	}

	public ShapedCraftingRecipeGenerator(ItemLike output, String... pattern) {
		this(output, 1, pattern);
	}

	public ShapedCraftingRecipeGenerator addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
		return this;
	}

	@Override
	public void generate(RecipeData recipes) {
		if (ingredients.size() == 0) throw new IllegalStateException("recipe must have at least one ingredient");
		else if (pattern.length == 0) throw new IllegalStateException("recipe must have a pattern");
		else {
			ShapedRecipeBuilder factory = ShapedRecipeBuilder
				.shaped(output, outputCount)
				.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance());
			for (String s : pattern) {
				factory.pattern(s);
			}
			ingredients.forEach(factory::define);
			factory.save(recipes, getRecipeId());
		}
	}

	@Override
	public String getGeneratorName() {
		return "shaped";
	}
}
