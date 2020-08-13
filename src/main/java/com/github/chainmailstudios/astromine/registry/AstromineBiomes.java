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

package com.github.chainmailstudios.astromine.registry;

/*
 * import com.github.chainmailstudios.astromine.AstromineCommon;
 * import com.github.chainmailstudios.astromine.common.world.biome.EarthSpaceBiome;
 * import com.github.chainmailstudios.astromine.common.world.biome.MarsBiome;
 * import com.github.chainmailstudios.astromine.common.world.biome.MoonBiome;
 * import com.github.chainmailstudios.astromine.common.world.biome.VulcanBiome;
 * import com.github.chainmailstudios.astromine.common.world.generation.mars.MarsBiomeSource;
 * import com.github.chainmailstudios.astromine.common.world.generation.moon.MoonBiomeSource;
 * import com.github.chainmailstudios.astromine.common.world.generation.space.EarthSpaceBiomeSource;
 * import com.github.chainmailstudios.astromine.common.world.generation.vulcan.VulcanBiomeSource;
 *
 * import net.minecraft.util.registry.BuiltinRegistries;
 * import net.minecraft.util.registry.Registry;
 * import net.minecraft.world.biome.Biome;
 *
 * public class AstromineBiomes {
 * public static Biome ASTEROID_BELT;
 * public static Biome MOON_FLATS;
 * public static Biome MOON_HILLS;
 * public static Biome MOON_LOWLANDS;
 * public static Biome MARS;
 * public static Biome MARS_RIVERBED;
 * public static Biome VULCAN;
 *
 * public static void initialize() {
 * ASTEROID_BELT = Registry.register(BuiltinRegistries.BIOME, AstromineCommon.identifier("asteroid_belt"), new EarthSpaceBiome());
 *
 * MOON_FLATS = Registry.register(BuiltinRegistries.BIOME, AstromineCommon.identifier("moon_flats"), new MoonBiome(100, 20));
 * MOON_HILLS = Registry.register(BuiltinRegistries.BIOME, AstromineCommon.identifier("moon_hills"), new MoonBiome(105, 30));
 * MOON_LOWLANDS = Registry.register(BuiltinRegistries.BIOME, AstromineCommon.identifier("moon_lowlands"), new MoonBiome(93, 9));
 *
 * MARS = Registry.register(BuiltinRegistries.BIOME, AstromineCommon.identifier("mars"), new MarsBiome(100, 1));
 * MARS_RIVERBED = Registry.register(BuiltinRegistries.BIOME, AstromineCommon.identifier("mars_riverbed"), new MarsBiome(60, 0.1f));
 *
 * VULCAN = Registry.register(BuiltinRegistries.BIOME, AstromineCommon.identifier("vulcan"), new VulcanBiome(100, 1));
 * }
 * }
 */

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.HashSet;
import java.util.Set;

public class AstromineBiomes {
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();

	public static final Identifier ASTEROID_BELT_ID = AstromineCommon.identifier("asteroid_belt");
	public static final RegistryKey<Biome> ASTEROID_BELT = register(Registry.BIOME_KEY, ASTEROID_BELT_ID);

	public static final Identifier VULCAN_PLAINS_ID = AstromineCommon.identifier("vulcan_plains");
	public static final RegistryKey<Biome> VULCAN_PLAINS = register(Registry.BIOME_KEY, VULCAN_PLAINS_ID);

	public static final Identifier MOON_FLATS_ID = AstromineCommon.identifier("moon_flats");
	public static final RegistryKey<Biome> MOON_FLATS = register(Registry.BIOME_KEY, MOON_FLATS_ID);
	public static final Identifier MOON_HILLS_ID = AstromineCommon.identifier("moon_hills");
	public static final RegistryKey<Biome> MOON_HILLS = register(Registry.BIOME_KEY, MOON_HILLS_ID);
	public static final Identifier MOON_LOWLANDS_ID = AstromineCommon.identifier("moon_lowlands");
	public static final RegistryKey<Biome> MOON_LOWLANDS = register(Registry.BIOME_KEY, MOON_LOWLANDS_ID);
	public static final Identifier MARS_ID = AstromineCommon.identifier("mars");
	public static final RegistryKey<Biome> MARS = register(Registry.BIOME_KEY, MARS_ID);
	public static final Identifier MARS_RIVERBED_ID = AstromineCommon.identifier("mars_riverbed");
	public static final RegistryKey<Biome> MARS_RIVERBED = register(Registry.BIOME_KEY, MARS_RIVERBED_ID);

	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		RegistryKey<T> key = RegistryKey.of(registry, identifier);
		KEYS.add(key);
		return key;
	}

	public static boolean isAstromine(RegistryKey<?> key) {
		return KEYS.contains(key);
	}

	public static void initialize() {
		// Unused.
	}
}
