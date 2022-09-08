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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AMWorlds {
	public static final RegistryKey<World> ROCKET_INTERIORS = RegistryKey.of(Registry.WORLD_KEY, AMCommon.id("rocket"));
	
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();
	
	private static final Map<RegistryKey<?>, Boolean> CACHE = new ConcurrentHashMap<>();
	
	public static void init() {
	
	}
	
	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		var key = RegistryKey.of(registry, identifier);
		KEYS.add(key);
		return key;
	}
	
	public static boolean isVacuum(RegistryEntry<DimensionType> dimensionType) {
		return dimensionType.isIn(AMTagKeys.DimensionTypeTags.IS_VACUUM);
	}
	
	public static boolean isAstromine(RegistryKey<?> key) {
		CACHE.computeIfAbsent(key, KEYS::contains);
		return CACHE.get(key);
	}
}
