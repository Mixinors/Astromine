package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialEntry;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.generators.Generator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.Random;

public interface RecipeGenerator extends Generator {
	void generateRecipe(RecipeData recipes, MaterialSet set);
	MaterialItemType getOutput();
	default MaterialEntry getOutput(MaterialSet set) {
		return set.getEntry(getOutput());
	}
	default String getRecipeName(MaterialSet set) {
		return getOutput(set).getName();
	}
	default Identifier getRecipeId(MaterialSet set) {
		return AstromineCommon.identifier(getRecipeName(set));
	}
	default String getString() {
		return getGeneratorName()+"{"+getOutput().getName()+"}";
	}
}
