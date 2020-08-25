package com.github.chainmailstudios.astromine.datagen.generator.loottable.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;

public class DropSelfSetLootTableGenerator extends GenericSetLootTableGenerator {
	public DropSelfSetLootTableGenerator(MaterialItemType type) {
		super(type);
	}

	@Override
	public void generate(LootTableData data, MaterialSet set) {
		data.registerBlockDropSelf(set.getEntry(type).asBlock());
	}

	@Override
	public String getGeneratorName() {
		return "drop_self_set";
	}
}
