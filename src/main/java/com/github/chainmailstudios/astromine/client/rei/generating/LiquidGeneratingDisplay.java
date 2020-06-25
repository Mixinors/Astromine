package com.github.chainmailstudios.astromine.client.rei.generating;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.recipe.LiquidGeneratingRecipe;
import me.shedaniel.rei.api.EntryStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class LiquidGeneratingDisplay extends AbstractEnergyGeneratingDisplay {
	private final Fluid fluid;
	private final Fraction amount;

	public LiquidGeneratingDisplay(LiquidGeneratingRecipe recipe) {
		super(recipe.getEnergyGenerated());
		this.fluid = recipe.getFluid();
		this.amount = recipe.getAmount();
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(Collections.singletonList(EntryStack.create(fluid, amount.floatValue())));
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
}
