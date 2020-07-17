package com.github.chainmailstudios.astromine.client.rei.pressing;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.recipe.PressingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.TrituratingRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class PressingDisplay implements RecipeDisplay {
	private final List<List<EntryStack>> inputs;
	private final List<EntryStack> outputs;
	private final int timeRequired;
	private final double energyRequired;
	private final Identifier recipeId;

	public PressingDisplay(PressingRecipe recipe) {
		this(
				EntryStack.ofIngredients(recipe.getPreviewInputs()),
				Collections.singletonList(EntryStack.create(recipe.getOutput())),
				recipe.getTime(),
				recipe.getEnergyConsumed(),
				recipe.getId()
		);
	}

	public PressingDisplay(List<List<EntryStack>> inputs, List<EntryStack> outputs, int timeRequired, double energyRequired, Identifier recipeId) {
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

	public double getEnergyRequired() {
		return energyRequired;
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.PRESSING;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(this.recipeId);
	}
}
