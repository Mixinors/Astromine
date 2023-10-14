package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.client.util.TransformationManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adapted from Immersive Portals
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(
			method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FDDD)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private void renderClouds(
			MatrixStack matrices, Matrix4f matrix4f, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo cir
	) {
		if (TransformationManager.isIsometricView) {
			cir.cancel();
		}
	}
}
