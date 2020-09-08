package com.github.chainmailstudios.astromine.datagen.generator.modelstate.set;

import net.minecraft.block.Block;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

public class GenericBlockSetModelStateGenerator extends GenericItemSetModelGenerator {

	public GenericBlockSetModelStateGenerator(MaterialItemType type) {
		super(type);
		if (!type.isBlock()) throw new IllegalArgumentException("type " + type.getName() + " isn't a block");
	}

	@Override
	public void generate(ModelStateData data, MaterialSet set) {
		data.addSingletonCubeAll(getBlock(set));
		data.addSimpleBlockItemModel(getBlock(set));
	}

	public Block getBlock(MaterialSet set) {
		return set.getEntry(type).asBlock();
	}

	@Override
	public String getGeneratorName() {
		return type.getName() + "_block_set_modelstate";
	}
}
