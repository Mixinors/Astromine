package com.github.chainmailstudios.astromine.client.rei.generating;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.recipe.SolidGeneratingRecipe;
import me.shedaniel.rei.api.EntryStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class SolidGeneratingDisplay extends AbstractEnergyGeneratingDisplay {
	private final List<EntryStack> stacks;
	private final Identifier id;
	private final Double time;

	public SolidGeneratingDisplay(SolidGeneratingRecipe recipe) {
		super(recipe.getEnergyGenerated());
		this.stacks = EntryStack.ofIngredient(recipe.getInput());
		for (EntryStack stack : stacks) {
			stack.setAmount(recipe.getAmount());
		}
		this.id = recipe.getId();
		this.time = null;
	}

	public SolidGeneratingDisplay(double energyGenerated, List<EntryStack> stacks, Identifier id, Double time) {
		super(energyGenerated);
		this.stacks = stacks;
		this.id = id;
		this.time = time;
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(stacks);
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.SOLID_GENERATING;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(this.id);
	}

	public Double getTime() {
		return time;
	}
}
