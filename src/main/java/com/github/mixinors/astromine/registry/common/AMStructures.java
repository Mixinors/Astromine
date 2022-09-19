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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.world.structure.CraterGenerator;
import com.github.mixinors.astromine.common.world.structure.CraterStructure;
import com.github.mixinors.astromine.common.world.structure.MeteorGenerator;
import com.github.mixinors.astromine.common.world.structure.MeteorStructure;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Map;
import java.util.function.Supplier;

public class AMStructures {
	public static final Identifier METEOR_ID = AMCommon.id("meteor");
	public static final RegistryEntry<Structure> METEOR_STRUCTURE_FEATURE = registerStructure(METEOR_ID, new MeteorStructure(createConfig(BiomeTags.IS_OVERWORLD)), MeteorStructure.TYPE);
	public static final RegistrySupplier<StructurePieceType> METEOR_STRUCTURE_PIECE = registerStructurePiece(METEOR_ID, () -> (StructurePieceType.Simple) MeteorGenerator::new);
	
	public static final Identifier CRATER_ID = AMCommon.id("crater");
	public static final RegistryEntry<Structure> CRATER_STRUCTURE_FEATURE = registerStructure(CRATER_ID, new CraterStructure(createConfig(AMTagKeys.BiomeTags.IS_MOON)), CraterStructure.TYPE);
	public static final RegistrySupplier<StructurePieceType> CRATER_STRUCTURE_PIECE = registerStructurePiece(CRATER_ID, () -> (StructurePieceType.Simple) CraterGenerator::new);
	
	public static void init() {}
	
	public static RegistryEntry<Structure> registerStructure(Identifier id, Structure structure, StructureType<?> type) {
		Registry.register(Registry.STRUCTURE_TYPE, id, type);
		return BuiltinRegistries.add(BuiltinRegistries.STRUCTURE, RegistryKey.of(Registry.STRUCTURE_KEY, id), structure);
	}
	
	public static <T extends StructurePieceType> RegistrySupplier<T> registerStructurePiece(Identifier id, Supplier<T> pieceType) {
		return AMCommon.registry(Registry.STRUCTURE_PIECE_KEY).register(id, pieceType);
	}
	
	private static Structure.Config createConfig(TagKey<Biome> biomeTag, Map<SpawnGroup, StructureSpawns> spawns) {
		return new Structure.Config(getOrCreateBiomeTag(biomeTag), spawns, GenerationStep.Feature.SURFACE_STRUCTURES, StructureTerrainAdaptation.NONE);
	}
	
	private static Structure.Config createConfig(TagKey<Biome> biome) {
		return createConfig(biome, Map.of());
	}
	
	private static RegistryEntryList<Biome> getOrCreateBiomeTag(TagKey<Biome> key) {
		return BuiltinRegistries.BIOME.getOrCreateEntryList(key);
	}
}
