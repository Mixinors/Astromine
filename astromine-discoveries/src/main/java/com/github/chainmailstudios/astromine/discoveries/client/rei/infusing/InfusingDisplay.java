package com.github.chainmailstudios.astromine.discoveries.client.rei.infusing;

import com.github.chainmailstudios.astromine.discoveries.client.rei.AstromineDiscoveriesRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.discoveries.common.recipe.AltarRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InfusingDisplay implements RecipeDisplay {
	private List<List<EntryStack>> inputs;
	private List<List<EntryStack>> outputs;
	private Identifier recipeId;

	public InfusingDisplay(AltarRecipe recipe) {
		this(
			EntryStack.ofIngredients(recipe.getPreviewInputs()),
			Collections.singletonList(Collections.singletonList(EntryStack.create(recipe.getOutput().copy()))),
			recipe.getId()
		);
	}

	public InfusingDisplay(List<List<EntryStack>> inputs, List<List<EntryStack>> outputs, Identifier recipeId) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.recipeId = recipeId;
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return inputs;
	}

	@Override
	public List<List<EntryStack>> getResultingEntries() {
		return outputs;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(recipeId);
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineDiscoveriesRoughlyEnoughItemsPlugin.INFUSING;
	}
}
