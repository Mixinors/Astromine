package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class StonecuttingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Ingredient input;
	public final String suffix;

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input, int outputCount, String suffix) {
		super(output, outputCount);
		this.input = input;
		this.suffix = suffix;
	}

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input) {
		this(output, input, 1, "");
	}

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input, int outputCount) {
		this(output, input, outputCount, "");
	}

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input, String suffix) {
		this(output, input, 1, suffix);
	}

	@Override
	public String getGeneratorName() {
		return "stonecutting";
	}

	@Override
	public Identifier getRecipeId() {
		Identifier id = super.getRecipeId();
		String path = id.getPath() + "_from_stonecutting";
		if (!suffix.isEmpty()) path = path + "_" + suffix;
		return new Identifier(id.getNamespace(), path);
	}

	@Override
	public void generate(RecipeData data) {
		SingleItemRecipeJsonFactory
				.create(input, output, outputCount)
				.create("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(data, getRecipeId());
	}
}
