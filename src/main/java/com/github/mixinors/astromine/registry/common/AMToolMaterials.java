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

package com.github.mixinors.astromine.registry.common;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class AMToolMaterials {
	public static final ToolMaterial BRONZE = register(2, 539, 7.0F, 2.5F, 18, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("bronze_ingots")));
	public static final ToolMaterial STEEL = register(3, 1043, 7.5F, 3.0F, 16, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("steel_ingots")));
	
	public static final ToolMaterial METITE = register(1, 853, 13.0F, 4.0F, 5, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("metite_ingots")));
	public static final ToolMaterial ASTERITE = register(5, 2015, 10.0F, 5.0F, 20, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("asterites")));
	public static final ToolMaterial STELLUM = register(5, 2643, 8.0F, 6.0F, 15, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("stellum_ingots")));
	public static final ToolMaterial GALAXIUM = register(6, 3072, 11.0F, 5.0F, 18, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("galaxiums")));
	public static final ToolMaterial UNIVITE = register(7, 3918, 12.0F, 6.0F, 22, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("univite_ingots")));
	public static final ToolMaterial LUNUM = register(5, 534, 7.0F, 4.5F, 8, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("lunum_ingots")));

	public static final ToolMaterial METEORIC_STEEL = register(3, 949, 10.5F, 3.5f, 10, () -> Ingredient.fromTag(AMTagKeys.createCommonItemTag("meteoric_steel_ingots")));
	
	public static final ToolMaterial PRIMITIVE_DRILL = register(2, Integer.MAX_VALUE, 5.0F, 1.5F, 12, () -> Ingredient.EMPTY);
	public static final ToolMaterial BASIC_DRILL = register(2, Integer.MAX_VALUE, 10.0F, 2.0F, 16, () -> Ingredient.EMPTY);
	public static final ToolMaterial ADVANCED_DRILL = register(3, Integer.MAX_VALUE, 15.0F, 3.0F, 20, () -> Ingredient.EMPTY);
	public static final ToolMaterial ELITE_DRILL = register(5, Integer.MAX_VALUE, 20.0F, 5.0F, 16, () -> Ingredient.EMPTY);
	
	public static void init() {
	}
	
	public static ToolMaterial register(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
		return new AstromineToolMaterial(miningLevel, itemDurability, miningSpeed, attackDamage, enchantability, repairIngredient);
	}
	
	public static class AstromineToolMaterial implements ToolMaterial {
		private final int miningLevel;
		private final int itemDurability;
		
		private final float miningSpeed;
		private final float attackDamage;
		
		private final int enchantability;
		
		private final Lazy<Ingredient> repairIngredient;
		
		AstromineToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			
			this.enchantability = enchantability;
			
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
