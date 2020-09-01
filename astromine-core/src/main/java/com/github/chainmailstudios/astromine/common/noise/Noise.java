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

package com.github.chainmailstudios.astromine.common.noise;

/**
 * A class that can create 2d and 3d noise. You *MUST* have at least one constructor in your implementation that takes
 * in only a seed, otherwise {@link OctaveNoiseSampler} won't work in conjunction with it.
 *
 * @author SuperCoder79
 */
public abstract class Noise {
	private static final int CHAR_BIT = 8;
	private static final int SIZE_INT = 8;

	public Noise(long seed) {}

	// Helper noise functions =======================

	/**
	 * A bit hack that computes a rough estimation of a square root for the given number Don't use this in places that
	 * need accuracy-- You'll regret it!
	 *
	 * @param d
	 *        the input for the fastSqrt algorithm
	 *
	 * @return a **rough** approximation of square root
	 */
	public static double fastSqrt(double d) {
		return Double.longBitsToDouble(((Double.doubleToLongBits(d) - (1L << 52)) >> 1) + (1L << 61));
	}

	/**
	 * A fast implementation of Math.abs() that avoids branching
	 *
	 * @param n
	 *        the number to get the absolute value of
	 *
	 * @return the absolute value
	 */
	public static int fastAbs(int n) {
		int mask = n >> (SIZE_INT * CHAR_BIT - 1);
		return ((n + mask) ^ mask);
	}

	public static long fastAbs(long n) {
		long mask = n >> (SIZE_INT * CHAR_BIT - 1);
		return ((n + mask) ^ mask);
	}

	protected static int factorial(int n) {
		if (n == 1) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}
	}

	protected static double lerp(double progress, double start, double end) {
		return start + progress * (end - start);
	}

	protected static double sigmoid(double x) {
		return (1 / (1 + Math.exp(-x)));
	}

	public abstract double sample(double x, double z);

	public abstract double sample(double x, double y, double z);

	// ensures that the returned value is in [-1, 1]
	protected double clamp(double value) {
		return (value > 1) ? 1 : (value < -1) ? -1 : value;
	}

	// ensures that the returned value is in [0, 1]
	protected double clampPositive(double value) {
		return (value < 0) ? 0 : value;
	}
}
