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

package com.github.mixinors.astromine.registry;

import com.github.mixinors.astromine.common.fluid.ExtendedFluid;
import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.AMCommon;

import java.util.List;

public class AMFluids {
	public static final Fluid CRUDE_OIL = ExtendedFluid.builder().fog(0x7e675005).tint(0x7e675005).damage(0).toxic(false).infinite(false).name("crude_oil").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid RESIDUAL_FUEL_OIL = ExtendedFluid.builder().fog(0x7e675005).tint(0x7e675005).damage(0).toxic(false).infinite(false).name("residual_fuel_oil").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid HEAVY_GAS_OIL = ExtendedFluid.builder().fog(0x7e675005).tint(0x7e675005).damage(0).toxic(false).infinite(false).name("heavy_gas_oil").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid DIESEL = ExtendedFluid.builder().fog(0x7e7B6522).tint(0x7e7B6522).damage(0).toxic(false).infinite(false).name("diesel").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid KEROSENE = ExtendedFluid.builder().fog(0x7e968048).tint(0x7e968048).damage(0).toxic(false).infinite(false).name("kerosene").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid NAPHTHA = ExtendedFluid.builder().fog(0x7eB19D6F).tint(0x7eB19D6F).damage(0).toxic(false).infinite(false).name("naphtha").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid GASOLINE = ExtendedFluid.builder().fog(0x7eCBB794).tint(0x7eCBB794).damage(0).toxic(false).infinite(false).name("gasoline").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid BUTANE = ExtendedFluid.builder().fog(0x7eE4D2B9).tint(0x7eE4D2B9).damage(0).toxic(false).infinite(false).name("butane").group(AMItemGroups.ASTROMINE).build();
	
	public static final List<Fluid> OIL_DERIVATIVES = Lists.newArrayList(CRUDE_OIL, RESIDUAL_FUEL_OIL, HEAVY_GAS_OIL, DIESEL, KEROSENE, NAPHTHA, GASOLINE, BUTANE);
	
	public static final Fluid OXYGEN = ExtendedFluid.builder().fog(0x7e159ef9).tint(0xff159ef9).damage(0).toxic(false).infinite(false).name("oxygen").group(AMItemGroups.ASTROMINE).build();
	
	public static final Fluid HYDROGEN = ExtendedFluid.builder().fog(0x7eff0019).tint(0xffff0019).damage(0).toxic(false).infinite(false).name("hydrogen").group(AMItemGroups.ASTROMINE).build();
	
	public static void init() {

	}

	public static <T extends Fluid> T register(String name, T fluid) {
		return Registry.register(Registry.FLUID, AMCommon.id(name), fluid);
	}
}
