package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineMaterialSets;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineWorldGenGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsLootTableGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsMaterialSets;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsModelStateGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsRecipeGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsTagGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsWorldGenGenerators;

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
