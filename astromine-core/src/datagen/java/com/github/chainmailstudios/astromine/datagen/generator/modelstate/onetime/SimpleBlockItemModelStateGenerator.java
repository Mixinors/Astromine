package com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime;

import net.minecraft.block.Block;
import net.minecraft.data.client.model.ModelIds;

import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

public class SimpleBlockItemModelStateGenerator extends GenericBlockModelStateGenerator {
	public SimpleBlockItemModelStateGenerator(Block... blocks) {
		super(blocks);
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach((block) -> {
			data.addState(block, ModelStateData.createSingletonBlockState(block, ModelIds.getBlockModelId(block)));
			data.addSimpleBlockItemModel(block);
		});
	}

	@Override
	public String getGeneratorName() {
		return "block_item_modelstate";
	}
}
