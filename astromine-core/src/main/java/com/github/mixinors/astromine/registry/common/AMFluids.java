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

import com.github.mixinors.astromine.common.fluid.ExtendedFluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.AMCommon;

public class AMFluids {
	public static final ExtendedFluid OIL = ExtendedFluid.builder().fog(0x7e121212).tint(0x7e121212).damage(0).toxic(false).infinite(false).name("oil").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid FUEL = ExtendedFluid.builder().fog(0x7e968048).tint(0x7e968048).damage(0).toxic(false).infinite(false).name("fuel").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid BIOMASS = ExtendedFluid.builder().fog(0x7e6fda34).tint(0x7e6fda34).damage(0).toxic(false).infinite(false).name("biomass").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid OXYGEN = ExtendedFluid.builder().fog(0x7e159ef9).tint(0xff159ef9).damage(0).toxic(false).infinite(false).name("oxygen").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid HYDROGEN = ExtendedFluid.builder().fog(0x7eff0019).tint(0xffff0019).damage(0).toxic(false).infinite(false).name("hydrogen").group(AMItemGroups.ASTROMINE).build();
	
	public static void init() {

	}

	public static <T extends Fluid> T register(String name, T fluid) {
		return Registry.register(Registry.FLUID, AMCommon.id(name), fluid);
	}
}
