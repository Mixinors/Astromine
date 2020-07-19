package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.client.registry.ItemRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@Inject(at = @At(value = "RETURN"),
			method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;" + "IILnet/minecraft/client" +
					"/render/model/BakedModel;)V")
	public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrix, VertexConsumerProvider vertexes, int light, int overlay, BakedModel model, CallbackInfo ci) {
		if (ItemRendererRegistry.INSTANCE.get(stack.getItem()) != null) {
			model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrix);
			ItemRendererRegistry.INSTANCE.get(stack.getItem()).render(stack, matrix, vertexes, light, overlay);
		}
	}
}
