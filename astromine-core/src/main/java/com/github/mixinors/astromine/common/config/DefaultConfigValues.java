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

package com.github.mixinors.astromine.common.config;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public class DefaultConfigValues {
	public static final long PRIMITIVE_BATTERY_ENERGY = 8000;
	public static final long BASIC_BATTERY_ENERGY = 16000;
	public static final long ADVANCED_BATTERY_ENERGY = 32000;
	public static final long ELITE_BATTERY_ENERGY = 64000;

	public static final long PRIMITIVE_BATTERY_PACK_ENERGY = PRIMITIVE_BATTERY_ENERGY * 6;
	public static final long BASIC_BATTERY_PACK_ENERGY = BASIC_BATTERY_ENERGY * 6;
	public static final long ADVANCED_BATTERY_PACK_ENERGY = ADVANCED_BATTERY_ENERGY * 6;
	public static final long ELITE_BATTERY_PACK_ENERGY = ELITE_BATTERY_ENERGY * 6;

	public static final long PORTABLE_TANK_FLUID = FluidConstants.BUCKET * 8L;
	public static final long LARGE_PORTABLE_TANK_FLUID = PORTABLE_TANK_FLUID * 2;
	
	public static final long PRIMITIVE_FLUID_STORAGE = LARGE_PORTABLE_TANK_FLUID * 2L;
	public static final long BASIC_FLUID_STORAGE = LARGE_PORTABLE_TANK_FLUID * 4L;
	public static final long ADVANCED_FLUID_STORAGE = LARGE_PORTABLE_TANK_FLUID * 8L;
	public static final long ELITE_FLUID_STORAGE = LARGE_PORTABLE_TANK_FLUID * 16L;
	
	public static final double PRIMITIVE_SPEED_MODIFIER = 0.5D;
	public static final double BASIC_SPEED_MODIFIER = 1.0D;
	public static final double ADVANCED_SPEED_MODIFIER = 2.0D;
	public static final double ELITE_SPEED_MODIFIER = 4.0D;

	public static final double BASE_SPEED = 1.0D;
	public static final double TANK_BASE_SPEED = 10.0D;
	public static final double CAPACITOR_BASE_SPEED = 10.0D;

	public static final long UTILITY_DELAY = 40L;
	public static final long UTILITY_ENERGY = PRIMITIVE_BATTERY_PACK_ENERGY;
	public static final long UTILITY_FLUID = PORTABLE_TANK_FLUID;
	public static final long UTILITY_ENERGY_CONSUMED = 1024L;
}
