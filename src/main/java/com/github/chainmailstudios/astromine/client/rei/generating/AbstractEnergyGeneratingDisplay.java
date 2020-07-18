package com.github.chainmailstudios.astromine.client.rei.generating;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class AbstractEnergyGeneratingDisplay implements RecipeDisplay {
	private final double energyGenerated;

	public AbstractEnergyGeneratingDisplay(double energyGenerated) {
		this.energyGenerated = energyGenerated;
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return Collections.emptyList();
	}

	public double getEnergyGeneratedPerTick() {
		return energyGenerated;
	}
}
