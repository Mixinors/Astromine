package com.github.chainmailstudios.astromine.common.material;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class AstromineToolMaterials {
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

		public int getDurability() {
			return this.itemDurability;
		}

		public float getMiningSpeedMultiplier() {
			return this.miningSpeed;
		}

		public float getAttackDamage() {
			return this.attackDamage;
		}

		public int getMiningLevel() {
			return this.miningLevel;
		}

		public int getEnchantability() {
			return this.enchantability;
		}

		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}
	}
}
