package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;

public class ShapelessCraftingRecipeGenerator extends OneTimeRecipeGenerator {
	public final List<Ingredient> ingredients;

	public ShapelessCraftingRecipeGenerator(ItemLike output, int outputCount, Ingredient... inputs) {
		super(output, outputCount);
		this.ingredients = Arrays.asList(inputs.clone());
	}

	public ShapelessCraftingRecipeGenerator(ItemLike output, Ingredient... inputs) {
		this(output, 1, inputs);
	}

	@Override
	public void generate(RecipeData recipes) {
		ShapelessRecipeBuilder factory = ShapelessRecipeBuilder
			.shapeless(output, outputCount)
			.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance());
		ingredients.forEach((factory::requires));
		factory.save(recipes, getRecipeId());
	}

	@Override
	public String getGeneratorName() {
		return "shapeless";
	}
}
