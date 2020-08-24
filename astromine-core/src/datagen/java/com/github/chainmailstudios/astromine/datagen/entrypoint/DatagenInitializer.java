package com.github.chainmailstudios.astromine.datagen.entrypoint;

import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineMaterialSets;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface DatagenInitializer {
	default DataGeneratorHandler createHandler() {
		return DataGeneratorHandler.create(getPath());
	}

	default Path getPath() {
		return Paths.get("../" + getModuleId() + "/src/generated/resources");
	}

	String getModuleId();

	AstromineMaterialSets getMaterialSets();

	default void registerData() {
		System.out.println("Running datagen for " + getModuleId());
		DataGeneratorHandler handler = createHandler();
		registerLootTables(handler.getLootTables());
		registerRecipes(handler.getRecipes());
		registerTags(handler.getTags());
		registerModelStates(handler.getModelStates());
		handler.run();
	}

	default void registerLootTables(LootTableData lootTables) {
		if (getLootTableGenerators() != null) {
			getLootTableGenerators().generateLootTables(lootTables);
		}
	}

	default void registerRecipes(RecipeData recipes) {
		if (getRecipeGenerators() != null) {
			getRecipeGenerators().generateRecipes(recipes);
		}
	}

	default void registerTags(TagData tags) {
		if (getTagGenerators() != null) {
			getTagGenerators().generateTags(tags);
		}
	}

	default void registerModelStates(ModelStateData modelStates) {
		if (getModelStateGenerators() != null) {
			getModelStateGenerators().generateModelStates(modelStates);
		}
	}

	AstromineLootTableGenerators getLootTableGenerators();

	AstromineRecipeGenerators getRecipeGenerators();

	AstromineTagGenerators getTagGenerators();

	AstromineModelStateGenerators getModelStateGenerators();
}
