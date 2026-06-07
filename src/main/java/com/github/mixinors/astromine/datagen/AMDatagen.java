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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.AMMaterialFamilies;
import com.github.mixinors.astromine.datagen.provider.AMBlockLootTableProvider;
import com.github.mixinors.astromine.datagen.provider.AMEntityLootTableProvider;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import com.github.mixinors.astromine.datagen.provider.AMRecipeProvider;
import com.github.mixinors.astromine.datagen.provider.tag.*;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AMDatagen {
	public static void init() {
		AMCommon.modEventBus().addListener(AMDatagen::gatherData);
	}
	
	public static void gatherData(GatherDataEvent event) {
		AMBlockFamilies.init();
		AMMaterialFamilies.init();
		
		var lookupProvider = event.getLookupProvider();
		var existingFileHelper = event.getExistingFileHelper();
		
		event.createProvider(AMModelProvider::new);
		event.createProvider(AMRecipeProvider::new);
		
		event.createBlockAndItemTags(
				(output, lookup) -> new AMBlockTagProvider(output, lookup, existingFileHelper),
				(output, lookup, blockTags) -> new AMItemTagProvider(output, lookup, blockTags, existingFileHelper)
		);
		event.addProvider(new AMFluidTagProvider(event.getGenerator().getPackOutput(), lookupProvider, existingFileHelper));
		event.addProvider(new AMEntityTypeTagProvider(event.getGenerator().getPackOutput(), lookupProvider, existingFileHelper));
		event.addProvider(new AMDimensionTypeTagProvider(event.getGenerator().getPackOutput(), lookupProvider, existingFileHelper));
		event.addProvider(new LootTableProvider(
				event.getGenerator().getPackOutput(),
				Set.of(),
				List.of(
						new LootTableProvider.SubProviderEntry(AMBlockLootTableProvider::new, LootContextParamSets.BLOCK),
						new LootTableProvider.SubProviderEntry(AMEntityLootTableProvider::new, LootContextParamSets.ENTITY)
				),
				lookupProvider
		));
	}
	
	public static <T extends Comparable<?>, U> TreeMap<T, U> toTreeMap(Map<T, U> map) {
		return new TreeMap<>(map);
	}
}
