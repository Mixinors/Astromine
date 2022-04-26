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
import com.github.mixinors.astromine.client.model.SpaceSlimeEntityModel;
import com.github.mixinors.astromine.common.entity.SpaceSlimeEntity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class SpaceSlimeEntityRenderer extends MobEntityRenderer<SpaceSlimeEntity, SpaceSlimeEntityModel> {
	private static final Identifier TEXTURE = AMCommon.id("textures/entity/space_slime/space_slime.png");

	public SpaceSlimeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SpaceSlimeEntityModel(context.getPart(EntityModelLayers.SLIME)), 0.25F);
		this.addFeature(new SlimeOverlayFeatureRenderer(this, context.getModelLoader()));
		// I don't get why it's complaining, MobEntityRenderer implements FeatureRendererContext through LivingEntityRenderer
	}

	@Override
	public void render(SpaceSlimeEntity slimeEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i) {
		this.shadowRadius = 0.25F * (float) slimeEntity.getSize();

		// if the slime is floating, we rotate it around the x axis for 1 full rotation
		// todo: random axis rotation
		if (slimeEntity.isFloating()) {
			var progress = slimeEntity.getFloatingProgress() / 200f;
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(progress * 360));
		}

		super.render(slimeEntity, f, g, matrices, vertexConsumerProvider, i);
	}

	@Override
	public void scale(SpaceSlimeEntity slimeEntity, MatrixStack matrices, float f) {
		var scale = 0.999F;
		matrices.scale(scale, scale, scale);
		matrices.translate(0.0D, -0.125D, 0.0D);

		// calculate stretch slime size
		var slimeSize = (float) slimeEntity.getSize();
		var i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (slimeSize * 0.5F + 1.0F);
		var j = 1.0F / (i + 1.0F);

		// scale matrix based on slime size
		matrices.scale(j * slimeSize, 1.0F / j * slimeSize, j * slimeSize);
	}

	@Override
	public Identifier getTexture(SpaceSlimeEntity slimeEntity) {
		return TEXTURE;
	}
}
