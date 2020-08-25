package com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

public abstract class CraftingSetRecipeGenerator implements SetRecipeGenerator {
	public final MaterialItemType input;
	public final MaterialItemType output;
	public final int outputCount;

	public CraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount) {
		this.input = input;
		this.output = output;
		this.outputCount = outputCount;
	}

	public String getRecipeSuffix() {
		return "";
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + getRecipeSuffix();
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		if (!set.hasType(input) || !set.hasType(output)) return false;
		else return !set.isFromVanilla(input) || !set.isFromVanilla((output));
	}

	@Override
	public MaterialItemType getOutput() {
		return output;
	}
}
