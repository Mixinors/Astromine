package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class AstromineArmorMaterials {
	public static final AstromineArmorMaterial METITE = new AstromineArmorMaterial("metite", 15, new int[]{2, 4, 6, 2}, 15, AstromineSounds.ASTERITE_ARMOR_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.ofItems(AstromineItems.METITE_INGOT));
	public static final AstromineArmorMaterial ASTERITE = new AstromineArmorMaterial("asterite", 41, new int[]{4, 7, 8, 4}, 20, AstromineSounds.ASTERITE_ARMOR_EQUIPPED, 4.0f, 0.1f, () -> Ingredient.ofItems(AstromineItems.ASTERITE_FRAGMENT));
	public static final AstromineArmorMaterial STELLUM = new AstromineArmorMaterial("stellum", 22, new int[]{2, 4, 6, 2}, 7, AstromineSounds.STELLUM_ARMOR_EQUIPPED, 6.0f, 0.2f, () -> Ingredient.ofItems(AstromineItems.STELLUM_INGOT));
	public static final AstromineArmorMaterial GALAXIUM = new AstromineArmorMaterial("galaxium", 44, new int[]{4, 8, 9, 4}, 18, AstromineSounds.GALAXIUM_ARMOR_EQUIPPED, 4.5f, 0.1f, () -> Ingredient.ofItems(AstromineItems.GALAXIUM_FRAGMENT));
	public static final AstromineArmorMaterial UNIVITE = new AstromineArmorMaterial("univite", 47, new int[]{5, 8, 9, 5}, 22, AstromineSounds.UNIVITE_ARMOR_EQUIPPED, 5.0f, 0.1f, () -> Ingredient.ofItems(AstromineItems.UNIVITE_INGOT));
	public static final AstromineArmorMaterial SPACE_SUIT = new AstromineArmorMaterial("space_suit", 50, new int[]{1, 2, 3, 1}, 2, AstromineSounds.SPACE_SUIT_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.ofItems(AstromineItems.METITE_INGOT));

	public static class AstromineArmorMaterial implements ArmorMaterial {
		private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
		private final String name;
		private final int durabilityMultiplier;
		private final int[] protectionAmounts;
		private final int enchantability;
		private final SoundEvent equipSound;
		private final float toughness;
		private final float knockbackResistance;
		private final Lazy<Ingredient> repairIngredientSupplier;

		private AstromineArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> supplier) {
			this.name = name;
			this.durabilityMultiplier = durabilityMultiplier;
			this.protectionAmounts = protectionAmounts;
			this.enchantability = enchantability;
			this.equipSound = equipSound;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairIngredientSupplier = new Lazy(supplier);
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
