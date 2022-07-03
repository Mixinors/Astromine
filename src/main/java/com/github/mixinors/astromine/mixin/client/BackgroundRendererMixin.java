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

import com.github.mixinors.astromine.common.event.BackgroundEvents;
import com.github.mixinors.astromine.registry.client.AMValues;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	@Inject(at = @At("HEAD"), method = "applyFog", cancellable = true)
	private static void astromine$applyFog(Camera camera, BackgroundRenderer.FogType type, float viewDistance, boolean thickFog, CallbackInfo ci) {
		var res = BackgroundEvents.FOG.invoker().calculate(camera, type);
		
		if (res.isFalse()) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private static void astromine$render(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {
		var res = BackgroundEvents.RENDER.invoker().render(camera, tickDelta, world, viewDistance, skyDarkness);
		
		if (res.isFalse()) {
			ci.cancel();
		}
	}
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), method = "applyFog")
	private static float astromine$applyFog$setShaderFogStart(float original) {
		var client = InstanceUtil.getClient();
		
		if (AMWorlds.isAstromine(client.world.getRegistryKey())) {
			if (AMValues.LAST_FOG_START == Float.MAX_VALUE) {
				AMValues.LAST_FOG_START = original;
			} else {
				AMValues.LAST_FOG_START = MathHelper.lerp(AMValues.LAST_TICK_DELTA / 2048.0F, AMValues.LAST_FOG_START, original);
			}
		} else {
			AMValues.LAST_FOG_START = original;
		}
		
		return AMValues.LAST_FOG_START;
	}
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"), method = "applyFog")
	private static float astromine$applyFog$setShaderFogEnd(float original) {
		var client = InstanceUtil.getClient();
		
		if (AMWorlds.isAstromine(client.world.getRegistryKey())) {
			if (AMValues.LAST_FOG_END == Float.MAX_VALUE) {
				AMValues.LAST_FOG_END = original;
			} else {
				AMValues.LAST_FOG_END = MathHelper.lerp(AMValues.LAST_TICK_DELTA / 2048.0F, AMValues.LAST_FOG_END, original);
			}
		} else {
			AMValues.LAST_FOG_END = original;
		}
		
		return AMValues.LAST_FOG_END;
	}
}
