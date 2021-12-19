/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.block.redstone;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ComparatorBlockEntity;
import net.minecraft.screen.ScreenHandler;

import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.ToLongFunction;

/**
 * A handler of {@link ComparatorBlockEntity}
 * output levels.
 */
public class ComparatorOutput {
	/**
	 * Returns the output level for a {@link BlockEntity} with an {@link SimpleItemStorage}.
	 */
	public static int forItems(@Nullable BlockEntity entity) {
		return ScreenHandler.calculateComparatorOutput(entity);
	}

	/**
	 * Returns the output level for a {@link BlockEntity} with an {@link EnergyStore}.
	 */
	public static int forEnergy(@Nullable BlockEntity entity) {
		if (entity == null) {
			return 0;
		}

		EnergyStore component = EnergyStore.get(entity);

		if (component == null) {
			return 0;
		}

		if (component.getAmount() <= 0.0001) {
			return 0;
		}

		return 1 + (int) (component.getAmount() / component.getSize() * 14.0);
	}

	/**
	 * Returns the output level for a {@link BlockEntity} with a {@link SimpleFluidStorage}.
	 */
	public static int forFluids(@Nullable BlockEntity entity) {
		if (entity == null) {
			return 0;
		}

		SimpleFluidStorage fluidStorage = SimpleFluidStorage.get(entity);

		if (fluidStorage == null) {
			return 0;
		}

		Collection<FluidVolume> contents = fluidStorage.getContents().values();
		long amounts = sumBy(contents, FluidVolume::getAmount);

		if (amounts == 0) {
			return 0;
		}

		long sizes = sumBy(contents, FluidVolume::getSize);
		return 1 + (int) (amounts * 14.0f / sizes);
	}

	/**
	 * Sums collection of {@link T} into a {@link long} by the given {@link Function}.
	 */
	private static <T> long sumBy(Collection<T> ts, ToLongFunction<? super T> extractor) {
		long result = 0;

		for (T t : ts) {
			result += extractor.applyAsLong(t);
		}

		return result;
	}
}
