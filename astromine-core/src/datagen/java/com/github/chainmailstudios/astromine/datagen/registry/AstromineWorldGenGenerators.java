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

package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.worldgen.onetime.OneTimeWorldGenGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.worldgen.set.SetWorldGenGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.WorldGenData;

import java.util.ArrayList;
import java.util.List;

public abstract class AstromineWorldGenGenerators {
	private final List<SetWorldGenGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeWorldGenGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public SetWorldGenGenerator register(SetWorldGenGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeWorldGenGenerator register(OneTimeWorldGenGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateWorldGen(WorldGenData worldGen) {
		AstromineMaterialSets.getMaterialSets().forEach((set) -> generateSetWorldGen(worldGen, set));
		generateOneTimeWorldGen(worldGen);
	}

	private void generateSetWorldGen(WorldGenData worldGen, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(worldGen, set);
					AstromineCommon.LOGGER.info("World generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AstromineCommon.LOGGER.error("World generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AstromineCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeWorldGen(WorldGenData worldGen) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(worldGen));
	}
}
