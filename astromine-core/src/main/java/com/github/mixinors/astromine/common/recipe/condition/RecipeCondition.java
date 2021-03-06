package com.github.mixinors.astromine.common.recipe.condition;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RecipeCondition {
	private final String modId;
	private final String itemId;
	
	public RecipeCondition(String modId, String itemId) {
		this.modId = modId;
		this.itemId = itemId;
	}
	
	public boolean isAllowed() {
		if (!modId.isEmpty()) {
			return Platform.isModLoaded(modId);
		}
		
		if (!itemId.isEmpty()) {
			return Registry.ITEM.getIds().contains(new Identifier(itemId));
		}
		
		return false;
	}
}
