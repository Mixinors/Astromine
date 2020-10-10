package com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime;

import net.minecraft.block.Block;

import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

public class SimpleBlockItemModelGenerator extends GenericBlockModelStateGenerator {
	public SimpleBlockItemModelGenerator(Block... blocks) {
		super(blocks);
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach(data::addSimpleBlockItemModel);
	}

	@Override
	public String getGeneratorName() {
		return "block_item_model";
	}
}
