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
import java.util.Set;

public class AMWorlds {
	private static final Set<RegistryKey<World>> KEYS = new HashSet<>();
	public static final RegistryKey<World> ROCKET_INTERIORS = register(AMCommon.id("rocket_interiors"));
	public static final RegistryKey<World> MOON = register(AMCommon.id("moon"));
	public static final RegistryKey<World> EARTH_ORBIT = register(AMCommon.id("earth_orbit"));
	
	public static void init() {
	
	}
	
	public static RegistryKey<World> register(Identifier id) {
		var key = RegistryKey.of(Registry.WORLD_KEY, id);
		KEYS.add(key);
		return key;
	}
	
	public static boolean isVacuum(RegistryEntry<DimensionType> dimensionType) {
		return dimensionType.isIn(AMTagKeys.DimensionTypeTags.IS_VACUUM);
	}
	
	public static boolean isAstromine(RegistryKey<World> key) {
		return KEYS.contains(key);
	}
}
