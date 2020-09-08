package com.github.chainmailstudios.astromine.datagen.generator.modelstate.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

public class GenericItemSetModelGenerator implements SetModelStateGenerator {
	protected final MaterialItemType type;

	public GenericItemSetModelGenerator(MaterialItemType type) {
		this.type = type;
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(type) && !set.isFromVanilla(type);
	}

	@Override
	public void generate(ModelStateData data, MaterialSet set) {
		data.addGeneratedItemModel(set.getItem(type));
	}

	@Override
	public String getGeneratorName() {
		return type.getName() + "_item_set_modelstate";
	}
}
