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

package com.github.chainmailstudios.astromine.discoveries.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineBiomes;

public class AstromineDiscoveriesBiomes extends AstromineBiomes {
	public static final Identifier ASTEROID_BELT_ID = AstromineCommon.identifier("asteroid_belt");
	public static final RegistryKey<Biome> ASTEROID_BELT = register(Registry.BIOME_KEY, ASTEROID_BELT_ID);

	public static final Identifier VULCAN_PLAINS_ID = AstromineCommon.identifier("vulcan_plains");
	public static final RegistryKey<Biome> VULCAN_PLAINS = register(Registry.BIOME_KEY, VULCAN_PLAINS_ID);

	public static final Identifier LUNAR_PLAINS_ID = AstromineCommon.identifier("lunar_plains");
	public static final RegistryKey<Biome> LUNAR_PLAINS = register(Registry.BIOME_KEY, LUNAR_PLAINS_ID);
	public static final Identifier LUNAR_HILLS_ID = AstromineCommon.identifier("lunar_hills");
	public static final RegistryKey<Biome> LUNAR_HILLS = register(Registry.BIOME_KEY, LUNAR_HILLS_ID);
	public static final Identifier LUNAR_LOWLANDS_ID = AstromineCommon.identifier("lunar_lowlands");
	public static final RegistryKey<Biome> LUNAR_LOWLANDS = register(Registry.BIOME_KEY, LUNAR_LOWLANDS_ID);

	public static final Identifier MARTIAN_PLAINS_ID = AstromineCommon.identifier("martian_plains");
	public static final RegistryKey<Biome> MARTIAN_PLAINS = register(Registry.BIOME_KEY, MARTIAN_PLAINS_ID);
	public static final Identifier MARTIAN_RIVERBED_ID = AstromineCommon.identifier("martian_riverbed");
	public static final RegistryKey<Biome> MARTIAN_RIVERBED = register(Registry.BIOME_KEY, MARTIAN_RIVERBED_ID);

	public static final Identifier GLACIOS_ID = AstromineCommon.identifier("glacios");
	public static final RegistryKey<Biome> GLACIOS = register(Registry.BIOME_KEY, GLACIOS_ID);

	public static void initialize() {

	}
}
