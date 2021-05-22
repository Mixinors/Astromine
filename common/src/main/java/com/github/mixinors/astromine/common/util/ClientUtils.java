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

package com.github.mixinors.astromine.common.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;

import com.github.mixinors.astromine.common.fluid.ExtendedFluid;

import java.util.function.Function;

public class ClientUtils {
	/** Returns Minecraft's client's instance. */
	public static MinecraftClient getInstance() {
		return MinecraftClient.getInstance();
	}
	
	/** Returns Minecraft's client's player. */
	public static ClientPlayerEntity getPlayer() {
		return getInstance().player;
	}
	
	/** Returns Minecraft's client's world. **/
	public static ClientWorld getWorld() {
		return getInstance().world;
	}
	
	/** Registers the necessary data for an {@link ExtendedFluid} on the client side. */
	@Environment(EnvType.CLIENT)
	public static void registerExtendedFluid(String name, int tint, Fluid still, Fluid flowing) {
		throw new UnsupportedOperationException("Cannot call this method method; must @Overwrite!");
	}
}
