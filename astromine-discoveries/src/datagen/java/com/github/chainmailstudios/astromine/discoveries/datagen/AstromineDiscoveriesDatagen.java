package com.github.chainmailstudios.astromine.discoveries.datagen;

import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.chainmailstudios.astromine.datagen.registry.*;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesLootTableGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesModelStateGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesRecipeGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesTagGenerators;

public class AstromineDiscoveriesDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-discoveries";
	}

	@Override
	public AstromineMaterialSets getMaterialSets() {
		return null;
	}

	@Override
	public AstromineLootTableGenerators getLootTableGenerators() {
		return new AstromineDiscoveriesLootTableGenerators();
	}

	@Override
	public AstromineRecipeGenerators getRecipeGenerators() {
		return new AstromineDiscoveriesRecipeGenerators();
	}

	@Override
	public AstromineTagGenerators getTagGenerators() {
		return new AstromineDiscoveriesTagGenerators();
	}

	@Override
	public AstromineModelStateGenerators getModelStateGenerators() {
		return new AstromineDiscoveriesModelStateGenerators();
	}

	@Override
	public AstromineWorldGenGenerators getWorldGenGenerators() {
		return new AstromineWorldGenGenerators() {};
	}
}
