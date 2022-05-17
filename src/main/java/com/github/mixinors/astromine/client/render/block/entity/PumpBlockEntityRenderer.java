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

package com.github.mixinors.astromine.client.render.block.entity;

import com.github.mixinors.astromine.common.block.entity.utility.PumpBlockEntity;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class PumpBlockEntityRenderer implements BlockEntityRenderer<PumpBlockEntity> {
	public PumpBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
	}
	
	@Override
	public void render(PumpBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay) {
		var lerpDepth = MathHelper.lerp(tickDelta / 100.0F, entity.prevDepth, entity.depth);
		entity.prevDepth = lerpDepth;
		
		var maxY = lerpDepth / 20.0D + 1.0F;
		
		var layer = AMRenderLayers.getPumpTube();
		var consumer = provider.getBuffer(layer);
		
		var prevY = 0.0F;
		
		var prevV = 1.0F - ((float) maxY - (int) maxY);
		
		for (var y = 1.0F; y < maxY; y += 1.0F) {
			var v = prevV + (float) Math.min(maxY - y, 1.0F);
			
			var posMatrix = matrices.peek().getPositionMatrix();
			var normMatrix = matrices.peek().getNormalMatrix();
			
			var dY = y;
			
			if (dY > maxY - 1.0F) {
				dY = (float) maxY - 1.0F;
			}
			
			consumer.vertex(posMatrix, (5.0F / 16.0F), -prevY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (10.0F / 16.0F), -prevY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (10.0F / 16.0F), -dY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (5.0F / 16.0F), -dY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			
			consumer.vertex(posMatrix, (5.0F / 16.0F), -prevY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (5.0F / 16.0F), -dY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (10.0F / 16.0F), -dY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (10.0F / 16.0F), -prevY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			
			consumer.vertex(posMatrix, (5.0F / 16.0F), -prevY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (5.0F / 16.0F), -dY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (5.0F / 16.0F), -dY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (5.0F / 16.0F), -prevY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			
			consumer.vertex(posMatrix, (10.0F / 16.0F), -dY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (10.0F / 16.0F), -prevY, (5.0F / 16.0F)).color(0xFFFFFFFF).texture(0.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (10.0F / 16.0F), -prevY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, prevV).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			consumer.vertex(posMatrix, (10.0F / 16.0F), -dY, (10.0F / 16.0F)).color(0xFFFFFFFF).texture(1.0F, v).overlay(overlay).light(light).normal(normMatrix, 0.0F, 0.0F, 0.0F).next();
			
			prevY = y;
			prevV = v;
		}
	}
	
	@Override
	public boolean rendersOutsideBoundingBox(PumpBlockEntity blockEntity) {
		return true;
	}
}
