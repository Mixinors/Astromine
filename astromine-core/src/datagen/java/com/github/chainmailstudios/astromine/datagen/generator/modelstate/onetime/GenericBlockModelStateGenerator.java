package com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime;

import net.minecraft.block.Block;

import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

import java.util.Arrays;
import java.util.List;

public class GenericBlockModelStateGenerator implements OneTimeModelStateGenerator {
	protected final List<Block> blocks;

	public GenericBlockModelStateGenerator(Block... blocks) {
		this.blocks = Arrays.asList(blocks);
	}

	@Override
	public String getGeneratorName() {
		return "generic_block_modelstate";
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach((block) -> {
			data.addSingletonCubeAll(block);
			data.addSimpleBlockItemModel(block);
		});
	}
}
