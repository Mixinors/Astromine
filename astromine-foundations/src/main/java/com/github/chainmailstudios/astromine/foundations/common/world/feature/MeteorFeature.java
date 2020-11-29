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

package com.github.chainmailstudios.astromine.foundations.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class MeteorFeature extends StructureFeature<NoneFeatureConfiguration> {

	public MeteorFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return MeteorFeature.Start::new;
	}

	public static class Start extends StructureStart<NoneFeatureConfiguration> {

		public Start(StructureFeature<NoneFeatureConfiguration> structureFeature, int i, int j, BoundingBox blockBox, int k, long l) {
			super(structureFeature, i, j, blockBox, k, l);
		}

		public void generatePieces(RegistryAccess drm, ChunkGenerator chunkGenerator, StructureManager structureManager, int i, int j, Biome biome, NoneFeatureConfiguration defaultFeatureConfig) {
			MeteorGenerator meteorGenerator = new MeteorGenerator(this.random, i * 16, j * 16);
			this.pieces.add(meteorGenerator);
			this.calculateBoundingBox();
		}
	}
}
