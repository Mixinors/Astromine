package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

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

	public ShapedCraftingSetRecipeGenerator addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
		return this;
	}

	public ShapedCraftingSetRecipeGenerator addType(char c, MaterialItemType type) {
		this.types.put(c, type);
		return this;
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		if (pattern.length == 0) throw new IllegalStateException("recipe must have a pattern");
		else {
			ShapedRecipeBuilder factory = ShapedRecipeBuilder
				.shaped(set.getItem(output), outputCount)
				.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance());
			for (String s : pattern) {
				factory.pattern(s);
			}
			if (patternContains('#')) factory.define('#', set.getIngredient(input));
			if (patternContains('I')) factory.define('I', set.getItem(input));
			ingredients.forEach(factory::define);
			types.forEach((c, type) -> factory.define(c, set.getIngredient(type)));
			factory.save(recipes, getRecipeId(set));
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
