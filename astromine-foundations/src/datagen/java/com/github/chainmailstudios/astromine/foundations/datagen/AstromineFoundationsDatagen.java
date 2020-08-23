package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsRecipeGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsTagGenerators;

public class AstromineFoundationsDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-foundations";
	}

	@Override
	public AstromineLootTableGenerators getLootTableGenerators() {
		return null;
	}

	@Override
	public AstromineRecipeGenerators getRecipeGenerators() {
		return new AstromineFoundationsRecipeGenerators();
	}

	@Override
	public AstromineTagGenerators getTagGenerators() {
		return new AstromineFoundationsTagGenerators();
	}
}
