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
import com.github.mixinors.astromine.common.entity.slime.SpaceSlimeEntity;
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
	}
	
	@Override
	public void render(SpaceSlimeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int i) {
		this.shadowRadius = 0.25F * (float) entity.getSize();
		
		// if the slime is floating, we rotate it around the x axis for 1 full rotation
		// todo: random axis rotation
		if (entity.isFloating()) {
			var floatingProgress = MathHelper.lerp(tickDelta, entity.prevFloatingProgress, entity.getFloatingProgress());
			entity.prevFloatingProgress = floatingProgress;
			
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((floatingProgress / 200.0F) * 360.0F));
		}
		
		super.render(entity, tickDelta, tickDelta, matrices, provider, i);
	}
	
	@Override
	public void scale(SpaceSlimeEntity slimeEntity, MatrixStack matrices, float tickDelta) {
		var scale = 0.999F;
		
		matrices.scale(scale, scale, scale);
		
		matrices.translate(0.0D, -0.125D, 0.0D);
		
		var slimeSize = (float) slimeEntity.getSize();
		
		var stretch = MathHelper.lerp(tickDelta, slimeEntity.lastStretch, slimeEntity.stretch) / (slimeSize * 0.5F + 1.0F);
		
		var multiplier = 1.0F / (stretch + 1.0F);
		
		matrices.scale(multiplier * slimeSize, 1.0F / multiplier * slimeSize, multiplier * slimeSize);
	}
	
	@Override
	public Identifier getTexture(SpaceSlimeEntity slimeEntity) {
		return TEXTURE;
	}
}
