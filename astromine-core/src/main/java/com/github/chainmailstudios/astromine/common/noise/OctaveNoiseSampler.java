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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * A helper class used to generify the usage of octaves in noise generation. It clamps the values between amplitudeLow
 * and amplitudeHigh
 *
 * @param <T>
 *        The noise sampler that you are using. It must have a constructor with just a long parameter.
 *
 * @author Valoeghese and SuperCoder79
 */
public class OctaveNoiseSampler<T extends Noise> extends Noise {
	private final Noise[] samplers;
	private final double clamp;
	private final double frequency;
	private final double amplitudeLow;
	private final double amplitudeHigh;

	public OctaveNoiseSampler(Class<T> classT, Random rand, int octaves, double frequency, double amplitudeHigh, double amplitudeLow) {
		super(0);
		samplers = new Noise[octaves];
		clamp = 1D / (1D - (1D / Math.pow(2, octaves)));

		Constructor<T> constructor = this.getNoiseConstructor(classT);

		for (int i = 0; i < octaves; ++i) {
			samplers[i] = create(constructor, rand.nextLong());
		}

		this.frequency = frequency;
		this.amplitudeLow = amplitudeLow;
		this.amplitudeHigh = amplitudeHigh;
	}

	private Constructor<T> getNoiseConstructor(Class<T> clazz) {
		try {
			return clazz.getDeclaredConstructor(long.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Reflection hackery to make the Noise objects
	 */
	private T create(Constructor<T> constructor, long seed) {
		if (constructor == null) {
			return null;
		}

		try {
			return constructor.newInstance(seed);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double sample(double x, double y) {
		double amplFreq = 0.5D;
		double result = 0;
		for (Noise sampler : samplers) {
			result += (amplFreq * sampler.sample(x / (amplFreq * frequency), y / (amplFreq * frequency)));

			amplFreq *= 0.5D;
		}

		result = result * clamp;
		return result > 0 ? result * amplitudeHigh : result * amplitudeLow;
	}

	public double sample(double x, double y, double z) {
		double amplFreq = 0.5D;
		double result = 0;
		for (Noise sampler : samplers) {
			double freq = amplFreq * frequency;
			result += (amplFreq * sampler.sample(x / freq, y / freq, z / freq));

			amplFreq *= 0.5D;
		}

		result = result * clamp;
		return result > 0 ? result * amplitudeHigh : result * amplitudeLow;
	}

	public double sampleCustom(double x, double y, double freqModifier, double amplitudeHMod, double amplitudeLMod, int octaves) {
		double amplFreq = 0.5D;
		double result = 0;

		double sampleFreq = frequency * freqModifier;

		for (int i = 0; i < octaves; ++i) {
			Noise sampler = samplers[i];

			double freq = amplFreq * sampleFreq;
			result += (amplFreq * sampler.sample(x / freq, y / freq));

			amplFreq *= 0.5D;
		}

		double sampleClamp = 1D / (1D - (1D / Math.pow(2, octaves)));
		result *= sampleClamp;
		return result > 0 ? result * amplitudeHigh * amplitudeHMod : result * amplitudeLow * amplitudeLMod;
	}

	public double sampleCustom(double x, double y, double z, double freqModifier, double amplitudeHMod, double amplitudeLMod, int octaves) {
		double amplFreq = 0.5D;
		double result = 0;

		double sampleFreq = frequency * freqModifier;

		for (int i = 0; i < octaves; ++i) {
			Noise sampler = samplers[i];

			double freq = amplFreq * sampleFreq;
			result += (amplFreq * sampler.sample(x / freq, y / freq, z / freq));

			amplFreq *= 0.5D;
		}

		double sampleClamp = 1D / (1D - (1D / Math.pow(2, octaves)));
		result = result * sampleClamp;
		return result > 0 ? result * amplitudeHigh * amplitudeHMod : result * amplitudeLow * amplitudeLMod;
	}
}
