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

package com.github.chainmailstudios.astromine.discoveries.client.render.sky;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class MarsSkyProperties extends DimensionSpecialEffects {
	private final float[] rgba = new float[4];

	public MarsSkyProperties() {
		super(Float.NaN, false, SkyType.NORMAL, true, true);
	}

	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
		return new Vec3(0.8, 0.5, 0.08);
	}

	@Override
	public boolean isFoggyAt(int camX, int camY) {
		return false;
	}

	@Override
	public float[] getSunriseColor(float skyAngle, float tickDelta) {
		// Help me, how in the world does this work
		float g = Mth.cos(skyAngle * 6.2831855F) - 0.0F;
		if (g >= -0.4F && g <= 0.4F) {
			float i = (g - -0.0F) / 0.4F * 0.5F + 0.5F;
			float j = 1.0F - (1.0F - Mth.sin(i * 3.1415927F)) * 0.99F;
			j *= j;
			this.rgba[0] = i * 0.1F + 1.2F;
			this.rgba[1] = i * i * 0.7F + 0.2F;
			this.rgba[2] = i * i * 0.0F + 0.2F;
			this.rgba[3] = j;
			return this.rgba;
		} else {
			return null;
		}
	}
}
