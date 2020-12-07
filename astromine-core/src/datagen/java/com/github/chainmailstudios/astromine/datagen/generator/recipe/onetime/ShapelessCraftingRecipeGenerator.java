package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.Arrays;
import java.util.List;

public class ShapelessCraftingRecipeGenerator extends OneTimeRecipeGenerator {
	public final List<Ingredient> ingredients;

	public ShapelessCraftingRecipeGenerator(ItemConvertible output, int outputCount, Ingredient... inputs) {
		super(output, outputCount);
		this.ingredients = Arrays.asList(inputs.clone());
	}

	public ShapelessCraftingRecipeGenerator(ItemConvertible output, Ingredient... inputs) {
		this(output, 1, inputs);
	}

	@Override
	public void generate(RecipeData recipes) {
		ShapelessRecipeJsonFactory factory = ShapelessRecipeJsonFactory
				.create(output, outputCount)
				.criterion("impossible", new ImpossibleCriterion.Conditions());
		ingredients.forEach((factory::input));
		factory.offerTo(recipes, getRecipeId());
	}

	@Override
	public String getGeneratorName() {
		return "shapeless";
	}
}
