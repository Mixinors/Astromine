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

package com.github.mixinors.astromine.client.render.effects;

import com.github.mixinors.astromine.registry.common.AMBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public class MoonDimensionEffects extends DimensionSpecialEffects {
	public MoonDimensionEffects() {
		super(Float.NaN, false, SkyType.NONE, true, true);
	}
	
	@Override
	public boolean constantAmbientLight() {
		var client = Minecraft.getInstance();
		
		if (client.player != null && client.level != null) {
			if (client.level.getBiome(client.player.blockPosition()).unwrapKey().orElseThrow().equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
				return true;
			}
		}

		return super.constantAmbientLight();
	}
	
	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
		return Vec3.ZERO;
	}
	
	@Override
	public boolean isFoggyAt(int camX, int camY) {
		var client = Minecraft.getInstance();
		
		if (client.player != null && client.level != null) {
			if (client.level.getBiome(client.player.blockPosition()).unwrapKey().orElseThrow().equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
				return true;
			}
		}
		
		return false;
	}
}
