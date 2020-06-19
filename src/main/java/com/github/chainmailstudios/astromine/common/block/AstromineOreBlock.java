package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AstromineOreBlock extends OreBlock {
	public AstromineOreBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected int getExperienceWhenMined(Random random) {
		if (this == AstromineBlocks.ASTERITE_ORE) {
			return MathHelper.nextInt(random, 4, 8);
		} else if (this == AstromineBlocks.GALAXIUM_ORE) {
			return MathHelper.nextInt(random, 5, 9);
		} else if (this == AstromineBlocks.ASTEROID_METITE_ORE || this ==  AstromineBlocks.METEOR_METITE_ORE) {
			return MathHelper.nextInt(random, 3, 7);
		} else if (this == AstromineBlocks.ASTEROID_COAL_ORE) {
			return MathHelper.nextInt(random, 0, 2);
		} else if (this == AstromineBlocks.ASTEROID_DIAMOND_ORE || this == AstromineBlocks.ASTEROID_EMERALD_ORE) {
			return MathHelper.nextInt(random, 3, 7);
		} else if (this == AstromineBlocks.ASTEROID_LAPIS_ORE) {
			return MathHelper.nextInt(random, 2, 5);
		} else if (this == AstromineBlocks.ASTEROID_REDSTONE_ORE) {
			return MathHelper.nextInt(random, 2, 5);
		} else {
			return 0;
		}
	}
}
