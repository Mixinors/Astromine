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
	
	public static final double PRIMITIVE_TANK_SPEED = 1.0D;
	public static final double BASIC_TANK_SPEED = 2.0D;
	public static final double ADVANCED_TANK_SPEED = 4.0D;
	public static final double ELITE_TANK_SPEED = 16.0D;
	
	public static final double PRIMITIVE_SPEED_MODIFIER = 0.5D;
	public static final double BASIC_SPEED_MODIFIER = 1.0D;
	public static final double ADVANCED_SPEED_MODIFIER = 2.0D;
	public static final double ELITE_SPEED_MODIFIER = 4.0D;
}
