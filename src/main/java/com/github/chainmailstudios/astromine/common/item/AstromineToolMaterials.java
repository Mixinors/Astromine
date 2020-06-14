package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class AstromineToolMaterials {
	public static final AstromineToolMaterial METITE = new AstromineToolMaterial(4, 2015, 8f, 5.0f, 15, () -> Ingredient.ofItems(AstromineItems.METITE_INGOT));
	public static final AstromineToolMaterial ASTERITE = new AstromineToolMaterial(5, 2643, 10f, 5.0f, 20, () -> Ingredient.ofItems(AstromineItems.ASTERITE_FRAGMENT));
	public static final AstromineToolMaterial STELLUM = new AstromineToolMaterial(2, 981, 14f, 6.0f, 5, () -> Ingredient.ofItems(AstromineItems.STELLUM_INGOT));
	public static final AstromineToolMaterial GALAXIUM = new AstromineToolMaterial(6, 3072, 11f, 5.0f, 18, () -> Ingredient.ofItems(AstromineItems.GALAXIUM_FRAGMENT));
	public static final AstromineToolMaterial UNIVITE = new AstromineToolMaterial(7, 3918, 12f, 6.0f, 22, () -> Ingredient.ofItems(AstromineItems.UNIVITE_INGOT));

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
