package com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime;

import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.OneTimeModelStateGenerator;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;
import net.minecraft.block.Block;
import net.minecraft.data.client.model.ModelIds;

import java.util.Arrays;
import java.util.List;

public class SimpleBlockItemModelStateGenerator implements OneTimeModelStateGenerator {
	private List<Block> blocks;

	public SimpleBlockItemModelStateGenerator(Block... blocks) {
		this.blocks = Arrays.asList(blocks);
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
		return "block_item_set_modelstate";
	}
}
