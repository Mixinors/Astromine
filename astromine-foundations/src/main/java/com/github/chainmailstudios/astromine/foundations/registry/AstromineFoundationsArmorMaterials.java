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

import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.registry.AstromineArmorMaterials;

public class AstromineFoundationsArmorMaterials extends AstromineArmorMaterials {
	public static final ArmorMaterial COPPER = register("copper", 12, new int[]{ 1, 4, 5, 2 }, 14, AstromineFoundationsSoundEvents.COPPER_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:copper_ingots"))));
	public static final ArmorMaterial TIN = register("tin", 12, new int[]{ 1, 5, 4, 2 }, 14, AstromineFoundationsSoundEvents.TIN_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:tin_ingots"))));
	public static final ArmorMaterial SILVER = register("silver", 17, new int[]{ 2, 5, 5, 2 }, 22, AstromineFoundationsSoundEvents.SILVER_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:silver_ingots"))));
	public static final ArmorMaterial LEAD = register("lead", 18, new int[]{ 3, 5, 7, 2 }, 7, AstromineFoundationsSoundEvents.LEAD_ARMOR_EQUIPPED, 0.1F, 0.0F, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:lead_ingots"))));

	public static final ArmorMaterial BRONZE = register("bronze", 20, new int[]{ 2, 5, 6, 2 }, 16, AstromineFoundationsSoundEvents.BRONZE_ARMOR_EQUIPPED, 0.7f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:bronze_ingots"))));
	public static final ArmorMaterial STEEL = register("steel", 24, new int[]{ 3, 5, 7, 2 }, 12, AstromineFoundationsSoundEvents.STEEL_ARMOR_EQUIPPED, 0.6f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:steel_ingots"))));
	public static final ArmorMaterial ELECTRUM = register("electrum", 13, new int[]{ 1, 4, 5, 2 }, 25, AstromineFoundationsSoundEvents.ELECTRUM_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:electrum_ingots"))));
	public static final ArmorMaterial ROSE_GOLD = register("rose_gold", 9, new int[]{ 1, 3, 5, 2 }, 25, AstromineFoundationsSoundEvents.ROSE_GOLD_ARMOR_EQUIPPED, 0.1F, 0.0F, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:rose_gold_ingots"))));
	public static final ArmorMaterial STERLING_SILVER = register("sterling_silver", 18, new int[]{ 2, 5, 6, 2 }, 23, AstromineFoundationsSoundEvents.STERLING_SILVER_ARMOR_EQUIPPED, 0.1F, 0.0F, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse(
		"c:sterling_silver_ingots"))));
	public static final ArmorMaterial FOOLS_GOLD = register("fools_gold", 15, new int[]{ 2, 5, 6, 2 }, 10, AstromineFoundationsSoundEvents.FOOLS_GOLD_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:fools_gold_ingots"))));

	public static final ArmorMaterial METITE = register("metite", 15, new int[]{ 2, 4, 6, 2 }, 7, AstromineFoundationsSoundEvents.METITE_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:metite_ingots"))));
	public static final ArmorMaterial ASTERITE = register("asterite", 35, new int[]{ 4, 7, 8, 4 }, 20, AstromineFoundationsSoundEvents.ASTERITE_ARMOR_EQUIPPED, 4.0f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:asterites"))));
	public static final ArmorMaterial STELLUM = register("stellum", 41, new int[]{ 3, 5, 6, 2 }, 15, AstromineFoundationsSoundEvents.STELLUM_ARMOR_EQUIPPED, 6.0f, 0.2f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:stellum_ingots"))));
	public static final ArmorMaterial GALAXIUM = register("galaxium", 44, new int[]{ 4, 8, 9, 4 }, 18, AstromineFoundationsSoundEvents.GALAXIUM_ARMOR_EQUIPPED, 4.5f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:galaxiums"))));
	public static final ArmorMaterial UNIVITE = register("univite", 47, new int[]{ 5, 8, 9, 5 }, 22, AstromineFoundationsSoundEvents.UNIVITE_ARMOR_EQUIPPED, 5.0f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:univite_ingots"))));
	public static final ArmorMaterial LUNUM = register("lunum", 30, new int[]{ 4, 7, 8, 4 }, 18, AstromineFoundationsSoundEvents.LUNUM_ARMOR_EQUIPPED, 1.0f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:lunum_ingots"))));

	public static final ArmorMaterial METEORIC_STEEL = register("meteoric_steel", 20, new int[]{ 3, 5, 7, 2 }, 10, AstromineFoundationsSoundEvents.METEORIC_STEEL_ARMOR_EQUIPPED, 0.4f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse(
		"c:meteoric_steel_ingots"))));
}
