package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.ShapedCraftingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

public abstract class ToolCraftingRecipeGenerators extends ShapedCraftingSetRecipeGenerator {
	public ToolCraftingRecipeGenerators(MaterialItemType input, MaterialItemType output, String... pattern) {
		super(input, output, pattern);
		this.addIngredient('s', Ingredient.fromTag(TagRegistry.item(new Identifier("c", "wood_sticks"))));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return !set.usesSmithing() && super.shouldGenerate(set);
	}

	public static class PickaxeCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public PickaxeCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.PICKAXE, "###", " s ", " s ");
		}
	}

	public static class AxeCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public AxeCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.AXE, "##", "#s", " s");
		}
	}

	public static class ShovelCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public ShovelCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.SHOVEL, "#", "s", "s");
		}
	}

	public static class SwordCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public SwordCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.SWORD, "#", "#", "s");
		}
	}

	public static class HoeCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public HoeCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.HOE, "##", " s", " s");
		}
	}

	public static class MattockCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public MattockCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.MATTOCK, "###", "#s ", " s ");
		}
	}

	public static class MiningToolCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public MiningToolCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.MINING_TOOL, "###", " # ", " s ");
		}
	}

	public static class HammerCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public HammerCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.HAMMER, "###", "#s#", " s ");
		}
	}

	public static class ExcavatorCraftingRecipeGenerator extends ToolCraftingRecipeGenerators {
		public ExcavatorCraftingRecipeGenerator(MaterialItemType input) {
			super(input, MaterialItemType.EXCAVATOR, " # ", "#s#", " s ");
		}
	}
}
