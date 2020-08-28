package com.github.chainmailstudios.astromine.discoveries.common.block;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import com.github.chainmailstudios.astromine.foundations.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AsteroidOreBlock extends AstromineOreBlock {
	public AsteroidOreBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected int getExperienceWhenMined(Random random) {
		if (this == AstromineDiscoveriesBlocks.ASTEROID_ASTERITE_ORE) {
			return MathHelper.nextInt(random, 5, 8);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_GALAXIUM_ORE || this == AstromineDiscoveriesBlocks.ASTEROID_STELLUM_ORE) {
			return MathHelper.nextInt(random, 6, 9);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_METITE_ORE) {
			return MathHelper.nextInt(random, 4, 7);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_COAL_ORE) {
			return MathHelper.nextInt(random, 0, 2);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_TIN_ORE || this == AstromineDiscoveriesBlocks.ASTEROID_COPPER_ORE) {
			return MathHelper.nextInt(random, 1, 2);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_IRON_ORE) {
			return MathHelper.nextInt(random, 1, 3);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_GOLD_ORE) {
			return MathHelper.nextInt(random, 2, 3);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_DIAMOND_ORE || this == AstromineDiscoveriesBlocks.ASTEROID_EMERALD_ORE) {
			return MathHelper.nextInt(random, 3, 7);
		} else if (this == AstromineDiscoveriesBlocks.ASTEROID_LAPIS_ORE || this == AstromineDiscoveriesBlocks.ASTEROID_REDSTONE_ORE) {
			return MathHelper.nextInt(random, 2, 5);
		} else {
			return 0;
		}
	}
}
