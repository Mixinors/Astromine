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
import com.github.mixinors.astromine.client.model.entity.SuperSpaceSlimeEntityModel;
import com.github.mixinors.astromine.client.render.entity.layer.SpaceSlimeGlassLayer;
import com.github.mixinors.astromine.common.entity.slime.SuperSpaceSlimeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SuperSpaceSlimeEntityRenderer extends MobRenderer<SuperSpaceSlimeEntity, SuperSpaceSlimeEntityModel> {
	private static final ResourceLocation TEXTURE = AMCommon.id("textures/entity/space_slime/space_slime.png");
	private static final ResourceLocation EXPLODING_TEXTURE = AMCommon.id("textures/entity/space_slime/space_slime_exploding.png");
	
	public SuperSpaceSlimeEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new SuperSpaceSlimeEntityModel(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
		
		this.addLayer(new SlimeOuterLayer(this, context.getModelSet()));
		this.addLayer(new SpaceSlimeGlassLayer<>(this, context.getItemRenderer()));
	}
	
	@Override
	public void render(SuperSpaceSlimeEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light) {
		this.shadowRadius = 2.5F;
		
		super.render(entity, yaw, tickDelta, matrices, provider, light);
	}
	
	@Override
	public void scale(SuperSpaceSlimeEntity slimeEntity, PoseStack matrices, float tickDelta) {
		var scale = 0.999F;
		
		matrices.scale(scale, scale, scale);
		
		matrices.translate(0.0D, -0.525D, 0.0D);
		
		var slimeSize = 10.0F;
		
		var stretch = Mth.lerp(tickDelta, slimeEntity.prevStretch, slimeEntity.stretch) / (slimeSize * 0.5F + 1.0F);
		
		var multiplier = 1.0F / (stretch + 1.0F);
		
		matrices.scale(multiplier * slimeSize, 1.0F / multiplier * slimeSize, multiplier * slimeSize);
		
		if (slimeEntity.isExploding()) {
			var explodingProgress = Mth.lerp(tickDelta, slimeEntity.prevExplodingProgress, slimeEntity.getExplodingProgress());
			var explodingScale = 1.0F + (float) Math.sin(explodingProgress / 5.0F) / 10.0F;
			
			matrices.scale(explodingScale, explodingScale, explodingScale);
		}
	}
	
	@Override
	public ResourceLocation getTextureLocation(SuperSpaceSlimeEntity slimeEntity) {
		return slimeEntity.isExploding() ? EXPLODING_TEXTURE : TEXTURE;
	}
}
