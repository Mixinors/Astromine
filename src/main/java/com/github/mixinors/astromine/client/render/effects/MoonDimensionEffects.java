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
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class MoonDimensionEffects extends DimensionEffects {
	public MoonDimensionEffects() {
		super(Float.NaN, false, SkyType.NONE, true, true);
	}
	
	@Override
	public boolean isDarkened() {
		var client = InstanceUtil.getClient();
		
		if (client.player != null) {
			if (client.world != null) {
				if (client.world.getBiome(client.player.getBlockPos()).equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
					return true;
				}
			}
		}

		return super.isDarkened();
	}
	
	@Override
	public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
		return Vec3d.ZERO;
	}
	
	@Override
	public boolean useThickFog(int camX, int camY) {
		var client = InstanceUtil.getClient();
		
		if (client.player != null) {
			if (client.world != null) {
				if (client.world.getBiome(client.player.getBlockPos()).getKey().orElseThrow().equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
