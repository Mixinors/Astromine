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

package com.github.mixinors.astromine.datagen.generator.recipe;

import me.shedaniel.architectury.hooks.TagHooks;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.datagen.generator.recipe.set.ShapedCraftingSetRecipeGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;

public abstract class ToolCraftingRecipeGenerators extends ShapedCraftingSetRecipeGenerator {
	public ToolCraftingRecipeGenerators(MaterialItemType input, MaterialItemType output, String... pattern) {
		super(input, output, pattern);
		this.addIngredient('s', Ingredient.fromTag(TagHooks.getItemOptional(new Identifier("c", "wood_sticks"))));
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
