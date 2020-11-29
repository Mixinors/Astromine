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

package com.github.chainmailstudios.astromine.discoveries.client.render.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.client.model.PrimitiveRocketEntityModel;
import com.github.chainmailstudios.astromine.discoveries.common.entity.PrimitiveRocketEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PrimitiveRocketEntityRenderer extends EntityRenderer<PrimitiveRocketEntity> {
	public static final ResourceLocation ID = AstromineCommon.identifier("textures/entity/rocket/primitive_rocket.png");

	private final PrimitiveRocketEntityModel model = new PrimitiveRocketEntityModel();

	public PrimitiveRocketEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(PrimitiveRocketEntity rocket, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light) {
		matrices.pushPose();

		matrices.translate(0.0D, 3.0D, 0.0D);

		matrices.scale(-1.0F, -1.0F, 1.0F);

		matrices.scale(2, 2, 2);

		matrices.mulPose(Vector3f.YP.rotationDegrees(90.0F));

		this.model.setupAnim(rocket, 0, 0.0F, -0.1F, rocket.getViewYRot(tickDelta), rocket.getViewXRot(tickDelta));

		VertexConsumer vertexConsumer = provider.getBuffer(this.model.renderType(this.getTextureLocation(rocket)));

		this.model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		matrices.popPose();

		super.render(rocket, yaw, tickDelta, matrices, provider, light);
	}

	@Override
	public ResourceLocation getTextureLocation(PrimitiveRocketEntity entity) {
		return ID;
	}
}
