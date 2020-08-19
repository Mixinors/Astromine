package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;

public abstract class CookingRecipeGenerator extends SimpleProcessingRecipeGenerator {
	public final float experience;

	public CookingRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, 1, output, 1, time);
		this.experience = experience;
	}

	public CookingRecipeGenerator(MaterialItemType input, MaterialItemType output, int time) {
		this(input, output, time, 0.1f);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		if(set.isFromVanilla(input) && set.isFromVanilla(output)) return false;
		else return super.shouldGenerate(set);
	}

	@Override
	public String getGeneratorName() {
		return "cooking";
	}
}
