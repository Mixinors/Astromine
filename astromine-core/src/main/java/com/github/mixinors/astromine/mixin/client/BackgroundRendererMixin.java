package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.registry.common.AMDimensions;
import dev.vini2003.hammer.client.util.InstanceUtils;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	@Inject(at = @At("HEAD"), method = "applyFog", cancellable = true)
	private static void am_applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
		var client = InstanceUtils.getClient();
		
		if (client.world.getRegistryKey() == AMDimensions.EARTH_SPACE_WORLD) {
			ci.cancel();
		}
	}
}
