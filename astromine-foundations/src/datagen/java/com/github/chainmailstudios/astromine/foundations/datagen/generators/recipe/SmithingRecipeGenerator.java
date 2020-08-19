package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.SmithingRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;

public class SmithingRecipeGenerator implements RecipeGenerator {
	public final MaterialItemType addition;
	public final MaterialItemType type;

	public SmithingRecipeGenerator(MaterialItemType type, MaterialItemType addition) {
		this.addition = addition;
		this.type = type;
	}

	@Override
	public void generateRecipe(RecipeData recipes, MaterialSet set) {
		SmithingRecipeJsonFactory
			.create(Ingredient.ofItems(set.getSmithingBaseSet().getItem(type)), set.getIngredient(addition), set.getItem(type))
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.offerTo(recipes, getRecipeName(set));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.usesSmithing() && set.hasType(type) && set.hasType(addition) && set.getSmithingBaseSet().hasType(type) && !set.getEntry(getOutput()).isFromVanilla();
	}

	@Override
	public MaterialItemType getOutput() {
		return type;
	}

	@Override
	public String getGeneratorName() {
		return "smithing";
	}
}
