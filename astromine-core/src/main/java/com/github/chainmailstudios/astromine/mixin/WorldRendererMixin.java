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

package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.client.cca.ClientAtmosphereManager;
import com.github.chainmailstudios.astromine.common.fluid.AdvancedFluid;
import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.common.data.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import com.github.chainmailstudios.astromine.client.registry.SkyboxRegistry;
import com.github.chainmailstudios.astromine.client.render.sky.skybox.AbstractSkybox;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class WorldRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow private ClientWorld world;

	@Shadow @Final private BufferBuilderStorage bufferBuilders;

	@Inject(at = @At("HEAD"), method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
	void onRenderSky(MatrixStack matrices, float tickDelta, CallbackInfo callbackInformation) {
		AbstractSkybox skybox = SkyboxRegistry.INSTANCE.get(this.client.world.getRegistryKey());

		if (skybox != null) {
			skybox.render(matrices, tickDelta);
			callbackInformation.cancel();
		}
	}

	@Inject(at = @At(value = "HEAD", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V")
	void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		Vec3d cameraPosition = camera.getPos();

		float cX = (float) cameraPosition.x;
		float cY = (float) cameraPosition.y;
		float cZ = (float) cameraPosition.z;

		VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();

		VertexConsumer consumer = immediate.getBuffer(Layers.flat());

		ClientAtmosphereManager.getVolumes().forEach(((blockPos, volume) -> {
			int r = 255;
			int g = 255;
			int b = 255;
			int a = 255;
			
			if (volume.getFluid() instanceof AdvancedFluid) {
				int color = ((AdvancedFluid) volume.getFluid()).getTintColor();
				
				r = (color >> 24 & 255); 
				g = (color >> 16 & 255);
				b = (color >> 8 & 255);
				a = (color & 255);
			}
			
			if (!volume.isEmpty()) {
				float bX = blockPos.getX();
				float bY = blockPos.getY();
				float bZ = blockPos.getZ();

				float x = bX - cX;
				float y = bY - cY;
				float z = bZ - cZ;

				// Bottom
				consumer.vertex(matrices.peek().getModel(), x, y, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y, z + 1).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z + 1).color(r, g, b, a).light(15728880).next();


				// Top
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z + 1).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z + 1).color(r, g, b, a).light(15728880).next();


				// Front
				consumer.vertex(matrices.peek().getModel(), x, y, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1   , y + 1, z).color(r, g, b, a).light(15728880).next();


				// Back
				consumer.vertex(matrices.peek().getModel(), x, y, z + 1).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z + 1).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z + 1).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1   , y + 1, z + 1).color(r, g, b, a).light(15728880).next();


				// Left
				consumer.vertex(matrices.peek().getModel(), x, y, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y, z + 1).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z + 1).color(r, g, b, a).light(15728880).next();


				// Right
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z + 1).color(r, g, b, a).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z + 1).color(r, g, b, a).light(15728880).next();

			}
		}));
	}
}
