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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import com.github.chainmailstudios.astromine.client.cca.ClientAtmosphereManager;
import com.github.chainmailstudios.astromine.client.registry.SkyboxRegistry;
import com.github.chainmailstudios.astromine.client.render.layer.Layer;
import com.github.chainmailstudios.astromine.client.render.sky.skybox.Skybox;
import com.github.chainmailstudios.astromine.common.fluid.ExtendedFluid;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class WorldRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private ClientWorld world;

	@Shadow
	@Final
	private BufferBuilderStorage bufferBuilders;

	@Shadow
	protected abstract void renderLayer(RenderLayer renderLayer, MatrixStack matrixStack, double d, double e, double f);

	@Shadow
	public abstract void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f);

	@Inject(at = @At("HEAD"), method = "renderSky(Lnet/minecraft/client/util/math/Matrix" + "Stack;F)V", cancellable = true)
	void astromine_renderSky(MatrixStack matrices, float tickDelta, CallbackInfo callbackInformation) {
		Skybox skybox = SkyboxRegistry.INSTANCE.get(this.client.world.getRegistryKey());

		if (skybox != null) {
			skybox.render(matrices, tickDelta);
			callbackInformation.cancel();
		}
	}

	@Inject(method = "render", at = @At(value = "INVOKE_STRING", target = "net/minecraft/util/profiler/Profiler.swap(Ljava/lang/String;)V", args = "ldc=blockentities", shift = At.Shift.BEFORE))
	void astromine_render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lighttmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
		Vec3d cameraPosition = camera.getPos();

		float cX = (float) cameraPosition.x;
		float cY = (float) cameraPosition.y;
		float cZ = (float) cameraPosition.z;

		VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();

		VertexConsumer consumer = immediate.getBuffer(Layer.getGas());

		Vec3d playerPos = MinecraftClient.getInstance().player.getPos();

		for (Long2ObjectMap.Entry<FluidVolume> entry : ClientAtmosphereManager.getVolumes().long2ObjectEntrySet()) {
			long blockPos = entry.getLongKey();

			FluidVolume volume = entry.getValue();

			float r = 255;
			float g = 255;
			float b = 255;
			float a = 31;

			if (volume.getFluid() instanceof ExtendedFluid) {
				int color = ((ExtendedFluid) volume.getFluid()).getTintColor();

				r = (color >> 16 & 255);
				g = (color >> 8 & 255);
				b = (color & 255);
			}

			r /= 255;
			g /= 255;
			b /= 255;
			a /= 255;

			int bX = BlockPos.unpackLongX(blockPos);
			int bZ = BlockPos.unpackLongZ(blockPos);

			if (!volume.isEmpty() && world.isChunkLoaded(bX >> 4, bZ >> 4)) {
				int bY = BlockPos.unpackLongY(blockPos);

				float x = bX - cX;
				float y = bY - cY;
				float z = bZ - cZ;

				// Bottom
				consumer.vertex(matrices.peek().getModel(), x, y, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY, bZ))))).light(15728880).next();

				// Top
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY + 1, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY + 1, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY + 1, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY + 1, bZ))))).light(15728880).next();

				// Front
				consumer.vertex(matrices.peek().getModel(), x, y, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY + 1, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY + 1, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY, bZ))))).light(15728880).next();

				// Back
				consumer.vertex(matrices.peek().getModel(), x, y, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY + 1, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY + 1, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY, bZ + 1))))).light(15728880).next();

				// Left
				consumer.vertex(matrices.peek().getModel(), x, y, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY + 1, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y + 1, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY + 1, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x, y, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX, bY, bZ + 1))))).light(15728880).next();

				// Right
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY + 1, bZ))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y + 1, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY + 1, bZ + 1))))).light(15728880).next();
				consumer.vertex(matrices.peek().getModel(), x + 1, y, z + 1).color(r, g, b, Math.min(a, a / (16F / (float) playerPos.distanceTo(new Vec3d(bX + 1, bY, bZ + 1))))).light(15728880).next();
			}
		}

		immediate.draw();
	}
}
