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

package com.github.mixinors.astromine.common.util.constant.fluid;

import net.neoforged.neoforge.fluids.FluidType;

public class ExtraFluidConstants {
	public static final long BLOCK = FluidType.BUCKET_VOLUME;
	public static final long INGOT = BLOCK / 9L;
	public static final long NUGGET = Math.max(1L, INGOT / 9L);
	public static final long INGOT_FROM_2x2_BLOCK = BLOCK / 4L;
	public static final long NUGGET_FROM_2x2_BLOCK = Math.max(1L, INGOT_FROM_2x2_BLOCK / 9L);
	
	public static long ingot(boolean block2x2) {
		return ingots(1, block2x2);
	}
	
	public static long nugget(boolean block2x2) {
		return nuggets(1, block2x2);
	}
	
	public static long ingots(int count, boolean block2x2) {
		return (block2x2 ? INGOT_FROM_2x2_BLOCK : INGOT) * count;
	}
	
	public static long nuggets(int count, boolean block2x2) {
		return (block2x2 ? NUGGET_FROM_2x2_BLOCK : NUGGET) * count;
	}
}
