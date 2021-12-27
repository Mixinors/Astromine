/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.client.model;

import com.github.mixinors.astromine.client.render.entity.PrimitiveRocketEntityRenderer;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;

import com.github.mixinors.astromine.common.entity.PrimitiveRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

public class PrimitiveRocketEntityModel extends SinglePartEntityModel<PrimitiveRocketEntity> {
	private final ModelPart root;

	public PrimitiveRocketEntityModel(ModelPart root) {
		this.root = root;
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild("rocket", ModelPartBuilder.create().uv(48, 0).cuboid(-6.0F, -11.0F, -6.0F, 12, 11, 12), ModelTransform.NONE);
		modelPartData.addChild("rocket1", ModelPartBuilder.create().uv(78, 84).cuboid(-5.0F, -18.0F, -5.0F, 10, 7, 10), ModelTransform.NONE);
		modelPartData.addChild("rocket2", ModelPartBuilder.create().uv(84, 0).cuboid(-4.0F, -21.0F, -4.0F, 8, 3, 8), ModelTransform.NONE);
		modelPartData.addChild("rocket3", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -106.0F, -6.0F, 12, 86, 12), ModelTransform.NONE);
		modelPartData.addChild("rocket4", ModelPartBuilder.create().uv(48, 48).cuboid(-5.0F, -142.0F, -5.0F, 10, 36, 10), ModelTransform.NONE);
		modelPartData.addChild("rocket5", ModelPartBuilder.create().uv(78, 23).cuboid(-4.0F, -161.0F, -4.0F, 8, 19, 8), ModelTransform.NONE);
		modelPartData.addChild("rocket6", ModelPartBuilder.create().uv(0, 98).cuboid(-2.0F, -180.0F, -2.0F, 4, 19, 4), ModelTransform.NONE);
		modelPartData.addChild("rocket7", ModelPartBuilder.create().uv(88, 50).cuboid(-1.0F, -33.0F, 6.0F, 2, 15, 9), ModelTransform.NONE);
		modelPartData.addChild("rocket8", ModelPartBuilder.create().uv(48, 23).cuboid(-1.0F, -33.0F, -15.0F, 2, 15, 9), ModelTransform.NONE);
		modelPartData.addChild("rocket9", ModelPartBuilder.create().uv(70, 101).cuboid(-1.0F, -48.0F, 6.0F, 2, 15, 5), ModelTransform.NONE);
		modelPartData.addChild("rocket10", ModelPartBuilder.create().uv(84, 101).cuboid(-1.0F, -48.0F, -11.0F, 2, 15, 5), ModelTransform.NONE);
		modelPartData.addChild("rocket11", ModelPartBuilder.create().uv(16, 98).cuboid(-15.0F, -33.0F, -1.0F, 9, 15, 2), ModelTransform.NONE);
		modelPartData.addChild("rocket12", ModelPartBuilder.create().uv(48, 94).cuboid(6.0F, -33.0F, -1.0F, 9, 15, 2), ModelTransform.NONE);
		modelPartData.addChild("rocket13", ModelPartBuilder.create().uv(98, 101).cuboid(-11.0F, -48.0F, -1.0F, 5, 15, 2), ModelTransform.NONE);
		modelPartData.addChild("rocket14", ModelPartBuilder.create().uv(101, 11).cuboid(6.0F, -48.0F, -1.0F, 5, 15, 2), ModelTransform.NONE);

		return TexturedModelData.of(modelData, 128, 128);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}

	@Override
	public void setAngles(PrimitiveRocketEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}

	public static void renderItem(PrimitiveRocketEntityModel primitiveRocketEntityModel, ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		matrices.push();
		
		if (mode == ModelTransformation.Mode.GUI) {
			matrices.translate(0.66F, 0.22F, 0F);
		}
		
		matrices.scale(1.0F, -1.0F, -1.0F);
		
		if (mode == ModelTransformation.Mode.GUI) {
			matrices.scale(0.09F, 0.09F, 0.09F);
		} else {
			matrices.scale(0.3F, 0.3F, 0.3F);
		}
		
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45));

		VertexConsumer glintConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, primitiveRocketEntityModel.getLayer(PrimitiveRocketEntityRenderer.ID), false, stack.hasGlint());
		primitiveRocketEntityModel.render(matrices, glintConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
}
