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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class AMWorlds {
	public static final ResourceKey<Level> ROCKET_INTERIORS = createWorldKey(AMCommon.id("rocket_interiors"));
	public static final ResourceKey<DimensionType> ROCKET_INTERIORS_DIMENSION_TYPE_KEY = createTypeKey(AMCommon.id("rocket_interiors"));
	public static final ResourceKey<LevelStem> ROCKET_INTERIORS_DIMENSION_OPTIONS_KEY = createOptionsKey(AMCommon.id("rocket_interiors"));
	
	public static final ResourceKey<Level> MOON = createWorldKey(AMCommon.id("moon"));
	public static final ResourceKey<DimensionType> MOON_DIMENSION_TYPE_KEY = createTypeKey(AMCommon.id("moon"));
	public static final ResourceKey<LevelStem> MOON_DIMENSION_OPTIONS_KEY = createOptionsKey(AMCommon.id("moon"));
	
	public static final ResourceKey<Level> MOON_ORBIT = createWorldKey(AMCommon.id("moon_orbit"));
	public static final ResourceKey<DimensionType> MOON_ORBIT_DIMENSION_TYPE_KEY = createTypeKey(AMCommon.id("moon_orbit"));
	public static final ResourceKey<LevelStem> MOON_ORBIT_DIMENSION_OPTIONS_KEY = createOptionsKey(AMCommon.id("moon_orbit"));
	
	public static final ResourceKey<Level> EARTH_ORBIT = createWorldKey(AMCommon.id("earth_orbit"));
	public static final ResourceKey<DimensionType> EARTH_ORBIT_DIMENSION_TYPE_KEY = createTypeKey(AMCommon.id("earth_orbit"));
	public static final ResourceKey<LevelStem> EARTH_ORBIT_DIMENSION_OPTIONS_KEY = createOptionsKey(AMCommon.id("earth_orbit"));
	
	public static void init() {
	
	}
	
	public static boolean isVacuum(Holder<DimensionType> dimensionType) {
		return dimensionType.is(AMTagKeys.DimensionTypeTags.IS_VACUUM);
	}
	
	public static boolean isAstromine(ResourceKey<Level> key) {
		return key.equals(ROCKET_INTERIORS) || key.equals(MOON) || key.equals(MOON_ORBIT) || key.equals(EARTH_ORBIT);
	}
	
	public static ResourceKey<LevelStem> createOptionsKey(ResourceLocation id) {
		return ResourceKey.create(Registries.LEVEL_STEM, id);
	}
	
	private static ResourceKey<DimensionType> createTypeKey(ResourceLocation id) {
		return ResourceKey.create(Registries.DIMENSION_TYPE, id);
	}
	
	public static ResourceKey<Level> createWorldKey(ResourceLocation id) {
		return ResourceKey.create(Registries.DIMENSION, id);
	}
}
