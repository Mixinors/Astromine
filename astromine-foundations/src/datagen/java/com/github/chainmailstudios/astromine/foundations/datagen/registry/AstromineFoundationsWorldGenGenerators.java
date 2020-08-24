package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.generator.worldgen.WorldGenGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.worldgen.onetime.GenericOreFeatureWorldGenGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineWorldGenGenerators;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;

public class AstromineFoundationsWorldGenGenerators extends AstromineWorldGenGenerators {
	public final WorldGenGenerator COPPER_ORE = register(new GenericOreFeatureWorldGenGenerator(AstromineFoundationsBlocks.COPPER_ORE, 0, 0, 64, 20, 14));
	public final WorldGenGenerator LEAD_ORE = register(new GenericOreFeatureWorldGenGenerator(AstromineFoundationsBlocks.LEAD_ORE, 0, 0, 48, 8, 6));
	public final WorldGenGenerator SILVER_ORE = register(new GenericOreFeatureWorldGenGenerator(AstromineFoundationsBlocks.SILVER_ORE, 0, 0, 32, 3, 9));
	public final WorldGenGenerator TIN_ORE = register(new GenericOreFeatureWorldGenGenerator(AstromineFoundationsBlocks.TIN_ORE, 0, 0, 64, 20, 11));
}
