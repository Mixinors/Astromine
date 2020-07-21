package com.github.chainmailstudios.astromine.client.patchouli;

import com.github.chainmailstudios.astromine.common.recipe.PressingRecipe;

public class PressingPage extends BasicEnergyConsumingPage<PressingRecipe> {
	public PressingPage() {
		super(PressingRecipe.Type.INSTANCE);
	}
}
