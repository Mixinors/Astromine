package com.github.chainmailstudios.astromine.client.patchouli;

import com.github.chainmailstudios.astromine.common.recipe.TrituratingRecipe;

public class TrituratingPage extends BasicEnergyConsumingPage<TrituratingRecipe> {
	public TrituratingPage() {
		super(TrituratingRecipe.Type.INSTANCE);
	}
}
