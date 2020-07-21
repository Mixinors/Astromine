package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ElectrolyzingDisplay implements RecipeDisplay {
	private final double energy;
	private final FluidVolume input;
	private final FluidVolume firstOutput;
	private final FluidVolume secondOutput;
	private final Identifier id;

	public ElectrolyzingDisplay(double energy, FluidVolume input, FluidVolume firstOutput, FluidVolume secondOutput, Identifier id) {
		this.energy = energy;
		this.input = input;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.id = id;
	}

	public ElectrolyzingDisplay(ElectrolyzingRecipe recipe) {
		this(
				recipe.getEnergyConsumed(),
				new FluidVolume(recipe.getInputFluid(), recipe.getInputAmount()),
				new FluidVolume(recipe.getFirstOutputFluid(), recipe.getFirstOutputAmount()),
				new FluidVolume(recipe.getSecondOutputFluid(), recipe.getSecondOutputAmount()),
				recipe.getId()
		);
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
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.ELECTROLYZING;
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return Arrays.asList(EntryStack.create(firstOutput.getFluid()), EntryStack.create(secondOutput.getFluid()));
	}

	public double getEnergy() {
		return energy;
	}

	public FluidVolume getInput() {
		return input;
	}

	public FluidVolume getFirstOutput() {
		return firstOutput;
	}

	public FluidVolume getSecondOutput() {
		return secondOutput;
	}
}
