/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;

/**
 * Comparator behavior.
 */
@FunctionalInterface
public interface ComparatorMode {
	ComparatorMode NONE = new ComparatorMode() {
		/** Override behavior to have no output. */
		@Override
		public int getOutput(@Nullable BlockEntity entity) {
			return 0;
		}

		/** Override behavior to have no output. */
		@Override
		public boolean hasOutput() {
			return false;
		}
	};
	
	ComparatorMode ITEMS = ComparatorOutput::forItems;
	ComparatorMode FLUIDS = ComparatorOutput::forFluids;
	ComparatorMode ENERGY = ComparatorOutput::forEnergy;

	/** Returns the output level for the given {@link BlockEntity}. */
	int getOutput(@Nullable BlockEntity entity);

	/** Asserts whether this mode has an output or not. */
	default boolean hasOutput() {
		return true;
	}
}