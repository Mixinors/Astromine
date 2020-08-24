package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.chainmailstudios.astromine.datagen.registry.*;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.*;

public class AstromineFoundationsDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-foundations";
	}

	@Override
	public AstromineMaterialSets getMaterialSets() {
		return new AstromineFoundationsMaterialSets();
	}

	@Override
	public AstromineLootTableGenerators getLootTableGenerators() {
		return new AstromineFoundationsLootTableGenerators();
	}

	@Override
	public AstromineRecipeGenerators getRecipeGenerators() {
		return new AstromineFoundationsRecipeGenerators();
	}

	@Override
	public AstromineTagGenerators getTagGenerators() {
		return new AstromineFoundationsTagGenerators();
	}

	@Override
	public AstromineModelStateGenerators getModelStateGenerators() {
		return new AstromineFoundationsModelStateGenerators();
	}

	@Override
	public AstromineWorldGenGenerators getWorldGenGenerators() {
		return new AstromineFoundationsWorldGenGenerators();
	}
}
