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

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input, int outputCount) {
		super(output, outputCount);
		this.input = input;
	}

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input) {
		this(output, input, 1);
	}

	@Override
	public String getGeneratorName() {
		return "stonecutting";
	}

	@Override
	public Identifier getRecipeId() {
		Identifier id = super.getRecipeId();
		return new Identifier(id.getNamespace(), id.getPath() + "_from_stonecutting");
	}

	@Override
	public void generate(RecipeData data) {
		SingleItemRecipeJsonFactory
				.create(input, output, outputCount)
				.create("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(data, getRecipeId());
	}
}
