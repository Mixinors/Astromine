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

package com.github.mixinors.astromine.common.recipe.base.input;

import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

public interface DoubleFluidInputRecipe extends FluidInputRecipe {
	@Override
	default FluidIngredient getInput() {
		return getFirstInput();
	}

	FluidIngredient getFirstInput();

	FluidIngredient getSecondInput();

	@Override
	default boolean allows(FluidVariant... variants) {
		var firstInputVariant = variants[0];
		var secondInputVariant = variants[1];

		if (!getFirstInput().test(firstInputVariant, Long.MAX_VALUE) && !getSecondInput().test(firstInputVariant, Long.MAX_VALUE)) {
			return false;
		}

		return getSecondInput().test(secondInputVariant, Long.MAX_VALUE) || getFirstInput().test(secondInputVariant, Long.MAX_VALUE);
	}
}
