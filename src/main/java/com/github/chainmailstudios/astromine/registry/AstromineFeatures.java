/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
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
package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.feature.AsteroidOreFeature;
import com.github.chainmailstudios.astromine.common.world.feature.MeteorFeature;
import com.github.chainmailstudios.astromine.common.world.feature.MeteorGenerator;
import net.earthcomputer.libstructure.LibStructure;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Locale;

public class AstromineFeatures {
	public static Feature<DefaultFeatureConfig> ASTEROID_ORES;

	public static final Feature<DefaultFeatureConfig> ASTEROIDS = Registry.register(
			Registry.FEATURE,
			AstromineCommon.identifier("asteroids_feature"),
			new AsteroidOreFeature(DefaultFeatureConfig.CODEC)
	);

	public static final StructurePieceType METEOR = register(MeteorGenerator::new, "meteor");

	public static StructurePieceType register(StructurePieceType pieceType, String id) {
		return Registry.register(Registry.STRUCTURE_PIECE, AstromineCommon.identifier(id.toLowerCase(Locale.ROOT)), pieceType);
	}

	public static void initialize() {
		ASTEROID_ORES = Registry.register(Registry.FEATURE, AstromineCommon.identifier("asteroid_ores"), new AsteroidOreFeature(DefaultFeatureConfig.CODEC));

		// initialize meteor structure/feature
		MeteorFeature meteor = new MeteorFeature(DefaultFeatureConfig.CODEC);
		DefaultFeatureConfig config = new DefaultFeatureConfig();
		ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> meteorStructure = meteor.configure(config);
		LibStructure.registerStructure(AstromineCommon.identifier("meteor"), meteor, GenerationStep.Feature.RAW_GENERATION, new StructureConfig(32, 8, 12345), meteorStructure);

		Registry.BIOME.forEach(biome -> {
			biome.addStructureFeature(meteorStructure);
		});
	}
}
