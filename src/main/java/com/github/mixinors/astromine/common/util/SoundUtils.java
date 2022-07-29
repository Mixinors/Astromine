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

package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.registry.common.AMBodies;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.entity.player.PlayerEntity;

public class SoundUtils {
	public static float getSoundMufflingMultiplier(PlayerEntity player) {
		if (player.world == null) {
			return 1.0F;
		}
		
		// TODO: Optimize!
		for (var body : AMRegistries.BODY.getValues()) {
			var surfaceDimension = body.surfaceDimension();
			
			if (surfaceDimension != null && surfaceDimension.worldKey().equals(player.getWorld().getRegistryKey())) {
				var surfaceEnvironment = surfaceDimension.environment();
				
				if (surfaceEnvironment != null) {
					return surfaceEnvironment.sound();
				}
			}
			
			var orbitDimension = body.orbitDimension();
			
			if (orbitDimension != null && orbitDimension.worldKey().equals(player.getWorld().getRegistryKey())) {
				var orbitEnvironment = orbitDimension.environment();
				
				if (orbitEnvironment != null) {
					return orbitEnvironment.sound();
				}
			}
		}
		
		return 1.0F;
	}
}
