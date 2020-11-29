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
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class AstromineDiscoveriesDimensions extends AstromineDimensions {
	public static final ResourceLocation EARTH_SPACE_ID = AstromineCommon.identifier("earth_space");
	public static final ResourceKey<LevelStem> EARTH_SPACE_OPTIONS = register(Registry.LEVEL_STEM_REGISTRY, EARTH_SPACE_ID);
	public static final ResourceKey<DimensionType> EARTH_SPACE_TYPE_KEY = register(Registry.DIMENSION_TYPE_REGISTRY, EARTH_SPACE_ID);
	public static final ResourceKey<Level> EARTH_SPACE_WORLD = register(Registry.DIMENSION_REGISTRY, EARTH_SPACE_ID);

	public static final ResourceLocation MOON_ID = AstromineCommon.identifier("moon");
	public static final ResourceKey<LevelStem> MOON_OPTIONS = register(Registry.LEVEL_STEM_REGISTRY, MOON_ID);
	public static final ResourceKey<DimensionType> MOON_TYPE_KEY = register(Registry.DIMENSION_TYPE_REGISTRY, MOON_ID);
	public static final ResourceKey<Level> MOON_WORLD = register(Registry.DIMENSION_REGISTRY, MOON_ID);

	public static final ResourceLocation MARS_ID = AstromineCommon.identifier("mars");
	public static final ResourceKey<LevelStem> MARS_OPTIONS = register(Registry.LEVEL_STEM_REGISTRY, MARS_ID);
	public static final ResourceKey<DimensionType> MARS_TYPE_KEY = register(Registry.DIMENSION_TYPE_REGISTRY, MARS_ID);
	public static final ResourceKey<Level> MARS_WORLD = register(Registry.DIMENSION_REGISTRY, MARS_ID);

	public static final ResourceLocation VULCAN_ID = AstromineCommon.identifier("vulcan");
	public static final ResourceKey<LevelStem> VULCAN_OPTIONS = register(Registry.LEVEL_STEM_REGISTRY, VULCAN_ID);
	public static final ResourceKey<DimensionType> VULCAN_TYPE_KEY = register(Registry.DIMENSION_TYPE_REGISTRY, VULCAN_ID);
	public static final ResourceKey<Level> VULCAN_WORLD = register(Registry.DIMENSION_REGISTRY, VULCAN_ID);

	public static final ResourceLocation GLACIOS_ID = AstromineCommon.identifier("glacios");
	public static final ResourceKey<LevelStem> GLACIOS_OPTIONS = register(Registry.LEVEL_STEM_REGISTRY, GLACIOS_ID);
	public static final ResourceKey<DimensionType> GLACIOS_TYPE_KEY = register(Registry.DIMENSION_TYPE_REGISTRY, GLACIOS_ID);
	public static final ResourceKey<Level> GLACIOS_WORLD = register(Registry.DIMENSION_REGISTRY, GLACIOS_ID);

	public static void initialize() {

	}
}
