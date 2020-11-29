/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.item.AnimatedArmorItem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class ArmorFeatureRendererMixin {
	@Shadow
	protected abstract ResourceLocation getArmorLocation(ArmorItem armorItem, boolean bl, @Nullable String string);

	@Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
	private void renderArmorParts(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, ArmorItem armorItem, boolean bl, HumanoidModel<LivingEntity> bipedEntityModel, boolean bl2, float f, float g, float h, @Nullable String string, CallbackInfo ci) {
		if (armorItem instanceof AnimatedArmorItem) {
			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumerProvider, getArmorCutoutNoCull(this.getArmorLocation(armorItem, bl2, string), ((AnimatedArmorItem) armorItem).getFrames()), false, bl);
			bipedEntityModel.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, f, g, h, 1.0F);
			ci.cancel();
		}
	}

	@Unique
	private static RenderType getArmorCutoutNoCull(ResourceLocation texture, int frames) {
		RenderType.CompositeState multiPhaseParameters = RenderType.CompositeState.builder()
			.setTextureState(new AnimatedArmorItem.AnimatedTexturePhase(texture, frames))
			.setTransparencyState(RenderType.NO_TRANSPARENCY)
			.setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING)
			.setAlphaState(RenderType.DEFAULT_ALPHA)
			.setCullState(RenderType.NO_CULL)
			.setLightmapState(RenderType.LIGHTMAP)
			.setOverlayState(RenderType.OVERLAY)
			.setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
			.createCompositeState(true);
		return RenderType.create("astromine:armor_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, false, multiPhaseParameters);
	}
}
