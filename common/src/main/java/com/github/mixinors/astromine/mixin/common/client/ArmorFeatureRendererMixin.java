/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.mixin.common.client;

import com.github.mixinors.astromine.mixin.common.common.RenderLayerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.common.item.AnimatedArmorItem;
import org.jetbrains.annotations.Nullable;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {
	@Shadow
	protected abstract Identifier getArmorTexture(ArmorItem armorItem, boolean bl, @Nullable String string);

	@Inject(method = "renderArmorParts", at = @At("HEAD"), cancellable = true)
	private void renderArmorParts(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ArmorItem armorItem, boolean bl, BipedEntityModel<LivingEntity> bipedEntityModel, boolean bl2, float f, float g, float h, @Nullable String string, CallbackInfo ci) {
		if (armorItem instanceof AnimatedArmorItem animatedArmorItem) {
			var vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, getArmorCutoutNoCull(this.getArmorTexture(armorItem, bl2, string), animatedArmorItem.getFrames()), false, bl);
			
			bipedEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, f, g, h, 1.0F);
			
			ci.cancel();
		}
	}

	@Unique
	private static RenderLayer getArmorCutoutNoCull(Identifier texture, int frames) {
		var multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
			.texture(new AnimatedArmorItem.AnimatedTexturePhase(texture, frames))
			.transparency(RenderLayerAccessor.getNO_TRANSPARENCY())
			.diffuseLighting(RenderLayerAccessor.getENABLE_DIFFUSE_LIGHTING())
			.alpha(RenderLayerAccessor.getONE_TENTH_ALPHA())
			.cull(RenderLayerAccessor.getDISABLE_CULLING())
			.lightmap(RenderLayerAccessor.getENABLE_LIGHTMAP())
			.overlay(RenderLayerAccessor.getENABLE_OVERLAY_COLOR())
			.layering(RenderLayerAccessor.getVIEW_OFFSET_Z_LAYERING())
			.build(true);
		
		return RenderLayer.of("astromine:armor_cutout_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, false, multiPhaseParameters);
	}
}
