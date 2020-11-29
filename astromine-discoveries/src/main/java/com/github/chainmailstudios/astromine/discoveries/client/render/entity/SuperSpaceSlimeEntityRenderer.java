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
import com.github.chainmailstudios.astromine.discoveries.client.model.SuperSpaceSlimeEntityModel;
import com.github.chainmailstudios.astromine.discoveries.common.entity.SuperSpaceSlimeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SuperSpaceSlimeEntityRenderer extends MobRenderer<SuperSpaceSlimeEntity, SuperSpaceSlimeEntityModel> {

	private static final ResourceLocation TEXTURE = AstromineCommon.identifier("textures/entity/space_slime/space_slime.png");
	private static final ResourceLocation EXPLODING_TEXTURE = AstromineCommon.identifier("textures/entity/space_slime/space_slime_exploding.png");

	public SuperSpaceSlimeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new SuperSpaceSlimeEntityModel(16), 0.25F);
		this.addLayer(new SlimeOuterLayer(this));
	}

	@Override
	public void render(SuperSpaceSlimeEntity slimeEntity, float f, float g, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i) {
		this.shadowRadius = 2.5f;
		super.render(slimeEntity, f, g, matrices, vertexConsumerProvider, i);
	}

	@Override
	public void scale(SuperSpaceSlimeEntity slimeEntity, PoseStack matrices, float f) {
		float scale = 0.999F;
		matrices.scale(scale, scale, scale);
		matrices.translate(0.0D, -0.525D, 0.0D);

		// calculate stretch slime size
		float slimeSize = 10f;
		float i = Mth.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (slimeSize * 0.5F + 1.0F);
		float j = 1.0F / (i + 1.0F);

		// scale matrix based on slime size
		matrices.scale(j * slimeSize, 1.0F / j * slimeSize, j * slimeSize);

		// if the slime is exploding, make it quickly scale in and out
		if (slimeEntity.isExploding()) {
			float sin = 1 + (float) Math.sin(slimeEntity.getExplodingProgress() / 5f) / 10;
			matrices.scale(sin, sin, sin);
		}
	}

	/**
	 * Returns the current model texture for this {@link SuperSpaceSlimeEntityRenderer}.
	 * <p>
	 * The texture defaults to purple, but returns as red if {@link SuperSpaceSlimeEntity#isExploding}.
	 *
	 * @param slimeEntity
	 *        Super Space Slime to return texture for
	 *
	 * @return texture of entity, accounting for current phase
	 */
	@Override
	public ResourceLocation getTextureLocation(SuperSpaceSlimeEntity slimeEntity) {
		return slimeEntity.isExploding() ? EXPLODING_TEXTURE : TEXTURE;
	}
}
