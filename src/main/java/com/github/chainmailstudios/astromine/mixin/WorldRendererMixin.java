package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.client.registry.SkyboxRegistry;
import com.github.chainmailstudios.astromine.client.render.skybox.Skybox;
import com.github.chainmailstudios.astromine.world.AstromineDimensionTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;


	@Inject(at = @At("HEAD"), method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
	void onRenderSky(MatrixStack matrices, float tickDelta, CallbackInfo callbackInformation) {
		Skybox skybox = SkyboxRegistry.INSTANCE.get(client.world.getDimensionRegistryKey().getValue());

		if (skybox != null) {
			skybox.render(matrices, tickDelta);
			callbackInformation.cancel();
		}
	}
}
