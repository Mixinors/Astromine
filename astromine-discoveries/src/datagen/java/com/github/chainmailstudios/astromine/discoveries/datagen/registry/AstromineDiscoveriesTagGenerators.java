package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.tag.onetime.OneTimeTagGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.onetime.TagInTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.generators.tag.*;
import com.github.chainmailstudios.astromine.datagen.generator.tag.onetime.ItemInTagGenerator;

public class AstromineDiscoveriesTagGenerators extends AstromineTagGenerators {
	public final SetTagGenerator ASTEROID_ORES = register(new OreTagGenerator(MaterialItemType.ASTEROID_ORE));
}
