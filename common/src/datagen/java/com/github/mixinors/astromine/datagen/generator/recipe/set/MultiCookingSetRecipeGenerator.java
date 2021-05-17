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

package com.github.mixinors.astromine.datagen.generator.recipe.set;

import net.minecraft.item.Item;

import com.github.mixinors.astromine.datagen.generator.recipe.set.base.CookingSetRecipeGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MultiCookingSetRecipeGenerator extends CookingSetRecipeGenerator {
	private final Collection<MaterialItemType> inputs;

	public MultiCookingSetRecipeGenerator(Collection<MaterialItemType> inputs, MaterialItemType output, int time, float experience) {
		super(inputs.stream().findFirst().get(), output, time, experience);
		this.inputs = inputs;
	}

	public Collection<MaterialItemType> getInputs() {
		return inputs;
	}

	public Collection<Item> getInputs(MaterialSet set) {
		ArrayList<Item> items = new ArrayList<>();
		for (MaterialItemType type : getInputs()) {
			if (shouldGenerate(set, type)) items.add(set.getItem(type));
		}
		return items;
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		boolean hasInput = false;
		for (MaterialItemType type : getInputs()) {
			if (set.hasType(type)) hasInput = true;
		}
		return hasInput && set.hasType(output);
	}

	public boolean shouldGenerate(MaterialSet set, MaterialItemType type) {
		return set.hasType(output) && set.hasType(type) && !(set.isFromVanilla(type) && set.isFromVanilla(output));
	}
}
