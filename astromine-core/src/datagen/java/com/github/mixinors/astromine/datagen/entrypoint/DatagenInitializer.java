/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.datagen.entrypoint;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.registry.AMLootTableGenerators;
import com.github.mixinors.astromine.datagen.registry.AMMaterialSets;
import com.github.mixinors.astromine.datagen.registry.AMModelStateGenerators;
import com.github.mixinors.astromine.datagen.registry.AMRecipeGenerators;
import com.github.mixinors.astromine.datagen.registry.AMTagGenerators;
import com.github.mixinors.astromine.datagen.registry.AMWorldGenGenerators;
import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import me.shedaniel.cloth.api.datagen.v1.WorldGenData;

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

	AMMaterialSets getMaterialSets();

	default void registerData() {
		AMCommon.LOGGER.info("Initializing data generation for " + getModuleId() + ".");
		DataGeneratorHandler handler = createHandler();
		registerLootTables(handler.getLootTables());
		registerRecipes(handler.getRecipes());
		registerTags(handler.getTags());
		registerModelStates(handler.getModelStates());
		registerWorldGen(handler.getWorldGen());
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

	default void registerWorldGen(WorldGenData worldGen) {
		if (getWorldGenGenerators() != null) {
			getWorldGenGenerators().generateWorldGen(worldGen);
		}
	}

	AMLootTableGenerators getLootTableGenerators();

	AMRecipeGenerators getRecipeGenerators();

	AMTagGenerators getTagGenerators();

	AMModelStateGenerators getModelStateGenerators();

	AMWorldGenGenerators getWorldGenGenerators();
}
