package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.CookingRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SmeltingRecipeGenerator extends CookingRecipeGenerator {
	public SmeltingRecipeGenerator(Ingredient input, ItemLike output, int time, float experience) {
		super(input, output, time, experience);
	}

	public SmeltingRecipeGenerator(Ingredient input, ItemLike output, float experience) {
		this(input, output, 200, experience);
	}

	public SmeltingRecipeGenerator(Ingredient input, ItemLike output) {
		this(input, output, 200, 0.1f);
	}

	@Override
	public ResourceLocation getRecipeId() {
		ResourceLocation id = super.getRecipeId();
		return new ResourceLocation(id.getNamespace(), id.getPath() + "_from_smelting");
	}

	@Override
	public void generate(RecipeData recipes) {
		SimpleCookingRecipeBuilder
			.smelting(
				input,
				output,
				experience,
				time)
			.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance())
			.save(recipes, getRecipeId());
	}
}
