/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.datagen;

import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.AMMaterialFamilies;
import com.github.mixinors.astromine.datagen.provider.AMBlockLootTableProvider;
import com.github.mixinors.astromine.datagen.provider.AMEntityLootTableProvider;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import com.github.mixinors.astromine.datagen.provider.AMRecipeProvider;
import com.github.mixinors.astromine.datagen.provider.tag.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.Map;
import java.util.TreeMap;

public class AMDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		AMBlockFamilies.init();
		AMMaterialFamilies.init();
		
		dataGenerator.addProvider(AMModelProvider::new);
		dataGenerator.addProvider(AMRecipeProvider::new);
		
		var blockTagProvider = new AMBlockTagProvider(dataGenerator);
		
		dataGenerator.addProvider(blockTagProvider);
		
		dataGenerator.addProvider(new AMItemTagProvider(dataGenerator, blockTagProvider));
		
		dataGenerator.addProvider(AMFluidTagProvider::new);
		dataGenerator.addProvider(AMEntityTypeTagProvider::new);
		dataGenerator.addProvider(AMDimensionTypeTagProvider::new);
		dataGenerator.addProvider(AMBlockLootTableProvider::new);
		dataGenerator.addProvider(AMEntityLootTableProvider::new);
	}
	
	public static <T extends Comparable<?>, U> TreeMap<T, U> toTreeMap(Map<T, U> map) {
		return new TreeMap<>(map);
	}
}
