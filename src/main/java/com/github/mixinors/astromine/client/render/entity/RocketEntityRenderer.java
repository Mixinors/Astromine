/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.client.render.entity;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.model.entity.RocketEntityModel;
import com.github.mixinors.astromine.common.entity.rocket.RocketEntity;
import com.github.mixinors.astromine.registry.client.AMEntityModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {
	public static final ResourceLocation ID = AMCommon.id("textures/entity/rocket/rocket.png");
	
	private final RocketEntityModel model;
	
	public RocketEntityRenderer(Context context) {
		super(context);
		
		this.model = new RocketEntityModel(context.bakeLayer(AMEntityModelLayers.ROCKET));
	}
	
	@Override
	public void render(RocketEntity rocket, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light) {
		matrices.pushPose();
		
		matrices.scale(-1.0F, -1.0F, 1.0F);
		matrices.scale(2.0F, 2.0F, 2.0F);

		var vertexConsumer = provider.getBuffer(model.renderType(getTextureLocation(rocket)));
		
		model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		
		matrices.popPose();
		
		super.render(rocket, yaw, tickDelta, matrices, provider, light);
	}
	
	@Override
	public ResourceLocation getTextureLocation(RocketEntity entity) {
		return ID;
	}
}
