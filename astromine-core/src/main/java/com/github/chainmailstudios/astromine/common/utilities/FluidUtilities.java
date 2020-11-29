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

package com.github.chainmailstudios.astromine.common.utilities;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;

public class FluidUtilities {
	/**
	 * Returns the color of the specified fluid at the given player's position.
	 */
	public static int getColor(Player player, Fluid fluid) {
		FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
		if (handler == null) {
			throw new NullPointerException("No fluid renderer for " + Registry.FLUID.getKey(fluid));
		}
		return handler.getFluidColor(player.getCommandSenderWorld(), BlockPos.ZERO, fluid.defaultFluidState());
	}

	/**
	 * Returns the sprites of the specified fluid.
	 */
	public static TextureAtlasSprite[] getSprites(Fluid fluid) {
		FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
		if (handler == null) {
			throw new NullPointerException("No fluid renderer for " + Registry.FLUID.getKey(fluid));
		}
		return handler.getFluidSprites(null, null, fluid.defaultFluidState());
	}

	/**
	 * Returns the first sprite of the specified fluid.
	 */
	public static TextureAtlasSprite getSprite(Fluid fluid) {
		return getSprites(fluid)[0];
	}
}
