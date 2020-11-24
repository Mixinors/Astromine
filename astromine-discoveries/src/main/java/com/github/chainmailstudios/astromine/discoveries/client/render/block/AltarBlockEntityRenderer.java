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

package com.github.chainmailstudios.astromine.discoveries.client.render.block;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.discoveries.common.block.entity.AltarBlockEntity;

import java.util.Random;

public class AltarBlockEntityRenderer extends BlockEntityRenderer<AltarBlockEntity> {
	private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
	private final Random random = new Random();

	public AltarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(AltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.craftingTicks > 0)
			entity.craftingTicksDelta = entity.craftingTicks + tickDelta;
		matrices.push();
		ItemStack stack = entity.getItemComponent().getFirst();
		int j = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
		this.random.setSeed(j);
		BakedModel bakedModel = this.itemRenderer.getHeldItemModel(stack, entity.getWorld(), null);
		boolean bl = bakedModel.hasDepth();
		int k = 1;
		float h = 0.25F;
		float l = AltarBlockEntity.HOVER_HEIGHT + 0.1F;
		float m = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
		matrices.translate(0.5D, l + 1.0D + 0.25D * m, 0.5D);
		double progress;
		float n = getHeight(entity, tickDelta);
		if (entity.craftingTicks > 0) {
			progress = entity.getYProgress(entity.craftingTicksDelta);
			BlockPos pos = entity.getPos();
			BlockPos parentPos = entity.getPos();
			matrices.translate(0, AltarBlockEntity.HEIGHT_OFFSET * progress, 0);

			n = (entity.spinAge + tickDelta * entity.lastAgeAddition) / 20.0F + AltarBlockEntity.HOVER_HEIGHT;
		}
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(n));
		float o = bakedModel.getTransformation().ground.scale.getX();
		float p = bakedModel.getTransformation().ground.scale.getY();
		float q = bakedModel.getTransformation().ground.scale.getZ();
		float v;
		float w;
		if (!bl) {
			float r = -0.0F * (float) (k - 1) * 0.5F * o;
			v = -0.0F * (float) (k - 1) * 0.5F * p;
			w = -0.09375F * (float) (k - 1) * 0.5F * q;
			matrices.translate(r, v, w);
		}

		for (int u = 0; u < k; ++u) {
			matrices.push();
			if (u > 0) {
				if (bl) {
					v = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					w = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					matrices.translate(v, w, x);
				} else {
					v = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
					w = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
					matrices.translate(v, w, 0.0D);
				}
			}

			this.itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, bakedModel);
			matrices.pop();
			if (!bl) {
				matrices.translate(0.0F * o, 0.0F * p, 0.09375F * q);
			}
		}

		matrices.pop();
	}

	public float getHeight(AltarBlockEntity entity, float tickDelta) {
		return (entity.spinAge + tickDelta) / 20.0F + AltarBlockEntity.HOVER_HEIGHT;
	}
}
