package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.item.AnimatedArmorItem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {
	@Shadow
	protected abstract Identifier getArmorTexture(ArmorItem armorItem, boolean bl, @Nullable String string);

	@Inject(method = "renderArmorParts", at = @At("HEAD"), cancellable = true)
	private void renderArmorParts(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ArmorItem armorItem, boolean bl, BipedEntityModel<LivingEntity> bipedEntityModel, boolean bl2, float f, float g, float h,
		@Nullable String string, CallbackInfo ci) {
		if (armorItem instanceof AnimatedArmorItem) {
			VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, getArmorCutoutNoCull(this.getArmorTexture(armorItem, bl2, string), ((AnimatedArmorItem) armorItem).getFrames()), false, bl);
			bipedEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, f, g, h, 1.0F);
			ci.cancel();
		}
	}

	@Unique
	private static RenderLayer getArmorCutoutNoCull(Identifier texture, int frames) {
		RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
			.texture(new AnimatedArmorItem.Texture(texture, frames))
			.transparency(RenderLayer.NO_TRANSPARENCY)
			.diffuseLighting(RenderLayer.ENABLE_DIFFUSE_LIGHTING)
			.alpha(RenderLayer.ONE_TENTH_ALPHA)
			.cull(RenderLayer.DISABLE_CULLING)
			.lightmap(RenderLayer.ENABLE_LIGHTMAP)
			.overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
			.layering(RenderLayer.VIEW_OFFSET_Z_LAYERING)
			.build(true);
		return RenderLayer.of("astromine:armor_cutout_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, false, multiPhaseParameters);
	}
}
