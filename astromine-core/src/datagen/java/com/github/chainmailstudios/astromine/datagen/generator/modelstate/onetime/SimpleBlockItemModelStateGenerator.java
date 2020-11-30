package com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime;

import me.shedaniel.cloth.api.datagen.v1.ModelStateData;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;

public class SimpleBlockItemModelStateGenerator extends GenericBlockModelStateGenerator {
	public SimpleBlockItemModelStateGenerator(Block... blocks) {
		super(blocks);
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach((block) -> {
			data.addState(block, ModelStateData.createSingletonBlockState(block, ModelLocationUtils.getModelLocation(block)));
			data.addSimpleBlockItemModel(block);
		});
	}

	@Override
	public String getGeneratorName() {
		return "block_item_modelstate";
	}
}
