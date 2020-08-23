package com.github.chainmailstudios.astromine.datagen.generator.loottable.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;

public class DropSelfSetLootTableGenerator implements SetLootTableGenerator {
	private final MaterialItemType type;

	public DropSelfSetLootTableGenerator(MaterialItemType type) {
		if(!type.isBlock()) throw new IllegalArgumentException("type "+type.getName()+" isn't a block");
		else this.type = type;
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(type);
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
