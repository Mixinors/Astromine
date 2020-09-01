package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.ShapedCraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

public abstract class ArmorCraftingRecipeGenerators extends ShapedCraftingSetRecipeGenerator {
	public ArmorCraftingRecipeGenerators(MaterialItemType input, MaterialItemType output, String... pattern) {
		super(input, output, pattern);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return !set.usesSmithing() && super.shouldGenerate(set);
	}

	public static class HelmetCraftingRecipeGenerator extends ArmorCraftingRecipeGenerators {
		public HelmetCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.HELMET, "###", "# #");
		}
	}

	public static class ChestplateCraftingRecipeGenerator extends ArmorCraftingRecipeGenerators {
		public ChestplateCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.CHESTPLATE, "# #", "###", "###");
		}
	}

	public static class LeggingsCraftingRecipeGenerator extends ArmorCraftingRecipeGenerators {
		public LeggingsCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.LEGGINGS, "###", "# #", "# #");
		}
	}

	public static class BootsCraftingRecipeGenerator extends ArmorCraftingRecipeGenerators {
		public BootsCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.BOOTS, "# #", "# #");
		}
	}
}
