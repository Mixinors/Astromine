package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.generators.tag.OreTagGenerator;

public class AstromineDiscoveriesTagGenerators extends AstromineTagGenerators {
	public final SetTagGenerator ASTEROID_ORES = register(new OreTagGenerator(MaterialItemType.ASTEROID_ORE));
	public final SetTagGenerator MOON_ORES = register(new OreTagGenerator(MaterialItemType.MOON_ORE));
}
