package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import com.github.chainmailstudios.astromine.registry.client.AstromineScreens;
import spinnery.widget.api.Position;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	private int lastHeight = 0;

	/**
	 * Am I even coding if I don't abuse my own poor library?
	 * @author vini2003
	 */
	@Inject(at = @At("RETURN"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	void astromine_onRender(MatrixStack matrices, float f, CallbackInfo callbackInformation) {
		int height = MinecraftClient.getInstance().getWindow().getScaledHeight();

		if (height != lastHeight) {
			lastHeight = height;
			AstromineScreens.GAS_IMAGE.setPosition(Position.of(0, height - 18, 0));
		}

		VertexConsumerProvider.Immediate provider =  MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

		AstromineScreens.GAS_IMAGE.draw(matrices, provider);
	}
}
