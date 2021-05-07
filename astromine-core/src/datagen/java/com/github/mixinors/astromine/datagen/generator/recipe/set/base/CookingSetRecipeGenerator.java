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

package com.github.mixinors.astromine.datagen.generator.recipe.set.base;

import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;

public abstract class CookingSetRecipeGenerator extends SimpleProcessingSetRecipeGenerator {
	public final float experience;

	public CookingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, 1, output, 1, time);
		this.experience = experience;
	}

	public CookingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int time) {
		this(input, output, time, 0.1f);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		if (set.isFromVanilla(input) && set.isFromVanilla(output)) return false;
		else return super.shouldGenerate(set);
	}

	@Override
	public String getGeneratorName() {
		return "cooking";
	}
}
