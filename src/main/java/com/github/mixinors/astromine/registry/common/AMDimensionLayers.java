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

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.entity.placer.EarthOrbitEntityPlacer;
import com.github.mixinors.astromine.common.registry.DimensionLayerRegistry;
import net.minecraft.world.World;

public class AMDimensionLayers {
	public static void init() {
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.BOTTOM, AMWorlds.EARTH_ORBIT_WORLD, AMConfig.get().world.layers.earth.travelY, World.OVERWORLD, EarthOrbitEntityPlacer.TO_EARTH);
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.TOP, World.OVERWORLD, AMConfig.get().world.layers.earthOrbit.travelY, AMWorlds.EARTH_ORBIT_WORLD, EarthOrbitEntityPlacer.TO_EARTH_ORBIT);
	}
}
