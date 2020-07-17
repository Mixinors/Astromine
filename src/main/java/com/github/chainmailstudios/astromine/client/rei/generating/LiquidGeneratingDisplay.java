package com.github.chainmailstudios.astromine.client.rei.generating;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.LiquidGeneratingRecipe;
import me.shedaniel.rei.api.EntryStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class LiquidGeneratingDisplay extends AbstractEnergyGeneratingDisplay {
	private final Fluid fluid;
	private final Fraction amount;
	private final Identifier id;

	public LiquidGeneratingDisplay(LiquidGeneratingRecipe recipe) {
		super(recipe.getEnergyGenerated());
		this.fluid = recipe.getFluid();
		this.amount = recipe.getAmount();
		this.id = recipe.getId();
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(Collections.singletonList(EntryStack.create(fluid, amount.floatValue())));
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.LIQUID_GENERATING;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public Fraction getAmount() {
		return amount;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(this.id);
	}
}
