package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsSoundEvents;
import com.github.chainmailstudios.astromine.registry.AstromineArmorMaterials;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class AstromineTechnologiesArmorMaterials extends AstromineArmorMaterials {
	public static final ArmorMaterial SPACE_SUIT = register("space_suit", 50, new int[]{ 1, 2, 3, 1 }, 2, AstromineFoundationsSoundEvents.SPACE_SUIT_EQUIPPED, 0.0f, 0.0f, () -> Ingredient.fromTag(TagRegistry.item(Identifier.tryParse("c:metite_ingots"))));

	public static void initialize() {

	}
}
