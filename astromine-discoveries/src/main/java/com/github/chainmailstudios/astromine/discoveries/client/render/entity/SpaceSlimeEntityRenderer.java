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
import com.github.chainmailstudios.astromine.discoveries.client.model.SpaceSlimeEntityModel;
import com.github.chainmailstudios.astromine.discoveries.common.entity.SpaceSlimeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpaceSlimeEntityRenderer extends MobRenderer<SpaceSlimeEntity, SpaceSlimeEntityModel> {
	private static final ResourceLocation TEXTURE = AstromineCommon.identifier("textures/entity/space_slime/space_slime.png");

	public SpaceSlimeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new SpaceSlimeEntityModel(16), 0.25F);
		this.addLayer(new SlimeOuterLayer(this));
	}

	@Override
	public void render(SpaceSlimeEntity slimeEntity, float f, float g, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i) {
		this.shadowRadius = 0.25F * (float) slimeEntity.getSize();

		// if the slime is floating, we rotate it around the x axis for 1 full rotation
		// todo: random axis rotation
		if (slimeEntity.isFloating()) {
			float progress = slimeEntity.getFloatingProgress() / 200f;
			matrices.mulPose(Vector3f.XP.rotationDegrees(progress * 360));
		}

		super.render(slimeEntity, f, g, matrices, vertexConsumerProvider, i);
	}

	@Override
	public void scale(SpaceSlimeEntity slimeEntity, PoseStack matrices, float f) {
		float scale = 0.999F;
		matrices.scale(scale, scale, scale);
		matrices.translate(0.0D, -0.125D, 0.0D);

		// calculate stretch slime size
		float slimeSize = (float) slimeEntity.getSize();
		float i = Mth.lerp(f, slimeEntity.oSquish, slimeEntity.squish) / (slimeSize * 0.5F + 1.0F);
		float j = 1.0F / (i + 1.0F);

		// scale matrix based on slime size
		matrices.scale(j * slimeSize, 1.0F / j * slimeSize, j * slimeSize);
	}

	@Override
	public ResourceLocation getTextureLocation(SpaceSlimeEntity slimeEntity) {
		return TEXTURE;
	}
}
