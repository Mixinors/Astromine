package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.client.model.RocketEntityModel;
import com.github.chainmailstudios.astromine.registry.AstromineClientModels;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
	@Unique
	private RocketEntityModel rocketEntityModel = new RocketEntityModel();

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
		if (stack.getItem() == AstromineItems.ROCKET) {
			ci.cancel();
			AstromineClientModels.renderRocket(rocketEntityModel, stack, mode, matrixStack, vertexConsumerProvider, i, j);
		}
	}
}
