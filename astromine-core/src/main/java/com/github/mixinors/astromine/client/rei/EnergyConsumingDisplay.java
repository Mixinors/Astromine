package com.github.mixinors.astromine.client.rei;

import java.util.List;
import java.util.Optional;

import net.minecraft.util.Identifier;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;

public abstract class EnergyConsumingDisplay implements Display {
	private final List<EntryIngredient> inputs;
	private final List<EntryIngredient> outputs;
	private final int timeRequired;
	private final double energyRequired;
	private final Identifier recipeId;

	public EnergyConsumingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int timeRequired, double energyRequired, Identifier recipeId) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.timeRequired = timeRequired;
		this.energyRequired = energyRequired;
		this.recipeId = recipeId;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return inputs;
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return outputs;
	}

	public int getTimeRequired() {
		return timeRequired;
	}

	public double getEnergyRequired() {
		return energyRequired;
	}

	@Override
	public Optional<Identifier> getDisplayLocation() {
		return Optional.ofNullable(this.recipeId);
	}
}
