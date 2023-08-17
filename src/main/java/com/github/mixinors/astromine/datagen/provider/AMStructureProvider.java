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

package com.github.mixinors.astromine.datagen.provider;

import com.github.mixinors.astromine.common.world.structure.CraterStructure;
import com.github.mixinors.astromine.common.world.structure.MeteorStructure;
import com.github.mixinors.astromine.registry.common.AMStructureTypes;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AMStructureProvider extends FabricDynamicRegistryProvider {
	public AMStructureProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		RegistryEntryLookup<Biome> biomeLookup = entries.getLookup(RegistryKeys.BIOME);
		registerStructure(entries, AMStructureTypes.METEOR_ID, new MeteorStructure(createConfig(biomeLookup, BiomeTags.IS_OVERWORLD)));
		registerStructure(entries, AMStructureTypes.CRATER_ID, new CraterStructure(createConfig(biomeLookup, AMTagKeys.BiomeTags.IS_MOON)));
	}
	
	public static RegistryEntry<Structure> registerStructure(Entries entries, Identifier id, Structure structure) {
		return entries.add(RegistryKey.of(RegistryKeys.STRUCTURE, id), structure);
	}
	
	private static Structure.Config createConfig(RegistryEntryLookup<Biome> biomeLookup, TagKey<Biome> biomeTag, Map<SpawnGroup, StructureSpawns> spawns) {
		return new Structure.Config(getOrCreateBiomeTag(biomeLookup, biomeTag), spawns, GenerationStep.Feature.SURFACE_STRUCTURES, StructureTerrainAdaptation.NONE);
	}
	
	private static Structure.Config createConfig(RegistryEntryLookup<Biome> biomeLookup, TagKey<Biome> biome) {
		return createConfig(biomeLookup, biome, Map.of());
	}
	
	private static RegistryEntryList<Biome> getOrCreateBiomeTag(RegistryEntryLookup<Biome> biomeLookup, TagKey<Biome> key) {
		return RegistryEntryList.of((RegistryEntryOwner<Biome>) biomeLookup, key);
	}
	
	@Override
	public String getName() {
		return "Astromine Structures";
	}
}
