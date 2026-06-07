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

import java.util.function.Supplier;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class AMToolMaterials {
	public static final Tier BRONZE = register(2, 539, 7.0F, 2.5F, 18, () -> Ingredient.of(AMTagKeys.createCommonItemTag("bronze_ingots")));
	public static final Tier STEEL = register(3, 1043, 7.5F, 3.0F, 16, () -> Ingredient.of(AMTagKeys.createCommonItemTag("steel_ingots")));
	
	public static final Tier METITE = register(1, 853, 13.0F, 4.0F, 5, () -> Ingredient.of(AMTagKeys.createCommonItemTag("metite_ingots")));
	public static final Tier ASTERITE = register(5, 2015, 10.0F, 5.0F, 20, () -> Ingredient.of(AMTagKeys.createCommonItemTag("asterites")));
	public static final Tier STELLUM = register(5, 2643, 8.0F, 6.0F, 15, () -> Ingredient.of(AMTagKeys.createCommonItemTag("stellum_ingots")));
	public static final Tier GALAXIUM = register(6, 3072, 11.0F, 5.0F, 18, () -> Ingredient.of(AMTagKeys.createCommonItemTag("galaxiums")));
	public static final Tier UNIVITE = register(7, 3918, 12.0F, 6.0F, 22, () -> Ingredient.of(AMTagKeys.createCommonItemTag("univite_ingots")));
	public static final Tier LUNUM = register(5, 534, 7.0F, 4.5F, 8, () -> Ingredient.of(AMTagKeys.createCommonItemTag("lunum_ingots")));

	public static final Tier METEORIC_STEEL = register(3, 949, 10.5F, 3.5f, 10, () -> Ingredient.of(AMTagKeys.createCommonItemTag("meteoric_steel_ingots")));
	
	public static final Tier PRIMITIVE_DRILL = register(2, Integer.MAX_VALUE, 5.0F, 1.5F, 12, () -> Ingredient.EMPTY);
	public static final Tier BASIC_DRILL = register(2, Integer.MAX_VALUE, 10.0F, 2.0F, 16, () -> Ingredient.EMPTY);
	public static final Tier ADVANCED_DRILL = register(3, Integer.MAX_VALUE, 15.0F, 3.0F, 20, () -> Ingredient.EMPTY);
	public static final Tier ELITE_DRILL = register(5, Integer.MAX_VALUE, 20.0F, 5.0F, 16, () -> Ingredient.EMPTY);
	
	public static void init() {
	}
	
	public static Tier register(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
		return new AstromineToolMaterial(miningLevel, itemDurability, miningSpeed, attackDamage, enchantability, repairIngredient);
	}
	
	public static class AstromineToolMaterial implements Tier {
		private final int miningLevel;
		private final int itemDurability;
		
		private final float miningSpeed;
		private final float attackDamage;
		
		private final int enchantability;
		
		private final LazyLoadedValue<Ingredient> repairIngredient;
		
		AstromineToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			
			this.enchantability = enchantability;
			
			this.repairIngredient = new LazyLoadedValue(repairIngredient);
		}
		
		@Override
		public int getUses() {
			return this.itemDurability;
		}
		
		@Override
		public float getSpeed() {
			return this.miningSpeed;
		}
		
		@Override
		public float getAttackDamageBonus() {
			return this.attackDamage;
		}
		
		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			if (this.miningLevel <= 0) {
				return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
			}
			
			if (this.miningLevel == 1) {
				return BlockTags.INCORRECT_FOR_STONE_TOOL;
			}
			
			if (this.miningLevel == 2) {
				return BlockTags.INCORRECT_FOR_IRON_TOOL;
			}
			
			if (this.miningLevel == 3) {
				return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
			}
			
			return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
		}
		
		@Override
		public int getEnchantmentValue() {
			return this.enchantability;
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}
	}
}
