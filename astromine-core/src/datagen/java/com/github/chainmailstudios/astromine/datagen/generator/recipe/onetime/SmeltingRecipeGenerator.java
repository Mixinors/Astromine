package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.CookingRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class SmeltingRecipeGenerator extends CookingRecipeGenerator {
	public SmeltingRecipeGenerator(Ingredient input, Identifier output, int time, float experience) {
		super(input, output, time, experience);
	}

	public SmeltingRecipeGenerator(Ingredient input, Identifier output, float experience) {
		this(input, output, 200, experience);
	}

	public SmeltingRecipeGenerator(Ingredient input, Identifier output) {
		this(input, output, 200, 0.1f);
	}

	@Override
	public void generate(RecipeData recipes) {
		CookingRecipeJsonFactory
			.createSmelting(
				input,
				Registry.ITEM.get(output),
				experience,
				time)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.offerTo(recipes, getRecipeId());
	}
}
