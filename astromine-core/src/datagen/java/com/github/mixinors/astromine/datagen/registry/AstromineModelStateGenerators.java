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

package com.github.mixinors.astromine.datagen.registry;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.OneTimeModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.SetModelStateGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

import java.util.ArrayList;
import java.util.List;

public abstract class AstromineModelStateGenerators {
	private final List<SetModelStateGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeModelStateGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public SetModelStateGenerator register(SetModelStateGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeModelStateGenerator register(OneTimeModelStateGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateModelStates(ModelStateData modelStates) {
		AstromineMaterialSets.getMaterialSets().forEach((set) -> generateSetModelStates(modelStates, set));
		generateOneTimeRecipes(modelStates);
	}

	private void generateSetModelStates(ModelStateData modelStates, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(modelStates, set);
					AMCommon.LOGGER.info("Model/State generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AMCommon.LOGGER.error("Model/State generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AMCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeRecipes(ModelStateData modelStates) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(modelStates));
	}
}
