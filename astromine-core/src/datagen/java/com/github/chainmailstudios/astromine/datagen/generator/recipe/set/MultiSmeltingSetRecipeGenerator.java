package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.Collection;

public class MultiSmeltingSetRecipeGenerator extends MultiCookingSetRecipeGenerator {

	public MultiSmeltingSetRecipeGenerator(Collection<MaterialItemType> inputs, MaterialItemType output, int time, float experience) {
		super(inputs, output, time, experience);
	}

	public MultiSmeltingSetRecipeGenerator(Collection<MaterialItemType> inputs, MaterialItemType output, float experience) {
		this(inputs, output, 200, experience);
	}

	public MultiSmeltingSetRecipeGenerator(Collection<MaterialItemType> inputs, MaterialItemType output) {
		this(inputs, output, 200, 0.1f);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		Collection<Item> items = getInputs(set);
		CookingRecipeJsonFactory
				.createSmelting(
						Ingredient.ofItems(items.toArray(new Item[0])),
						set.getItem(output),
						experience,
						time)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_smelting";
	}
}
