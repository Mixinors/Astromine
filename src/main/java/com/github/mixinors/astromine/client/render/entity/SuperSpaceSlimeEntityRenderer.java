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
import com.github.mixinors.astromine.client.model.SuperSpaceSlimeEntityModel;
import com.github.mixinors.astromine.common.entity.SuperSpaceSlimeEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SuperSpaceSlimeEntityRenderer extends MobEntityRenderer<SuperSpaceSlimeEntity, SuperSpaceSlimeEntityModel> {
	private static final Identifier TEXTURE = AMCommon.id("textures/entity/space_slime/space_slime.png");
	private static final Identifier EXPLODING_TEXTURE = AMCommon.id("textures/entity/space_slime/space_slime_exploding.png");
	
	public SuperSpaceSlimeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SuperSpaceSlimeEntityModel(context.getPart(EntityModelLayers.SLIME)), 0.25F);
		
		this.addFeature(new SlimeOverlayFeatureRenderer(this, context.getModelLoader()));
	}
	
	@Override
	public void render(SuperSpaceSlimeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		this.shadowRadius = 2.5F;
		
		super.render(entity, yaw, tickDelta, matrices, provider, light);
	}
	
	@Override
	public void scale(SuperSpaceSlimeEntity slimeEntity, MatrixStack matrices, float tickDelta) {
		var scale = 0.999F;
		
		matrices.scale(scale, scale, scale);
		
		matrices.translate(0.0D, -0.525D, 0.0D);
		
		var slimeSize = 10.0F;
		
		var stretch = MathHelper.lerp(tickDelta, slimeEntity.prevStretch, slimeEntity.stretch) / (slimeSize * 0.5F + 1.0F);
		
		var multiplier = 1.0F / (stretch + 1.0F);
		
		matrices.scale(multiplier * slimeSize, 1.0F / multiplier * slimeSize, multiplier * slimeSize);
		
		if (slimeEntity.isExploding()) {
			var explodingScale = 1.0F + (float) Math.sin(slimeEntity.getExplodingProgress() / 5.0F) / 10.0F;
			
			matrices.scale(explodingScale, explodingScale, explodingScale);
		}
	}
	
	@Override
	public Identifier getTexture(SuperSpaceSlimeEntity slimeEntity) {
		return slimeEntity.isExploding() ? EXPLODING_TEXTURE : TEXTURE;
	}
}
