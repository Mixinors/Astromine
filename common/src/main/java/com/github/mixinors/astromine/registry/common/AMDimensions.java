/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashSet;
import java.util.Set;

public class AMDimensions {
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();
	
	public static final Identifier EARTH_SPACE_ID = AMCommon.id("earth_space");
	public static RegistryKey<DimensionOptions> EARTH_SPACE_OPTIONS = register(Registry.DIMENSION_OPTIONS, EARTH_SPACE_ID);
	public static RegistryKey<DimensionType> EARTH_SPACE_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, EARTH_SPACE_ID);
	public static RegistryKey<World> EARTH_SPACE_WORLD = register(Registry.DIMENSION, EARTH_SPACE_ID);
	
	
	
	
	
	public static void init() {
	
	}
	
	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		RegistryKey<T> key = RegistryKey.of(registry, identifier);
		KEYS.add(key);
		return key;
	}

	public static boolean isAstromine(RegistryKey<?> key) {
		return KEYS.contains(key);
	}
}
