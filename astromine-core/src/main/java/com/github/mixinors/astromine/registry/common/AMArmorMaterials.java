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

package com.github.mixinors.astromine.registry.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class AMArmorMaterials {
	public static final ArmorMaterial SPACE_SUIT = register("space_suit", 50, new int[]{ 1, 2, 3, 1 }, 2, AMSoundEvents.SPACE_SUIT_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:metite_ingots"))));
	
	public static final ArmorMaterial BRONZE = register("bronze", 20, new int[]{ 2, 5, 6, 2 }, 16, AMSoundEvents.BRONZE_ARMOR_EQUIPPED, 0.7f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:bronze_ingots"))));
	public static final ArmorMaterial STEEL = register("steel", 24, new int[]{ 3, 5, 7, 2 }, 12, AMSoundEvents.STEEL_ARMOR_EQUIPPED, 0.6f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:steel_ingots"))));
	public static final ArmorMaterial FOOLS_GOLD = register("fools_gold", 15, new int[]{ 2, 5, 6, 2 }, 10, AMSoundEvents.FOOLS_GOLD_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:fools_gold_ingots"))));
	
	public static final ArmorMaterial METITE = register("metite", 15, new int[]{ 2, 4, 6, 2 }, 7, AMSoundEvents.METITE_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:metite_ingots"))));
	public static final ArmorMaterial ASTERITE = register("asterite", 35, new int[]{ 4, 7, 8, 4 }, 20, AMSoundEvents.ASTERITE_ARMOR_EQUIPPED, 4.0f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:asterites"))));
	public static final ArmorMaterial STELLUM = register("stellum", 41, new int[]{ 3, 5, 6, 2 }, 15, AMSoundEvents.STELLUM_ARMOR_EQUIPPED, 6.0f, 0.2f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:stellum_ingots"))));
	public static final ArmorMaterial GALAXIUM = register("galaxium", 44, new int[]{ 4, 8, 9, 4 }, 18, AMSoundEvents.GALAXIUM_ARMOR_EQUIPPED, 4.5f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:galaxiums"))));
	public static final ArmorMaterial UNIVITE = register("univite", 47, new int[]{ 5, 8, 9, 5 }, 22, AMSoundEvents.UNIVITE_ARMOR_EQUIPPED, 5.0f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:univite_ingots"))));
	
	// public static final ArmorMaterial LUNUM = register("lunum", 30, new int[]{ 4, 7, 8, 4 }, 18, AMSoundEvents.LUNUM_ARMOR_EQUIPPED, 1.0f, 0.1f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:lunum_ingots"))));
	
	public static final ArmorMaterial METEORIC_STEEL = register("meteoric_steel", 20, new int[]{ 3, 5, 7, 2 }, 10, AMSoundEvents.METEORIC_STEEL_ARMOR_EQUIPPED, 0.4f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:meteoric_steel_ingots"))));
	
	public static ArmorMaterial register(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> supplier) {
		return new AstromineArmorMaterial(name, durabilityMultiplier, protectionAmounts, enchantability, equipSound, toughness, knockbackResistance, supplier);
	}

	public static class AstromineArmorMaterial implements ArmorMaterial {
		private static final int[] BASE_DURABILITY = new int[]{ 13, 15, 16, 11 };
		private final String name;
		private final int durabilityMultiplier;
		private final int[] protectionAmounts;
		private final int enchantability;
		private final SoundEvent equipSound;
		private final float toughness;
		private final float knockbackResistance;
		private final Supplier<Ingredient> repairIngredientSupplier;

		private AstromineArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> supplier) {
			this.name = name;
			this.durabilityMultiplier = durabilityMultiplier;
			this.protectionAmounts = protectionAmounts;
			this.enchantability = enchantability;
			this.equipSound = equipSound;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairIngredientSupplier = supplier;
		}

		@Override
		public int getDurability(EquipmentSlot slot) {
			return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
		}

		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return this.protectionAmounts[slot.getEntitySlotId()];
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public SoundEvent getEquipSound() {
			return this.equipSound;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return this.repairIngredientSupplier.get();
		}

		@Override
		@Environment(EnvType.CLIENT)
		public String getName() {
			return this.name;
		}

		@Override
		public float getToughness() {
			return this.toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return this.knockbackResistance;
		}
	}
}
