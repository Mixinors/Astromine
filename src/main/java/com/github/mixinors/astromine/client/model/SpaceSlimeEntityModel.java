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

package com.github.mixinors.astromine.client.model;

import com.github.mixinors.astromine.common.entity.slime.SpaceSlimeEntity;
import dev.vini2003.hammer.core.api.client.util.DrawingUtils;
import dev.vini2003.hammer.core.api.client.util.InstanceUtils;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SpaceSlimeEntityModel extends SlimeEntityModel<SpaceSlimeEntity> {
	public SpaceSlimeEntityModel(ModelPart root) {
		super(root);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.getPart().render(matrices, vertices, light, overlay, red, green, blue, alpha);
		
		matrices.push();
		
		// Translate and scale for the glass outline.
		matrices.translate(0.0F, 1.25F, 0.0F);
		matrices.scale(1.25F, 1.25F, 1.25F);
		
		DrawingUtils.getItemRenderer().renderItem(new ItemStack(Items.GLASS), ModelTransformation.Mode.FIXED, light, overlay, matrices, InstanceUtils.getClient().getBufferBuilders().getEffectVertexConsumers(), 0);
		
		matrices.pop();
	}
}
