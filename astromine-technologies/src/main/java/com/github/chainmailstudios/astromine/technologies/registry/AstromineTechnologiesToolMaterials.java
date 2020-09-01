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

package com.github.chainmailstudios.astromine.technologies.registry;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.registry.AstromineToolMaterials;

public class AstromineTechnologiesToolMaterials extends AstromineToolMaterials {
	public static final ToolMaterial BASIC_DRILL = register(2, Integer.MAX_VALUE, 10F, 2F, 16, () -> Ingredient.EMPTY);
	public static final ToolMaterial ADVANCED_DRILL = register(3, Integer.MAX_VALUE, 15F, 3F, 20, () -> Ingredient.EMPTY);
	public static final ToolMaterial ELITE_DRILL = register(5, Integer.MAX_VALUE, 20F, 5F, 16, () -> Ingredient.EMPTY);

	public static void initialize() {

	}
}
