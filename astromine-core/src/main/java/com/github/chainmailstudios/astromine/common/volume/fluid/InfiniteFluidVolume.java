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

package com.github.chainmailstudios.astromine.common.volume.fluid;

import net.minecraft.fluid.Fluid;

import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;

/**
 * A variation of {@link FluidVolume}, with infinite
 * {@link #getAmount()} and {@link #getSize()}.
 */
public final class InfiniteFluidVolume extends FluidVolume {
	/** Instantiates an {@link InfiniteFluidVolume}s. */
	private InfiniteFluidVolume(Fluid fluid) {
		super(Fraction.of(Long.MAX_VALUE), Fraction.of(Long.MAX_VALUE), fluid);
	}

	/** Instantiates an {@link InfiniteFluidVolume}s. */
	public static InfiniteFluidVolume of(Fluid fluid) {
		return new InfiniteFluidVolume(fluid);
	}

	/** Always return {@link Fraction#MAX_VALUE}. */
	@Override
	public Fraction getAmount() {
		return Fraction.of(Long.MAX_VALUE);
	}

	/** Always return {@link Fraction#MAX_VALUE}. */
	@Override
	public Fraction getSize() {
		return Fraction.of(Long.MAX_VALUE);
	}

	/** Returns a copy of this volume. */
	@Override
	public <V extends Volume<Fraction>> V copy() {
		return (V) of(getFluid());
	}
}
