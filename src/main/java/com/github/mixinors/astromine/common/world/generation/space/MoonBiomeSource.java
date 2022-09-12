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

package com.github.mixinors.astromine.common.world.generation.space;

import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public class MoonBiomeSource extends BiomeSource {
	public static final Codec<MoonBiomeSource> CODEC = RecordCodecBuilder.create((instance) ->
			instance.group(
					RegistryOps.createRegistryCodec(Registry.BIOME_KEY).forGetter((biomeSource) -> biomeSource.registry)
			).apply(instance, instance.stable(MoonBiomeSource::new)));
	
	private final Registry<Biome> registry;
	
	public MoonBiomeSource(Registry<Biome> registry) {
		super(ImmutableList.of(
				registry.getOrCreateEntry(AMBiomes.MOON_LIGHT_SIDE_KEY),
				registry.getOrCreateEntry(AMBiomes.MOON_DARK_SIDE_KEY),
				registry.getOrCreateEntry(AMBiomes.MOON_CRATER_FIELD_KEY)
		));
		
		this.registry = registry;
	}
	
	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}
	
	// TODO: Add 3D biomes and caves!
	// TODO: Add caves to the moon, and clamp their top and bottoms to not hit bedrock!
	@Override
	public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
		return registry.getIndexedEntries().get(0);
		// Roughly 50% of the moon's surface should be light side,
		// 30% crater fields, and 20% dark side.
		
		// The noise range is [0.0 .. 1.0], but I've seen it go negative.
		
		// TODO: Fix this. What the FUCK was Mojang thinking?
		
		//var sample = Math.abs(this.noise.sample(x / 128.0F, 0, z / 128.0F));
		//
		//if (sample > 0.5F) {
		//	return registry.getEntry(AMBiomes.MOON_LIGHT_SIDE_KEY).orElseThrow();
		//} else if (sample > 0.2F) {
		//	return registry.getEntry(AMBiomes.MOON_CRATER_FIELD_KEY).orElseThrow();
		//} else {
		//	return registry.getEntry(AMBiomes.MOON_DARK_SIDE_KEY).orElseThrow();
		//}
	}
}
