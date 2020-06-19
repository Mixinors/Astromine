package com.github.chainmailstudios.astromine.common.utilities;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;

import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;

public class FluidUtilities {
	public static int color(PlayerEntity player, Fluid fluid) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(player.getEntityWorld(), player.getBlockPos(), fluid.getDefaultState());
	}

	public static Sprite[] texture(Fluid fluid) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(null, null, fluid.getDefaultState());
	}

	public static String abbreviate(long value) {
		if (value < 1000) {
			return value + "mB";
		}
		int exponent = (int) (Math.log(value) / Math.log(1000));
		String[] units = new String[] {"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
		return String.format("%.1f%s", value / Math.pow(1000, exponent), units[exponent - 1]);
	}
}
