package com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.SetGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialEntry;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public interface SetRecipeGenerator extends SetGenerator<RecipeData>, RecipeGenerator {
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
		return getGeneratorName() + "{" + getOutput().getName() + "}";
	}
}
