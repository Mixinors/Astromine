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

package com.github.chainmailstudios.astromine.common.block.redstone;

import net.minecraft.block.entity.BlockEntity;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import java.util.Collection;
import java.util.function.Function;

public class ComparatorOutput {
	public static int forEnergy(@Nullable BlockEntity entity) {
		if (entity == null) {
			return 0;
		}

		EnergyHandler handler = Energy.of(entity);

		if (handler.getEnergy() <= 0.0001) {
			return 0;
		}

		return 1 + (int) (handler.getEnergy() / handler.getMaxStored() * 14.0);
	}

	public static int forFluids(@Nullable BlockEntity entity) {
		if (entity == null) {
			return 0;
		}

		FluidComponent fluidComponent = FluidComponent.get(entity);

		if (fluidComponent == null) {
			return 0;
		}

		Collection<FluidVolume> contents = fluidComponent.getContents().values();
		Fraction amounts = sumBy(contents, FluidVolume::getAmount);

		if (amounts.getNumerator() == 0) {
			return 0;
		}

		Fraction sizes = sumBy(contents, FluidVolume::getSize);
		Fraction ratio = amounts.divide(sizes);
		return 1 + (int) (ratio.floatValue() * 14.0f);
	}

	private static <T> Fraction sumBy(Collection<T> ts, Function<? super T, Fraction> extractor) {
		Fraction result = Fraction.EMPTY;

		for (T t : ts) {
			result = result.add(extractor.apply(t));
		}

		return result;
	}
}
