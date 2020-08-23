package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;

public abstract class ArmorCraftingRecipeGenerators extends ShapedCraftingRecipeGenerator {
	public ArmorCraftingRecipeGenerators(MaterialItemType input, MaterialItemType output, String... pattern) {
		super(input, output, pattern);
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

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return !set.usesSmithing() && super.shouldGenerate(set);
	}
}
