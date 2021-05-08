package com.github.mixinors.astromine.datagen;

import com.github.mixinors.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.mixinors.astromine.datagen.registry.*;

public class AMDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-core";
	}
	
	@Override
	public AMMaterialSets getMaterialSets() {
		return new AMMaterialSets();
	}
	
	@Override
	public AMLootTableGenerators getLootTableGenerators() {
		return new AMLootTableGenerators();
	}
	
	@Override
	public AMRecipeGenerators getRecipeGenerators() {
		return new AMRecipeGenerators();
	}
	
	@Override
	public AMTagGenerators getTagGenerators() {
		return new AMTagGenerators();
	}
	
	@Override
	public AMModelStateGenerators getModelStateGenerators() {
		return new AMModelStateGenerators();
	}
	
	@Override
	public AMWorldGenGenerators getWorldGenGenerators() {
		return new AMWorldGenGenerators();
	}
}
