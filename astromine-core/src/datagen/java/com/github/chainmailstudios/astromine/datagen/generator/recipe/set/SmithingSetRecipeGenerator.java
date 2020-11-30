package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.SetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

public class SmithingSetRecipeGenerator implements SetRecipeGenerator {
	public final MaterialItemType addition;
	public final MaterialItemType type;

	public SmithingSetRecipeGenerator(MaterialItemType type, MaterialItemType addition) {
		this.addition = addition;
		this.type = type;
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		UpgradeRecipeBuilder
			.smithing(Ingredient.of(set.getSmithingBaseSet().getItem(type)), set.getIngredient(addition), set.getItem(type))
			.unlocks("impossible", new ImpossibleTrigger.TriggerInstance())
			.save(recipes, getRecipeId(set));
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
