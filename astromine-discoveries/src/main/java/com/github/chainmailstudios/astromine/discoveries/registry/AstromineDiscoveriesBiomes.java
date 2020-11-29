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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class AstromineDiscoveriesBiomes extends AstromineBiomes {
	public static final ResourceLocation ASTEROID_BELT_ID = AstromineCommon.identifier("asteroid_belt");
	public static final ResourceKey<Biome> ASTEROID_BELT = register(Registry.BIOME_REGISTRY, ASTEROID_BELT_ID);

	public static final ResourceLocation VULCAN_PLAINS_ID = AstromineCommon.identifier("vulcan_plains");
	public static final ResourceKey<Biome> VULCAN_PLAINS = register(Registry.BIOME_REGISTRY, VULCAN_PLAINS_ID);

	public static final ResourceLocation LUNAR_PLAINS_ID = AstromineCommon.identifier("lunar_plains");
	public static final ResourceKey<Biome> LUNAR_PLAINS = register(Registry.BIOME_REGISTRY, LUNAR_PLAINS_ID);
	public static final ResourceLocation LUNAR_HILLS_ID = AstromineCommon.identifier("lunar_hills");
	public static final ResourceKey<Biome> LUNAR_HILLS = register(Registry.BIOME_REGISTRY, LUNAR_HILLS_ID);
	public static final ResourceLocation LUNAR_LOWLANDS_ID = AstromineCommon.identifier("lunar_lowlands");
	public static final ResourceKey<Biome> LUNAR_LOWLANDS = register(Registry.BIOME_REGISTRY, LUNAR_LOWLANDS_ID);

	public static final ResourceLocation MARTIAN_PLAINS_ID = AstromineCommon.identifier("martian_plains");
	public static final ResourceKey<Biome> MARTIAN_PLAINS = register(Registry.BIOME_REGISTRY, MARTIAN_PLAINS_ID);
	public static final ResourceLocation MARTIAN_RIVERBED_ID = AstromineCommon.identifier("martian_riverbed");
	public static final ResourceKey<Biome> MARTIAN_RIVERBED = register(Registry.BIOME_REGISTRY, MARTIAN_RIVERBED_ID);

	public static final ResourceLocation GLACIOS_ID = AstromineCommon.identifier("glacios");
	public static final ResourceKey<Biome> GLACIOS = register(Registry.BIOME_REGISTRY, GLACIOS_ID);

	public static void initialize() {

	}
}
