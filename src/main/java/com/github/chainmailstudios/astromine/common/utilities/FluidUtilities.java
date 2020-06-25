package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class FluidUtilities {
	public static int color(PlayerEntity player, Fluid fluid) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(player.getEntityWorld(), BlockPos.ORIGIN, fluid.getDefaultState());
	}

	public static Sprite[] texture(Fluid fluid) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(null, null, fluid.getDefaultState());
	}

	public static String shorten(long value) {
		if (value < 1000) {
			return value + "mB";
		}
		int exponent = (int) (Math.log(value) / Math.log(1000));
		String[] units = new String[]{"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
		return String.format("%.1f%s", value / Math.pow(1000, exponent), units[exponent - 1]);
	}

	public static MutableText rawFraction(Fraction current, Fraction maxValue, Text unit) {
		return new TranslatableText("text.astromine.tooltip.fractional_bar", rawFraction(current), rawFraction(maxValue), unit);
	}

	public static MutableText rawFraction(Fraction fraction) {
		return fraction.getDenominator() != 1 ? new TranslatableText("text.astromine.tooltip.fractional_value", fraction.getNumerator(), fraction.getDenominator())
				: new TranslatableText("text.astromine.tooltip.fractional_value_simple", fraction.getNumerator());
	}
}
