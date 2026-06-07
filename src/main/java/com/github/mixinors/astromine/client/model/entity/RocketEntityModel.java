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

package com.github.mixinors.astromine.client.model.entity;

import com.github.mixinors.astromine.client.render.entity.RocketEntityRenderer;
import com.github.mixinors.astromine.common.entity.rocket.RocketEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RocketEntityModel extends HierarchicalModel<RocketEntity> {
	private final ModelPart root;
	
	public RocketEntityModel(ModelPart root) {
		this.root = root;
	}
	
	public static LayerDefinition getTexturedModelData() {
		var modelData = new MeshDefinition();
		var modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild("rocket", CubeListBuilder.create().texOffs(48, 0).addBox(-6.0F, -11.0F, -6.0F, 12, 11, 12), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket1", CubeListBuilder.create().texOffs(78, 84).addBox(-5.0F, -18.0F, -5.0F, 10, 7, 10), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket2", CubeListBuilder.create().texOffs(84, 0).addBox(-4.0F, -21.0F, -4.0F, 8, 3, 8), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket3", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -106.0F, -6.0F, 12, 86, 12), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket4", CubeListBuilder.create().texOffs(48, 48).addBox(-5.0F, -142.0F, -5.0F, 10, 36, 10), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket5", CubeListBuilder.create().texOffs(78, 23).addBox(-4.0F, -161.0F, -4.0F, 8, 19, 8), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket6", CubeListBuilder.create().texOffs(0, 98).addBox(-2.0F, -180.0F, -2.0F, 4, 19, 4), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket7", CubeListBuilder.create().texOffs(88, 50).addBox(-1.0F, -33.0F, 6.0F, 2, 15, 9), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket8", CubeListBuilder.create().texOffs(48, 23).addBox(-1.0F, -33.0F, -15.0F, 2, 15, 9), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket9", CubeListBuilder.create().texOffs(70, 101).addBox(-1.0F, -48.0F, 6.0F, 2, 15, 5), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket10", CubeListBuilder.create().texOffs(84, 101).addBox(-1.0F, -48.0F, -11.0F, 2, 15, 5), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket11", CubeListBuilder.create().texOffs(16, 98).addBox(-15.0F, -33.0F, -1.0F, 9, 15, 2), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket12", CubeListBuilder.create().texOffs(48, 94).addBox(6.0F, -33.0F, -1.0F, 9, 15, 2), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket13", CubeListBuilder.create().texOffs(98, 101).addBox(-11.0F, -48.0F, -1.0F, 5, 15, 2), PartPose.ZERO);
		modelPartData.addOrReplaceChild("rocket14", CubeListBuilder.create().texOffs(101, 11).addBox(6.0F, -48.0F, -1.0F, 5, 15, 2), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
	
	@Override
	public void setupAnim(RocketEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	
	}
	
	@Override
	public ModelPart root() {
		return this.root;
	}
	
	public static void renderItem(RocketEntityModel rocketEntityModel, ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, int j) {
		matrices.pushPose();
		
		if (mode == ItemDisplayContext.GUI) {
			matrices.translate(0.66F, 0.22F, 0F);
		}
		
		matrices.scale(1.0F, -1.0F, -1.0F);
		
		if (mode == ItemDisplayContext.GUI) {
			matrices.scale(0.09F, 0.09F, 0.09F);
		} else {
			matrices.scale(0.3F, 0.3F, 0.3F);
		}
		
		matrices.mulPose(Axis.YP.rotationDegrees(45));
		matrices.mulPose(Axis.XP.rotationDegrees(45));
		
		var glintConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumerProvider, rocketEntityModel.renderType(RocketEntityRenderer.ID), false, stack.hasFoil());
		
		rocketEntityModel.renderToBuffer(matrices, glintConsumer, i, j, 0xFFFFFFFF);
		
		matrices.popPose();
	}
}
