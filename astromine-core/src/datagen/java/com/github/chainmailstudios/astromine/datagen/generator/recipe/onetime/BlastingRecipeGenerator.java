package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.CookingRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class BlastingRecipeGenerator extends CookingRecipeGenerator {
	public BlastingRecipeGenerator(Ingredient input, ItemLike output, int time, float experience) {
		super(input, output, time, experience);
	}

	public BlastingRecipeGenerator(Ingredient input, ItemLike output, float experience) {
		this(input, output, 100, experience);
	}

	public BlastingRecipeGenerator(Ingredient input, ItemLike output) {
		this(input, output, 100, 0.1f);
	}

	@Override
	public ResourceLocation getRecipeId() {
		ResourceLocation id = super.getRecipeId();
		return new ResourceLocation(id.getNamespace(), id.getPath() + "_from_blasting");
	}

	@Override
	public void generate(RecipeData recipes) {
		SimpleCookingRecipeBuilder
			.blasting(
				input,
				output,
				experience,
				time)
			.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance())
			.save(recipes, getRecipeId());
	}
}
