package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.registry.AstromineToolMaterials;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class AstromineTechnologiesToolMaterials extends AstromineToolMaterials {
	public static final ToolMaterial BASIC_DRILL = register(2, Integer.MAX_VALUE, 10F, 2F, 16, () -> Ingredient.EMPTY);
	public static final ToolMaterial ADVANCED_DRILL = register(3, Integer.MAX_VALUE, 15F, 3F, 20, () -> Ingredient.EMPTY);
	public static final ToolMaterial ELITE_DRILL = register(5, Integer.MAX_VALUE, 20F, 5F, 16, () -> Ingredient.EMPTY);

	public static void initialize() {

	}
}
