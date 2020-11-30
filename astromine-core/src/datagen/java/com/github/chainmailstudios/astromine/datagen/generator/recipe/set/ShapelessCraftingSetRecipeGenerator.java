package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

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

	public ShapelessCraftingSetRecipeGenerator addIngredients(Ingredient... ingredients) {
		this.ingredients.addAll(Arrays.asList(ingredients));
		return this;
	}

	public ShapelessCraftingSetRecipeGenerator addTypes(MaterialItemType... types) {
		this.types.addAll(Arrays.asList(types));
		return this;
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_" + input.getName();
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		ShapelessRecipeBuilder factory = ShapelessRecipeBuilder
				.shapeless(set.getItem(output), outputCount)
				.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance())
				.requires(set.getIngredient(input));
		ingredients.forEach(factory::requires);
		types.forEach(type -> factory.requires(set.getIngredient(type)));
		factory.save(recipes, getRecipeId(set));
	}

	@Override
	public String getGeneratorName() {
		return "shapeless";
	}
}
