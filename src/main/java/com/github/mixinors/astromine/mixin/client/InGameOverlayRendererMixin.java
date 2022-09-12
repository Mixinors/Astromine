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
import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.github.mixinors.astromine.registry.common.AMTagKeys;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: Move setting to custom Biome HUD Overlay setting.
@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {
	private static float ASTROMINE$LAST_ALPHA = 0.0F;
	
	@Shadow
	private static native void renderUnderwaterOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack);
	
	private static void renderMoonDarkSideOverlay(MinecraftClient client, MatrixStack matrices) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		var bufferBuilder = Tessellator.getInstance().getBuffer();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		
		float alpha;
		
		if (client.player != null && client.world != null && client.world.getBiome(client.player.getBlockPos()).getKey().orElseThrow().equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
			alpha = MathHelper.lerp(AMValues.TICK_DELTA / 128.0F, ASTROMINE$LAST_ALPHA, 0.6F);
		} else {
			alpha = MathHelper.lerp(AMValues.TICK_DELTA / 128.0F, ASTROMINE$LAST_ALPHA, 0.0F);
		}
		
		ASTROMINE$LAST_ALPHA = alpha;
		
		var peek = matrices.peek();
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		
		bufferBuilder.vertex(peek.getPositionMatrix(), -1.0F, -1.0F, -0.5F).color(0.0F, 0.0F, 0.0F, alpha).next();
		bufferBuilder.vertex(peek.getPositionMatrix(), 1.0F, -1.0F, -0.5F).color(0.0F, 0.0F, 0.0F, alpha).next();
		bufferBuilder.vertex(peek.getPositionMatrix(), 1.0F, 1.0F, -0.5F).color(0.0F, 0.0F, 0.0F, alpha).next();
		bufferBuilder.vertex(peek.getPositionMatrix(), -1.0F, 1.0F, -0.5F).color(0.0F, 0.0F, 0.0F, alpha).next();
		
		bufferBuilder.end();
		
		BufferRenderer.drawWithShader(bufferBuilder.end());
		
		RenderSystem.disableBlend();
	}
	
	@Inject(method = "renderOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
	private static void astromine$renderOverlays(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
		if (client.player != null) {
			if (client.player.isSubmergedIn(AMTagKeys.FluidTags.INDUSTRIAL_FLUIDS)) {
				renderUnderwaterOverlay(client, matrices);
			}
			
			renderMoonDarkSideOverlay(client, matrices);
		}
	}
}
