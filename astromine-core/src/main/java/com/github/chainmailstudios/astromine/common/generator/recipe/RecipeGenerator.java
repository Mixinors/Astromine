package com.github.chainmailstudios.astromine.common.generator.recipe;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialEntry;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import com.github.chainmailstudios.astromine.common.generator.SetGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.util.Identifier;

public interface RecipeGenerator extends SetGenerator<RecipeData> {
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
