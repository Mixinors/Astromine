package com.github.mixinors.astromine.common.fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public class ExtraFluidConstants {
	public static final long INGOT_FROM_2x2_BLOCK = 20250; // BLOCK / 4
	public static final long NUGGET_FROM_2x2_BLOCK = 2250; // INGOT_FROM_2x2_BLOCK / 9
	
	public static long ingot(boolean block2x2) {
		return ingots(1, block2x2);
	}
	
	public static long nugget(boolean block2x2) {
		return nuggets(1, block2x2);
	}
	
	public static long ingots(int count, boolean block2x2) {
		return (block2x2 ? INGOT_FROM_2x2_BLOCK : FluidConstants.INGOT) * count;
	}
	
	public static long nuggets(int count, boolean block2x2) {
		return (block2x2 ? NUGGET_FROM_2x2_BLOCK : FluidConstants.NUGGET) * count;
	}
}
