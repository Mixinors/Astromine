package com.github.chainmailstudios.astromine.client.rei.triturating;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.TrituratingRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class TrituratingDisplay implements RecipeDisplay {
	private final List<List<EntryStack>> inputs;
	private final List<EntryStack> outputs;
	private final int timeRequired;
	private final Fraction energyRequired;
	private final Identifier recipeId;

	public TrituratingDisplay(TrituratingRecipe recipe) {
		this(
				CollectionUtils.map(recipe.getPreviewInputs(), ingredient -> CollectionUtils.map(ingredient.getMatchingStacksClient(), EntryStack::create)),
				Collections.singletonList(EntryStack.create(recipe.getOutput())),
				recipe.getTime(),
				recipe.getEnergyConsumed().copy(),
				recipe.getId()
		);
	}

	public TrituratingDisplay(List<List<EntryStack>> inputs, List<EntryStack> outputs, int timeRequired, Fraction energyRequired, Identifier recipeId) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.timeRequired = timeRequired;
		this.energyRequired = energyRequired;
		this.recipeId = recipeId;
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return inputs;
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return outputs;
	}

	public int getTimeRequired() {
		return timeRequired;
	}

	public Fraction getEnergyRequired() {
		return energyRequired;
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.TRITURATING;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(this.recipeId);
	}
}
