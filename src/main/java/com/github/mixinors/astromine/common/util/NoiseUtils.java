package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.common.noise.OctaveNoiseSampler;
import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NoiseUtils {
	private static final Map<Long, OctaveNoiseSampler<?>> SAMPLERS = new ConcurrentHashMap<>();
	
	public static OctaveNoiseSampler<?> getSampler(long seed, int octaves, double frequency, double amplitudeHigh, double amplitudeLow) {
		return SAMPLERS.computeIfAbsent(seed, s -> new OctaveNoiseSampler<>(OpenSimplexNoise.class, new java.util.Random(s), octaves, frequency, amplitudeHigh, amplitudeLow));
	}
}
