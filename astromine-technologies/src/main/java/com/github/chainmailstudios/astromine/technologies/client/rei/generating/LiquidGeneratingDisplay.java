/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.technologies.client.rei.generating;

import com.github.chainmailstudios.astromine.technologies.client.rei.AstromineTechnologiesRoughlyEnoughItemsPlugin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

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
		return AstromineTechnologiesRoughlyEnoughItemsPlugin.LIQUID_GENERATING;
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
