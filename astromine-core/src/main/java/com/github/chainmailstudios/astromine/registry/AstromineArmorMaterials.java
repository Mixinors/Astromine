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

package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class AstromineArmorMaterials {
	public static final AstromineArmorMaterial COPPER = new AstromineArmorMaterial("copper", 12, new int[]{ 1, 4, 5, 2 }, 14, AstromineSoundEvents.COPPER_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.ofItems(AstromineItems.COPPER_INGOT));
	public static final AstromineArmorMaterial TIN = new AstromineArmorMaterial("tin", 12, new int[]{ 1, 5, 4, 2 }, 14, AstromineSoundEvents.TIN_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.ofItems(AstromineItems.TIN_INGOT));
	public static final AstromineArmorMaterial SILVER = new AstromineArmorMaterial("silver", 17, new int[]{ 2, 5, 5, 2 }, 22, AstromineSoundEvents.SILVER_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.ofItems(AstromineItems.SILVER_INGOT));
	public static final AstromineArmorMaterial LEAD = new AstromineArmorMaterial("lead", 18, new int[]{ 3, 5, 7, 2 }, 7, AstromineSoundEvents.LEAD_ARMOR_EQUIPPED, 0.1F, 0.0F, () -> Ingredient.ofItems(AstromineItems.LEAD_INGOT));

	public static final AstromineArmorMaterial BRONZE = new AstromineArmorMaterial("bronze", 20, new int[]{ 2, 5, 6, 2 }, 16, AstromineSoundEvents.BRONZE_ARMOR_EQUIPPED, 0.7f, 0.0f, () -> Ingredient.ofItems(AstromineItems.BRONZE_INGOT));
	public static final AstromineArmorMaterial STEEL = new AstromineArmorMaterial("steel", 24, new int[]{ 3, 5, 7, 2 }, 12, AstromineSoundEvents.STEEL_ARMOR_EQUIPPED, 0.6f, 0.0f, () -> Ingredient.ofItems(AstromineItems.STEEL_INGOT));
	public static final AstromineArmorMaterial ELECTRUM = new AstromineArmorMaterial("electrum", 13, new int[]{ 1, 4, 5, 2 }, 25, AstromineSoundEvents.ELECTRUM_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.ofItems(AstromineItems.ELECTRUM_INGOT));
	public static final AstromineArmorMaterial ROSE_GOLD = new AstromineArmorMaterial("rose_gold", 9, new int[]{ 1, 3, 5, 2 }, 25, AstromineSoundEvents.ROSE_GOLD_ARMOR_EQUIPPED, 0.1F, 0.0F, () -> Ingredient.ofItems(AstromineItems.ROSE_GOLD_INGOT));
	public static final AstromineArmorMaterial STERLING_SILVER = new AstromineArmorMaterial("sterling_silver", 18, new int[]{ 2, 5, 6, 2 }, 23, AstromineSoundEvents.STERLING_SILVER_ARMOR_EQUIPPED, 0.1F, 0.0F, () -> Ingredient.ofItems(AstromineItems.STERLING_SILVER_INGOT));
	public static final AstromineArmorMaterial FOOLS_GOLD = new AstromineArmorMaterial("fools_gold", 15, new int[]{ 2, 5, 6, 2 }, 10, AstromineSoundEvents.FOOLS_GOLD_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.ofItems(AstromineItems.FOOLS_GOLD_INGOT));

	public static final AstromineArmorMaterial METITE = new AstromineArmorMaterial("metite", 15, new int[]{ 2, 4, 6, 2 }, 7, AstromineSoundEvents.METITE_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.ofItems(AstromineItems.METITE_INGOT));
	public static final AstromineArmorMaterial ASTERITE = new AstromineArmorMaterial("asterite", 35, new int[]{ 4, 7, 8, 4 }, 20, AstromineSoundEvents.ASTERITE_ARMOR_EQUIPPED, 4.0f, 0.1f, () -> Ingredient.ofItems(AstromineItems.ASTERITE));
	public static final AstromineArmorMaterial STELLUM = new AstromineArmorMaterial("stellum", 41, new int[]{ 3, 5, 6, 2 }, 15, AstromineSoundEvents.STELLUM_ARMOR_EQUIPPED, 6.0f, 0.2f, () -> Ingredient.ofItems(AstromineItems.STELLUM_INGOT));
	public static final AstromineArmorMaterial GALAXIUM = new AstromineArmorMaterial("galaxium", 44, new int[]{ 4, 8, 9, 4 }, 18, AstromineSoundEvents.GALAXIUM_ARMOR_EQUIPPED, 4.5f, 0.1f, () -> Ingredient.ofItems(AstromineItems.GALAXIUM));
	public static final AstromineArmorMaterial UNIVITE = new AstromineArmorMaterial("univite", 47, new int[]{ 5, 8, 9, 5 }, 22, AstromineSoundEvents.UNIVITE_ARMOR_EQUIPPED, 5.0f, 0.1f, () -> Ingredient.ofItems(AstromineItems.UNIVITE_INGOT));

	public static final AstromineArmorMaterial SPACE_SUIT = new AstromineArmorMaterial("space_suit", 50, new int[]{ 1, 2, 3, 1 }, 2, AstromineSoundEvents.SPACE_SUIT_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.ofItems(AstromineItems.METITE_INGOT));

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
