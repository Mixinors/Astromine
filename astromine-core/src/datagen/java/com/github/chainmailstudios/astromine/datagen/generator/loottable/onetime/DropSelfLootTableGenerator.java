package com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime;

import net.minecraft.block.Block;

import me.shedaniel.cloth.api.datagen.v1.LootTableData;

import java.util.Arrays;
import java.util.List;

public class DropSelfLootTableGenerator implements OneTimeLootTableGenerator {
	private final List<Block> blocks;

	public DropSelfLootTableGenerator(Block... blocks) {
		this.blocks = Arrays.asList(blocks);
	}

	@Override
	public String getGeneratorName() {
		return "drop_self";
	}

	@Override
	public void generate(LootTableData data) {
		blocks.forEach(data::registerBlockDropSelf);
	}
}
