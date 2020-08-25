package com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

public abstract class CookingSetRecipeGenerator extends SimpleProcessingSetRecipeGenerator {
	public final float experience;

	public CookingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, 1, output, 1, time);
		this.experience = experience;
	}

	public CookingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int time) {
		this(input, output, time, 0.1f);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		if (set.isFromVanilla(input) && set.isFromVanilla(output)) return false;
		else return super.shouldGenerate(set);
	}

	@Override
	public String getGeneratorName() {
		return "cooking";
	}
}
