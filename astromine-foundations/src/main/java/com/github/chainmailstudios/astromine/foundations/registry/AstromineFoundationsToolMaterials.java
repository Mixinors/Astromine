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
	public static final ToolMaterial SILVER = register(2, 462, 6.5f, 2.0f, 20, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:silver_ingots"))));
	public static final ToolMaterial LEAD = register(2, 496, 4.5f, 1.5f, 5, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:lead_ingots"))));

	public static final ToolMaterial BRONZE = register(2, 539, 7f, 2.5f, 18, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:bronze_ingots"))));
	public static final ToolMaterial STEEL = register(3, 1043, 7.5f, 3f, 16, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:steel_ingots"))));
	public static final ToolMaterial ELECTRUM = register(2, 185, 11f, 1.0f, 21, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:electrum_ingots"))));
	public static final ToolMaterial ROSE_GOLD = register(1, 64, 10.0F, 0.5F, 24, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:rose_gold_ingots"))));
	public static final ToolMaterial STERLING_SILVER = register(2, 697, 7f, 2.5f, 20, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:sterling_silver_ingots"))));
	public static final ToolMaterial FOOLS_GOLD = register(2, 250, 6.5F, 2.0F, 16, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:fools_gold_ingots"))));

	public static final ToolMaterial METITE = register(1, 853, 13f, 4.0f, 5, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:metite_ingots"))));
	public static final ToolMaterial ASTERITE = register(5, 2015, 10f, 5.0f, 20, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:asterites"))));
	public static final ToolMaterial STELLUM = register(5, 2643, 8f, 6.0f, 15, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:stellum_ingots"))));
	public static final ToolMaterial GALAXIUM = register(6, 3072, 11f, 5.0f, 18, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:galaxiums"))));
	public static final ToolMaterial UNIVITE = register(7, 3918, 12f, 6.0f, 22, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:univite_ingots"))));
	public static final ToolMaterial LUNUM = register(4, 1382, 7f, 4.5f, 18, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:lunum_ingots"))));

	public static final ToolMaterial METEORIC_STEEL = register(3, 949, 10.5f, 3.5f, 10, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:meteoric_steel_ingots"))));
}
