package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

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
		SimpleCookingRecipeBuilder
			.smelting(
				Ingredient.of(items.toArray(new Item[0])),
				set.getItem(output),
				experience,
				time)
			.unlockedBy("impossible", new ImpossibleTrigger.TriggerInstance())
			.save(recipes, getRecipeId(set));
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_smelting";
	}
}
