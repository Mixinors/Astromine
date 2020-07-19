package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.registry.client.AstromineScreens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spinnery.widget.api.Position;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	private int lastHeight = 0;

	/**
	 * Am I even coding if I don't abuse my own poor library?
	 *
	 * @author vini2003
	 */
	@Inject(at = @At("RETURN"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	void astromine_onRender(MatrixStack matrices, float f, CallbackInfo callbackInformation) {
		if (MinecraftClient.getInstance().getCameraEntity() instanceof LivingEntity) {
			if (((LivingEntity) MinecraftClient.getInstance().getCameraEntity()).getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof SpaceSuitItem) {
				if (AstromineScreens.GAS_IMAGE.isHidden()) {
					AstromineScreens.GAS_IMAGE.setHidden(false);
				}
				int height = MinecraftClient.getInstance().getWindow().getScaledHeight();
				if (height != lastHeight) {
					lastHeight = height;
					AstromineScreens.GAS_IMAGE.setPosition(Position.of(0, height - 18, 0));
				}

				VertexConsumerProvider.Immediate provider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

				AstromineScreens.GAS_IMAGE.draw(matrices, provider);
			} else {
				if (!AstromineScreens.GAS_IMAGE.isHidden()) {
					AstromineScreens.GAS_IMAGE.setHidden(true);
				}
			}
		}
	}
}
