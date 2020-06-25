package com.github.chainmailstudios.astromine.client.rei.generating;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class AbstractEnergyGeneratingDisplay implements RecipeDisplay {
	private final Fraction energyGenerated;

	public AbstractEnergyGeneratingDisplay(Fraction energyGenerated) {
		this.energyGenerated = energyGenerated;
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return Collections.emptyList();
	}

	public Fraction getEnergyGeneratedPerTick() {
		return energyGenerated;
	}
}
