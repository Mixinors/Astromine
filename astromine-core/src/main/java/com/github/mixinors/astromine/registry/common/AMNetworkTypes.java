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
import com.github.mixinors.astromine.common.network.type.EnergyNetworkType;
import com.github.mixinors.astromine.common.network.type.FluidNetworkType;
import com.github.mixinors.astromine.common.network.type.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkTypeRegistry;

public class AMNetworkTypes {
	public static final NetworkType PRIMITIVE_ENERGY = register("primitive_energy_network", new EnergyNetworkType.Primitive());
	public static final NetworkType BASIC_ENERGY = register("basic_energy_network", new EnergyNetworkType.Primitive());
	public static final NetworkType ADVANCED_ENERGY = register("advanced_energy_network", new EnergyNetworkType.Primitive());
	public static final NetworkType ELITE_ENERGY = register("elite_energy_network", new EnergyNetworkType.Primitive());
	
	public static final NetworkType FLUID = register("fluid_network", new FluidNetworkType());

	public static void init() {

	}

	public static <T extends NetworkType> T register(String name, T type) {
		return (T) NetworkTypeRegistry.INSTANCE.register(AMCommon.id(name), type);
	}
}
