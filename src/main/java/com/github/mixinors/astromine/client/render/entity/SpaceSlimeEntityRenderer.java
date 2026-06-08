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
import com.github.mixinors.astromine.client.model.entity.SpaceSlimeEntityModel;
import com.github.mixinors.astromine.client.render.entity.layer.SpaceSlimeGlassLayer;
import com.github.mixinors.astromine.common.entity.slime.SpaceSlimeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpaceSlimeEntityRenderer extends MobRenderer<SpaceSlimeEntity, SpaceSlimeEntityModel> {
	private static final ResourceLocation TEXTURE = AMCommon.id("textures/entity/space_slime/space_slime.png");
	
	public SpaceSlimeEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new SpaceSlimeEntityModel(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
		
		this.addLayer(new SlimeOuterLayer(this, context.getModelSet()));
		this.addLayer(new SpaceSlimeGlassLayer<>(this, context.getItemRenderer()));
	}
	
	@Override
	public void render(SpaceSlimeEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource provider, int i) {
		this.shadowRadius = 0.25F * (float) entity.getSize();
		
		// if the slime is floating, we rotate it around the x axis for 1 full rotation
		// todo: random axis rotation
		if (entity.isFloating()) {
			var floatingProgress = Mth.lerp(tickDelta, entity.prevFloatingProgress, entity.getFloatingProgress());
			
			matrices.mulPose(Axis.XP.rotationDegrees((floatingProgress / 200.0F) * 360.0F));
		}
		
		super.render(entity, yaw, tickDelta, matrices, provider, i);
	}
	
	@Override
	public void scale(SpaceSlimeEntity slimeEntity, PoseStack matrices, float tickDelta) {
		var scale = 0.999F;
		
		matrices.scale(scale, scale, scale);
		
		matrices.translate(0.0D, -0.125D, 0.0D);
		
		var slimeSize = (float) slimeEntity.getSize();
		
		var stretch = Mth.lerp(tickDelta, slimeEntity.oSquish, slimeEntity.squish) / (slimeSize * 0.5F + 1.0F);
		
		var multiplier = 1.0F / (stretch + 1.0F);
		
		matrices.scale(multiplier * slimeSize, 1.0F / multiplier * slimeSize, multiplier * slimeSize);
	}
	
	@Override
	public ResourceLocation getTextureLocation(SpaceSlimeEntity slimeEntity) {
		return TEXTURE;
	}
}
