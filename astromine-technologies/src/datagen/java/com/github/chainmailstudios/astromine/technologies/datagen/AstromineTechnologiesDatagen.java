package com.github.chainmailstudios.astromine.technologies.datagen;

import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.chainmailstudios.astromine.datagen.registry.*;

public class AstromineTechnologiesDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-technologies";
	}

	@Override
	public AstromineMaterialSets getMaterialSets() {
		return null;
	}

	@Override
	public AstromineLootTableGenerators getLootTableGenerators() {
		return null;
	}

	@Override
	public AstromineRecipeGenerators getRecipeGenerators() {
		return new AstromineTechnologiesRecipeGenerators();
	}

	@Override
	public AstromineTagGenerators getTagGenerators() {
		return null;
	}

	@Override
	public AstromineModelStateGenerators getModelStateGenerators() {
		return new AstromineTechnologiesModelStateGenerators();
	}

	@Override
	public AstromineWorldGenGenerators getWorldGenGenerators() {
		return null;
	}
}
