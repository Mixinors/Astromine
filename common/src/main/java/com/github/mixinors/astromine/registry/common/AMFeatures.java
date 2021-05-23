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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.world.feature.*;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.registry.RegistrySupplier;
import me.shedaniel.architectury.targets.ArchitecturyTarget;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.feature.*;

import java.util.function.Supplier;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	public static RegistrySupplier<Feature<DefaultFeatureConfig>> ASTEROID_ORES = registerFeature(ASTEROID_ORES_ID, () -> new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	public static RegistryKey<ConfiguredFeature<?, ?>> ASTEROID_ORES_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, ASTEROID_ORES_ID);
	
	public static final Identifier METEOR_ID = AMCommon.id("meteor");
	public static RegistrySupplier<StructurePieceType> METEOR_STRUCTURE = registerStructurePiece(METEOR_ID, () -> MeteorGenerator::new);
	public static RegistryKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, METEOR_ID);
	
	public static final Identifier CRUDE_OIL_ID = AMCommon.id("crude_oil");
	public static RegistrySupplier<Feature<DefaultFeatureConfig>> CRUDE_OIL = registerFeature(CRUDE_OIL_ID, () -> new CrudeOilFeature(DefaultFeatureConfig.CODEC));
	public static RegistryKey<ConfiguredFeature<?, ?>> CRUDE_OIL_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, CRUDE_OIL_ID);
	
	public static final Identifier ASTROMINE_BIOME_MODIFICATIONS = AMCommon.id("biome_modifications");
	
	public static void init() {
		postInit();
	}
	
	@ExpectPlatform
	public static void postInit() {
		throw new AssertionError();
	}
	
	public static <T extends FeatureConfig> RegistrySupplier<Feature<T>> registerFeature(Identifier id, Supplier<Feature<T>> feature) {
		return AMCommon.registry(Registry.FEATURE_KEY).registerSupplied(id, feature);
	}

	public static <T extends StructurePieceType> RegistrySupplier<T> registerStructurePiece( Identifier id, Supplier<T> pieceType) {
		return AMCommon.registry(Registry.STRUCTURE_PIECE_KEY).registerSupplied(id, pieceType);
	}
}
