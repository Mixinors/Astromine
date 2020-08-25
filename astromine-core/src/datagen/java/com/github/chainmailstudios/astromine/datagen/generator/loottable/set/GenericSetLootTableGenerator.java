package com.github.chainmailstudios.astromine.datagen.generator.loottable.set;

import net.minecraft.block.Block;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

public abstract class GenericSetLootTableGenerator implements SetLootTableGenerator {
	protected final MaterialItemType type;

	public GenericSetLootTableGenerator(MaterialItemType type) {
		if (!type.isBlock()) throw new IllegalArgumentException("type " + type.getName() + " isn't a block");
		else this.type = type;
	}

	public Block getBlock(MaterialSet set) {
		return set.getEntry(type).asBlock();
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(type) && !set.isFromVanilla(type);
	}
}
