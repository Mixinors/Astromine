package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class StonecuttingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Ingredient input;
	public final String suffix;

	public StonecuttingRecipeGenerator(ItemLike output, Ingredient input, int outputCount, String suffix) {
		super(output, outputCount);
		this.input = input;
		this.suffix = suffix;
	}

	public StonecuttingRecipeGenerator(ItemLike output, Ingredient input) {
		this(output, input, 1, "");
	}

	public StonecuttingRecipeGenerator(ItemLike output, Ingredient input, int outputCount) {
		this(output, input, outputCount, "");
	}

	public StonecuttingRecipeGenerator(ItemLike output, Ingredient input, String suffix) {
		this(output, input, 1, suffix);
	}

	@Override
	public String getGeneratorName() {
		return "stonecutting";
	}

	@Override
	public ResourceLocation getRecipeId() {
		ResourceLocation id = super.getRecipeId();
		String path = id.getPath() + "_from_stonecutting";
		if (!suffix.isEmpty()) path = path + "_" + suffix;
		return new ResourceLocation(id.getNamespace(), path);
	}

	@Override
	public void generate(RecipeData data) {
		SingleItemRecipeBuilder
			.stonecutting(input, output, outputCount)
			.unlocks("impossible", new ImpossibleTrigger.TriggerInstance())
			.save(data, getRecipeId());
	}
}
