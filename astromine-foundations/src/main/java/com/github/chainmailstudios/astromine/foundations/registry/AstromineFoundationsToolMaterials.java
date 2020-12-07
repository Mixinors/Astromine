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

package com.github.chainmailstudios.astromine.foundations.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.registry.AstromineToolMaterials;

public class AstromineFoundationsToolMaterials extends AstromineToolMaterials {
	public static final ToolMaterial COPPER = register(1, 200, 4f, 1.5f, 10, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:copper_ingots"))));
	public static final ToolMaterial TIN = register(1, 200, 5f, 1.0f, 10, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:tin_ingots"))));
	public static final ToolMaterial LEAD = register(2, 496, 4.5f, 1.5f, 5, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:lead_ingots"))));
	public static final ToolMaterial BRONZE = register(2, 539, 7f, 2.5f, 18, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:bronze_ingots"))));
	public static final ToolMaterial STEEL = register(3, 1043, 7.5f, 3f, 16, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:steel_ingots"))));
}
