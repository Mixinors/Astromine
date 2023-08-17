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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class AMWorlds {
	public static final RegistryKey<World> ROCKET_INTERIORS = createWorldKey(AMCommon.id("rocket_interiors"));
	public static final RegistryKey<DimensionType> ROCKET_INTERIORS_DIMENSION_TYPE_KEY = createTypeKey(AMCommon.id("rocket_interiors"));
	public static final RegistryKey<DimensionOptions> ROCKET_INTERIORS_DIMENSION_OPTIONS_KEY = createOptionsKey(AMCommon.id("rocket_interiors"));
	
	public static final RegistryKey<World> MOON = createWorldKey(AMCommon.id("moon"));
	public static final RegistryKey<DimensionType> MOON_DIMENSION_TYPE_KEY = createTypeKey(AMCommon.id("moon"));
	public static final RegistryKey<DimensionOptions> MOON_DIMENSION_OPTIONS_KEY = createOptionsKey(AMCommon.id("moon"));
	
	public static final RegistryKey<World> EARTH_ORBIT = createWorldKey(AMCommon.id("earth_orbit"));
	public static final RegistryKey<DimensionType> EARTH_ORBIT_DIMENSION_TYPE_KEY = createTypeKey(AMCommon.id("earth_orbit"));
	public static final RegistryKey<DimensionOptions> EARTH_ORBIT_DIMENSION_OPTIONS_KEY = createOptionsKey(AMCommon.id("earth_orbit"));
	
	public static void init() {
	
	}
	
	public static boolean isVacuum(RegistryEntry<DimensionType> dimensionType) {
		return dimensionType.isIn(AMTagKeys.DimensionTypeTags.IS_VACUUM);
	}
	
	public static boolean isAstromine(RegistryKey<World> key) {
		return key.equals(ROCKET_INTERIORS) || key.equals(MOON) || key.equals(EARTH_ORBIT);
	}
	
	public static RegistryKey<DimensionOptions> createOptionsKey(Identifier id) {
		return RegistryKey.of(RegistryKeys.DIMENSION, id);
	}
	
	private static RegistryKey<DimensionType> createTypeKey(Identifier id) {
		return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id);
	}
	
	public static RegistryKey<World> createWorldKey(Identifier id) {
		return RegistryKey.of(RegistryKeys.WORLD, id);
	}
}
