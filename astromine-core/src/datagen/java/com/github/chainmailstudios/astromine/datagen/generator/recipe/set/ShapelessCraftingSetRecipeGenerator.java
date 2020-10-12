package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapelessCraftingSetRecipeGenerator extends CraftingSetRecipeGenerator {
	public final List<Ingredient> ingredients;
	public final List<MaterialItemType> types;

	public ShapelessCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount) {
		super(input, output, outputCount);
		this.ingredients = new ArrayList<>();
		this.types = new ArrayList<>();
	}

	public ShapelessCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 1);
	}

	public void addIngredients(Ingredient... ingredients) {
		this.ingredients.addAll(Arrays.asList(ingredients));
	}

	public void addTypes(MaterialItemType... types) {
		this.types.addAll(Arrays.asList(types));
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_" + input.getName();
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		ShapelessRecipeJsonFactory factory = ShapelessRecipeJsonFactory
				.create(set.getItem(output), outputCount)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.input(set.getIngredient(input));
		ingredients.forEach(factory::input);
		types.forEach(type -> factory.input(set.getIngredient(type)));
		factory.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getGeneratorName() {
		return "shapeless";
	}
}
