package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.utilities.data.Range;

public class AstromineOres {
	public static void initialize() {
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_COAL_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_IRON_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_GOLD_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 32), AstromineBlocks.ASTEROID_REDSTONE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 32), AstromineBlocks.ASTEROID_LAPIS_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 16), AstromineBlocks.ASTEROID_DIAMOND_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 16), AstromineBlocks.ASTEROID_EMERALD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_METITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTERITE_ORE);
	}
}
