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

package com.github.mixinors.astromine.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class AstromineToolMaterials {
	public static final ToolMaterial BRONZE = register(2, 539, 7f, 2.5f, 18, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:bronze_ingots"))));
	public static final ToolMaterial STEEL = register(3, 1043, 7.5f, 3f, 16, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:steel_ingots"))));
	
	public static final ToolMaterial METITE = register(1, 853, 13f, 4.0f, 5, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:metite_ingots"))));
	public static final ToolMaterial ASTERITE = register(5, 2015, 10f, 5.0f, 20, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:asterites"))));
	public static final ToolMaterial STELLUM = register(5, 2643, 8f, 6.0f, 15, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:stellum_ingots"))));
	public static final ToolMaterial GALAXIUM = register(6, 3072, 11f, 5.0f, 18, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:galaxiums"))));
	public static final ToolMaterial UNIVITE = register(7, 3918, 12f, 6.0f, 22, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:univite_ingots"))));
	public static final ToolMaterial LUNUM = register(4, 1382, 7f, 4.5f, 18, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:lunum_ingots"))));
	
	public static final ToolMaterial METEORIC_STEEL = register(3, 949, 10.5f, 3.5f, 10, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:meteoric_steel_ingots"))));
	
	public static final ToolMaterial PRIMITIVE_DRILL = register(2, Integer.MAX_VALUE, 5F, 1.5F, 12, () -> Ingredient.EMPTY);
	public static final ToolMaterial BASIC_DRILL = register(2, Integer.MAX_VALUE, 10F, 2F, 16, () -> Ingredient.EMPTY);
	public static final ToolMaterial ADVANCED_DRILL = register(3, Integer.MAX_VALUE, 15F, 3F, 20, () -> Ingredient.EMPTY);
	public static final ToolMaterial ELITE_DRILL = register(5, Integer.MAX_VALUE, 20F, 5F, 16, () -> Ingredient.EMPTY);
	
	public static void initialize() {
	
	}
	
	public static ToolMaterial register(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantibility, Supplier<Ingredient> repairIngredient) {
		return new AstromineToolMaterial(miningLevel, itemDurability, miningSpeed, attackDamage, enchantibility, repairIngredient);
	}

	public static class AstromineToolMaterial implements ToolMaterial {
		private final int miningLevel;
		private final int itemDurability;
		private final float miningSpeed;
		private final float attackDamage;
		private final int enchantability;
		private final Lazy<Ingredient> repairIngredient;

		AstromineToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantibility, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			this.enchantability = enchantibility;
			this.repairIngredient = new Lazy(repairIngredient);
		}

		@Override
		public int getDurability() {
			return this.itemDurability;
		}

		@Override
		public float getMiningSpeedMultiplier() {
			return this.miningSpeed;
		}

		@Override
		public float getAttackDamage() {
			return this.attackDamage;
		}

		@Override
		public int getMiningLevel() {
			return this.miningLevel;
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}
	}
}
