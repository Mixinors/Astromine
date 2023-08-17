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
import com.github.mixinors.astromine.datagen.provider.AMBiomeProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.stream.Stream;

public class MoonBiomeSource extends BiomeSource {
	private static final ThreadLocal<OpenSimplexNoise> SIMPLEX = ThreadLocal.withInitial(() -> null);
	
	public static final Codec<MoonBiomeSource> CODEC = RecordCodecBuilder.create((instance) ->
			instance.group(
					RegistryOps.getEntryCodec(AMBiomeProvider.MOON_LIGHT_SIDE_KEY),
					RegistryOps.getEntryCodec(AMBiomeProvider.MOON_DARK_SIDE_KEY),
					RegistryOps.getEntryCodec(AMBiomeProvider.MOON_CRATER_FIELD_KEY)
			).apply(instance, instance.stable(MoonBiomeSource::new)));
	
	private final RegistryEntry<Biome> lightSideBiome;
	private final RegistryEntry<Biome> darkSideBiome;
	private final RegistryEntry<Biome> craterSideBiome;
	
	public MoonBiomeSource(RegistryEntry<Biome> lightSideBiome, RegistryEntry<Biome> darkSideBiome, RegistryEntry<Biome> craterSideBiome) {
		this.lightSideBiome = lightSideBiome;
		this.darkSideBiome = darkSideBiome;
		this.craterSideBiome = craterSideBiome;
	}
	
	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}
	
	@Override
	protected Stream<RegistryEntry<Biome>> biomeStream() {
		return Stream.of(lightSideBiome, darkSideBiome, craterSideBiome);
	}
	
	// TODO: Add 3D biomes and caves!
	// TODO: Add caves to the moon, and clamp their top and bottoms to not hit bedrock!
	@Override
	public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
		// Roughly 50% of the moon's surface should be light side,
		// 30% crater fields, and 20% dark side.
		
		// The noise range is [0.0 .. 1.0], but I've seen it go negative.
		
		// TODO: Fix this. What the FUCK was Mojang thinking?
		
		var simplex = SIMPLEX.get();
		
		if (simplex == null) {
			var server = InstanceUtil.getServer();
			if (server == null) return darkSideBiome;
			
			var world = server.getWorld(World.OVERWORLD);
			if (world == null) return darkSideBiome;
			
			simplex = new OpenSimplexNoise(world.getSeed());
			
			SIMPLEX.set(simplex);
		}
		
		var sample = simplex.sample(x / 512.0F, z / 512.0F);
		
		if (sample > 0.5F) {
			return lightSideBiome;
		} else if (sample > 0.2F) {
			return craterSideBiome;
		} else {
			return darkSideBiome;
		}
	}
}
