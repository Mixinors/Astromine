package com.github.chainmailstudios.astromine.common.block;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;

public class AstromineOreBlock extends OreBlock {
	public AstromineOreBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected int getExperienceWhenMined(Random random) {
		return 0;
	}
}
