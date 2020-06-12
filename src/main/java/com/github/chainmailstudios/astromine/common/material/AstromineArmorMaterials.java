package com.github.chainmailstudios.astromine.common.material;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Lazy;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineSoundEvents;

import java.util.function.Supplier;

public class AstromineArmorMaterials {
    public static final AstromineArmorMaterial ASTERITE = new AstromineArmorMaterial("asterite", 41, new int[]{4, 7, 9, 4}, 20, AstromineSoundEvents.ASTERITE_ARMOR_EQUIPPED, 4.0f, 0.1f, () -> Ingredient.ofItems(AstromineItems.ASTERITE_FRAGMENT));
    public static final AstromineArmorMaterial STELLUM = new AstromineArmorMaterial("stellum", 22, new int[]{2, 5, 7, 2}, 7, AstromineSoundEvents.STELLUM_ARMOR_EQUIPPED, 6.0f, 0.2f, () -> Ingredient.ofItems(AstromineItems.STELLUM_INGOT));
    public static final AstromineArmorMaterial UNIVITE = new AstromineArmorMaterial("univite", 47, new int[]{5, 8, 9, 5}, 22, AstromineSoundEvents.UNIVITE_ARMOR_EQUIPPED, 5.0f, 0.1f, () -> Ingredient.EMPTY);
    public static final AstromineArmorMaterial SPACE_SUIT = new AstromineArmorMaterial("space_suit", 50, new int[]{1, 2, 3, 1}, 2, AstromineSoundEvents.SPACE_SUIT_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.ofItems(AstromineItems.METITE_CLUSTER));

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

        public int getDurability(EquipmentSlot slot) {
            return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
        }

        public int getProtectionAmount(EquipmentSlot slot) {
            return this.protectionAmounts[slot.getEntitySlotId()];
        }

        public int getEnchantability() {
            return this.enchantability;
        }

        public SoundEvent getEquipSound() {
            return this.equipSound;
        }

        public Ingredient getRepairIngredient() {
            return (Ingredient)this.repairIngredientSupplier.get();
        }

        @Environment(EnvType.CLIENT)
        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }

        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }
}
