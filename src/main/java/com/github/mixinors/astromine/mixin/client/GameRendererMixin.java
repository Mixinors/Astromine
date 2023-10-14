package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.client.util.TransformationManager;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Adapted from Immersive Portals
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(
			method = "getBasicProjectionMatrix",
			at = @At("HEAD"),
			cancellable = true
	)
	private void onGetBasicProjectionMatrix(
			double d,
			CallbackInfoReturnable<Matrix4f> cir
	) {
		if (TransformationManager.isIsometricView) {
			cir.setReturnValue(TransformationManager.getIsometricProjection());
		}
	}
}