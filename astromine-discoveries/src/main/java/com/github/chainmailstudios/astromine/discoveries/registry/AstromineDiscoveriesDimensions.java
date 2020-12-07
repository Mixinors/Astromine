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
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;

public class AstromineDiscoveriesDimensions extends AstromineDimensions {
	public static final Identifier EARTH_SPACE_ID = AstromineCommon.identifier("earth_space");
	public static final RegistryKey<DimensionOptions> EARTH_SPACE_OPTIONS = register(Registry.DIMENSION_OPTIONS, EARTH_SPACE_ID);
	public static final RegistryKey<DimensionType> EARTH_SPACE_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, EARTH_SPACE_ID);
	public static final RegistryKey<World> EARTH_SPACE_WORLD = register(Registry.DIMENSION, EARTH_SPACE_ID);

	public static final Identifier MOON_ID = AstromineCommon.identifier("moon");
	public static final RegistryKey<DimensionOptions> MOON_OPTIONS = register(Registry.DIMENSION_OPTIONS, MOON_ID);
	public static final RegistryKey<DimensionType> MOON_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, MOON_ID);
	public static final RegistryKey<World> MOON_WORLD = register(Registry.DIMENSION, MOON_ID);

	public static final Identifier MARS_ID = AstromineCommon.identifier("mars");
	public static final RegistryKey<DimensionOptions> MARS_OPTIONS = register(Registry.DIMENSION_OPTIONS, MARS_ID);
	public static final RegistryKey<DimensionType> MARS_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, MARS_ID);
	public static final RegistryKey<World> MARS_WORLD = register(Registry.DIMENSION, MARS_ID);

	public static final Identifier VULCAN_ID = AstromineCommon.identifier("vulcan");
	public static final RegistryKey<DimensionOptions> VULCAN_OPTIONS = register(Registry.DIMENSION_OPTIONS, VULCAN_ID);
	public static final RegistryKey<DimensionType> VULCAN_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, VULCAN_ID);
	public static final RegistryKey<World> VULCAN_WORLD = register(Registry.DIMENSION, VULCAN_ID);

	public static final Identifier GLACIOS_ID = AstromineCommon.identifier("glacios");
	public static final RegistryKey<DimensionOptions> GLACIOS_OPTIONS = register(Registry.DIMENSION_OPTIONS, GLACIOS_ID);
	public static final RegistryKey<DimensionType> GLACIOS_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, GLACIOS_ID);
	public static final RegistryKey<World> GLACIOS_WORLD = register(Registry.DIMENSION, GLACIOS_ID);

	public static void initialize() {

	}
}
