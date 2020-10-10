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
	public final Map<Character, MaterialItemType> types;
	public final String[] pattern;

	public ShapedCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount, String... pattern) {
		super(input, output, outputCount);
		this.ingredients = new HashMap<>();
		this.types = new HashMap<>();
		this.pattern = pattern;
	}

	public ShapedCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, String... pattern) {
		this(input, output, 1, pattern);
	}

	public void addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
	}

	public void addType(char c, MaterialItemType type) {
		this.types.put(c, type);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		if (pattern.length == 0) throw new IllegalStateException("recipe must have a pattern");
		else {
			ShapedRecipeJsonFactory factory = ShapedRecipeJsonFactory
					.create(set.getItem(output), outputCount)
					.criterion("impossible", new ImpossibleCriterion.Conditions());
			for (String s : pattern) {
				factory.pattern(s);
			}
			if (patternContains('#')) factory.input('#', set.getIngredient(input));
			if (patternContains('I')) factory.input('I', set.getItem(input));
			ingredients.forEach(factory::input);
			types.forEach((c, type) -> factory.input(c, set.getIngredient(type)));
			factory.offerTo(recipes, getRecipeId(set));
		}
	}

	public boolean patternContains(char c) {
		for (String s : pattern) {
			if (s.contains(String.valueOf(c))) return true;
		}
		return false;
	}

	@Override
	public String getGeneratorName() {
		return "shaped";
	}
}
