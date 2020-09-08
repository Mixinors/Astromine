package com.github.chainmailstudios.astromine.datagen.generator.modelstate.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

public class HandheldItemSetModelGenerator extends GenericItemSetModelGenerator {

	public HandheldItemSetModelGenerator(MaterialItemType type) {
		super(type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(type);
	}

	@Override
	public void generate(ModelStateData data, MaterialSet set) {
		data.addHandheldItemModel(set.getItem(type));
	}

	@Override
	public String getGeneratorName() {
		return type.getName() + "_handheld_item_set_modelstate";
	}
}
