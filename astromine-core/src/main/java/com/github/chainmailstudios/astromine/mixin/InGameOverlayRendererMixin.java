package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.registry.AstromineTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {
	@Shadow
	private static native void renderUnderwaterOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack);

	@Inject(method = "renderOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSubmergedIn(Lnet/minecraft/tag/Tag;)Z"))
	private static void astromine_renderFluidOverlay(MinecraftClient client, MatrixStack matrixStack, CallbackInfo ci) {
		if (client.player.isSubmergedIn(AstromineTags.INDUSTRIAL_FLUID)) {
			renderUnderwaterOverlay(client, matrixStack);
		}
	}
}
