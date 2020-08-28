package com.github.chainmailstudios.astromine.technologies.mixin;

import com.github.chainmailstudios.astromine.technologies.client.model.RocketEntityModel;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesItems;
import com.github.chainmailstudios.astromine.technologies.registry.client.AstromineTechnologiesClientModels;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
	@Unique
	private final RocketEntityModel rocketEntityModel = new RocketEntityModel();

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
		if (stack.getItem() == AstromineTechnologiesItems.ROCKET) {
			ci.cancel();
			AstromineTechnologiesClientModels.renderRocket(rocketEntityModel, stack, mode, matrices, vertexConsumerProvider, i, j);
		}
	}
}
