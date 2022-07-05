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

package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.registry.client.AMValues;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	private static float ASTROMINE$LAST_FOG_START = 0.0F;
	private static float ASTROMINE$LAST_FOG_END = 0.0F;
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), method = "applyFog")
	private static float astromine$applyFog$setShaderFogStart(float original) {
		var client = InstanceUtil.getClient();
		
		if (client.world != null) {
			if (AMWorlds.isAstromine(client.world.getRegistryKey())) {
				ASTROMINE$LAST_FOG_START = MathHelper.lerp(AMValues.TICK_DELTA / 2048.0F, ASTROMINE$LAST_FOG_START, original);
			} else {
				ASTROMINE$LAST_FOG_START = original;
			}
		}
		
		return ASTROMINE$LAST_FOG_START;
	}
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"), method = "applyFog")
	private static float astromine$applyFog$setShaderFogEnd(float original) {
		var client = InstanceUtil.getClient();
		
		if (client.world != null) {
			if (AMWorlds.isAstromine(client.world.getRegistryKey())) {
				ASTROMINE$LAST_FOG_END = MathHelper.lerp(AMValues.TICK_DELTA / 2048.0F, ASTROMINE$LAST_FOG_END, original);
			} else {
				ASTROMINE$LAST_FOG_END = original;
			}
		}
		
		return ASTROMINE$LAST_FOG_END;
	}
}
