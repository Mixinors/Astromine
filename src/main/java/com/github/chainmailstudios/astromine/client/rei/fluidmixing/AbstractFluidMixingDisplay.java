package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AbstractFluidMixingDisplay implements RecipeDisplay {
	private final double energy;
	private final FluidVolume input;
	private final FluidVolume output;
	private final Identifier id;

	public AbstractFluidMixingDisplay(double energy, FluidVolume input, FluidVolume output, Identifier id) {
		this.energy = energy;
		this.input = input;
		this.output = output;
		this.id = id;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(
				Collections.singletonList(EntryStack.create(input.getFluid()))
		);
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return Collections.singletonList(EntryStack.create(output.getFluid()));
	}

	public double getEnergy() {
		return energy;
	}

	public FluidVolume getInput() {
		return input;
	}

	public FluidVolume getOutput() {
		return output;
	}
}
