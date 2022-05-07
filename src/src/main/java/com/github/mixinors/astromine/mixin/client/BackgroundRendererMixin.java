package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.vini2003.hammer.core.api.client.util.InstanceUtils;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	@Inject(at = @At("HEAD"), method = "applyFog", cancellable = true)
	private static void astromine$applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
		var client = InstanceUtils.getClient();
		
		if (client != null && client.world.getRegistryKey().equals(AMWorlds.EARTH_SPACE_WORLD)) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private static void astromine$render(Camera camera, float tickDelta, ClientWorld world, int i, float f, CallbackInfo ci) {
		var client = InstanceUtils.getClient();
		
		if (client != null && client.world.getRegistryKey().equals(AMWorlds.EARTH_SPACE_WORLD)) {
			ci.cancel();
		}
	}
}
