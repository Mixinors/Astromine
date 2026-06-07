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

package com.github.mixinors.astromine.common.recipe.base.input;

import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import net.minecraft.world.item.ItemStack;

public interface DoubleItemInputRecipe extends ItemInputRecipe {
	@Override
	default ItemIngredient getInput() {
		return getFirstInput();
	}
	
	ItemIngredient getFirstInput();
	
	ItemIngredient getSecondInput();
	
	@Override
	default boolean allows(ItemStack... stacks) {
		var firstInputStack = stacks[0];
		var secondInputStack = stacks[1];
		
		var firstMatchesAnyInput = getFirstInput().test(firstInputStack, Long.MAX_VALUE) || getSecondInput().test(firstInputStack, Long.MAX_VALUE);
		var secondMatchesAnyInput = getFirstInput().test(secondInputStack, Long.MAX_VALUE) || getSecondInput().test(secondInputStack, Long.MAX_VALUE);
		
		if (firstInputStack.isEmpty()) {
			return secondMatchesAnyInput;
		}
		
		if (secondInputStack.isEmpty()) {
			return firstMatchesAnyInput;
		}
		
		return (getFirstInput().test(firstInputStack, Long.MAX_VALUE) && getSecondInput().test(secondInputStack, Long.MAX_VALUE))
				|| (getFirstInput().test(secondInputStack, Long.MAX_VALUE) && getSecondInput().test(firstInputStack, Long.MAX_VALUE));
	}
}
