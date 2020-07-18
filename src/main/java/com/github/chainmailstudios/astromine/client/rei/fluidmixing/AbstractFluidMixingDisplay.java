package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AbstractFluidMixingDisplay implements RecipeDisplay {
	private final double energy;
	private final FluidVolume firstInput;
	private final FluidVolume secondInput;
	private final FluidVolume output;
	private final Identifier id;

	public AbstractFluidMixingDisplay(double energy, FluidVolume firstInput, FluidVolume secondInput, FluidVolume output, Identifier id) {
		this.energy = energy;
		this.firstInput = firstInput;
		this.secondInput = secondInput;
		this.output = output;
		this.id = id;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Arrays.asList(
				Collections.singletonList(EntryStack.create(firstInput.getFluid())),
				Collections.singletonList(EntryStack.create(secondInput.getFluid()))
		);
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return Collections.singletonList(EntryStack.create(output.getFluid()));
	}

	public double getEnergy() {
		return energy;
	}

	public FluidVolume getFirstInput() {
		return firstInput;
	}

	public FluidVolume getSecondInput() {
		return secondInput;
	}

	public FluidVolume getOutput() {
		return output;
	}
}
