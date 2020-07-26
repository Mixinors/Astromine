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

package com.github.chainmailstudios.astromine.common.world.biome;

import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;

import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class MarsBiome extends Biome {
	public MarsBiome(float baseHeight, float variation) {
		super(new Settings().configureSurfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_CONFIG).precipitation(Precipitation.NONE).category(Category.NONE).depth(baseHeight).scale(variation).temperature(0.5F).downfall(0.5F).effects(new BiomeEffects.Builder().waterColor(
			4159204).waterFogColor(329011).fogColor(12638463).particleConfig(new BiomeParticleConfig(AstromineParticles.MARS_DUST, 0.00225F)).moodSound(BiomeMoodSound.CAVE).build()).parent(null));
		this.addFeature(GenerationStep.Feature.RAW_GENERATION, AstromineFeatures.MOON_CRATER.configure(DefaultFeatureConfig.INSTANCE).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(1))));
	}

	@Override
	public int getSkyColor() {
		return 0xcf940c;
	}
}
