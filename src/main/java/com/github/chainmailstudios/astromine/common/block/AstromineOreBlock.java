package com.github.chainmailstudios.astromine.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;

import java.util.Random;

public class AstromineOreBlock extends OreBlock {
	public AstromineOreBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected int getExperienceWhenMined(Random random) {
		if (this == AstromineBlocks.ASTEROID_ASTERITE_ORE) {
			return MathHelper.nextInt(random, 5, 8);
		} else if (this == AstromineBlocks.ASTEROID_GALAXIUM_ORE || this == AstromineBlocks.ASTEROID_STELLUM_ORE) {
			return MathHelper.nextInt(random, 6, 9);
		} else if (this == AstromineBlocks.ASTEROID_METITE_ORE || this == AstromineBlocks.METEOR_METITE_ORE) {
			return MathHelper.nextInt(random, 4, 7);
		} else if (this == AstromineBlocks.ASTEROID_COAL_ORE) {
			return MathHelper.nextInt(random, 0, 2);
		} else if (this == AstromineBlocks.ASTEROID_TIN_ORE || this == AstromineBlocks.ASTEROID_COPPER_ORE) {
			return MathHelper.nextInt(random, 1, 2);
		} else if (this == AstromineBlocks.ASTEROID_IRON_ORE) {
			return MathHelper.nextInt(random, 1, 3);
		} else if (this == AstromineBlocks.ASTEROID_GOLD_ORE) {
			return MathHelper.nextInt(random, 2, 3);
		} else if (this == AstromineBlocks.ASTEROID_DIAMOND_ORE || this == AstromineBlocks.ASTEROID_EMERALD_ORE) {
			return MathHelper.nextInt(random, 3, 7);
		} else if (this == AstromineBlocks.ASTEROID_LAPIS_ORE || this == AstromineBlocks.ASTEROID_REDSTONE_ORE) {
			return MathHelper.nextInt(random, 2, 5);
		} else {
			return 0;
		}
	}
}
