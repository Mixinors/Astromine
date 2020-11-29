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

package com.github.chainmailstudios.astromine.transportations.client.render.block;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.transportations.client.model.InserterArmModel;
import com.github.chainmailstudios.astromine.transportations.common.block.InserterBlock;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.InserterBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

import static java.lang.Math.*;

public class InserterBlockEntityRenderer extends BlockEntityRenderer<InserterBlockEntity> implements ConveyorRenderer<InserterBlockEntity> {
	public InserterBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(InserterBlockEntity blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		String type = ((InserterBlock) blockEntity.getBlockState().getBlock()).getType();
		int speed = ((InserterBlock) blockEntity.getBlockState().getBlock()).getSpeed();
		InserterArmModel modelInserterArm = new InserterArmModel();

		float position = blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * tickDelta;

		matrices.pushPose();
		matrices.translate(0.5, 1.5, 0.5);
		matrices.mulPose(Vector3f.XP.rotationDegrees(180.0F));
		if (direction == Direction.SOUTH) {
			matrices.mulPose(Vector3f.YP.rotationDegrees(180));
		} else if (direction == Direction.EAST) {
			matrices.mulPose(Vector3f.YP.rotationDegrees(90));
		} else if (direction == Direction.WEST) {
			matrices.mulPose(Vector3f.YP.rotationDegrees(-90));
		}

		modelInserterArm.getLowerArm().yRot = (float) Math.toRadians((position / speed) * 180F);

		if (position < speed / 4) {
			float grabPosition = position;
			modelInserterArm.getLowerArm().xRot = (float) Math.toRadians((grabPosition / (speed / 4)) * -30F + 40F);
			modelInserterArm.getMiddleArm().xRot = (float) Math.toRadians((grabPosition / (speed / 4)) * -20F + 40F);
		} else if (position >= speed - (speed / 4) && position < speed) {
			modelInserterArm.getLowerArm().xRot = (float) Math.toRadians((position / speed) * 120F - 80F);
			modelInserterArm.getMiddleArm().xRot = (float) Math.toRadians((position / speed) * 80F - 40F);
		} else {
			modelInserterArm.getLowerArm().xRot = (float) Math.toRadians(10);
			modelInserterArm.getMiddleArm().xRot = (float) Math.toRadians(20);
		}

		modelInserterArm.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.entitySolid(new ResourceLocation(AstromineCommon.MOD_ID + ":textures/block/" + type + "_inserter.png"))), light, overlay, 1, 1, 1, 1);
		matrices.popPose();

		if (!blockEntity.getItemComponent().isEmpty()) {
			matrices.pushPose();
			matrices.translate(0.5, 0, 0.5);
			if (direction == Direction.NORTH) {
				matrices.mulPose(Vector3f.YP.rotationDegrees(180));
			} else if (direction == Direction.EAST) {
				matrices.mulPose(Vector3f.YP.rotationDegrees(90));
			} else if (direction == Direction.WEST) {
				matrices.mulPose(Vector3f.YP.rotationDegrees(-90));
			}

			float lowArmSize = 8 / 16F;
			float midArmSize = 10 / 16F;
			float connectingAngle = modelInserterArm.getMiddleArm().xRot;

			float distance = (float) Math.sqrt(Math.pow(lowArmSize, 2) + Math.pow(midArmSize, 2) - 2 * lowArmSize * midArmSize * Math.cos(connectingAngle));
			float angle = (float) (180 - Math.toDegrees(modelInserterArm.getLowerArm().xRot + modelInserterArm.getMiddleArm().xRot));

			matrices.mulPose(Vector3f.YN.rotationDegrees((float) Math.toDegrees(modelInserterArm.getLowerArm().yRot)));
			matrices.mulPose(Vector3f.XP.rotationDegrees(180 + angle));
			matrices.translate(0, (1 + 5 / 16F) - distance, distance - (1F / 16F));
			matrices.scale(0.3F, 0.3F, 0.3F);

			renderItem(blockEntity, blockEntity.getItemComponent().getFirst(), matrices, vertexConsumers);
			matrices.popPose();
		}
	}
}
