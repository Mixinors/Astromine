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

package com.github.chainmailstudios.astromine.discoveries.client.model;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import com.github.chainmailstudios.astromine.discoveries.common.entity.SpaceSlimeEntity;

public class SpaceSlimeEntityModel extends SlimeEntityModel<SpaceSlimeEntity> {

	public SpaceSlimeEntityModel(int size) {
		super(size);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.getParts().forEach((modelPart) -> {
			modelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		});

		// translate & scale for glass outline
		matrices.translate(0, 1.25, 0);
		matrices.scale(1.25f, 1.25f, 1.25f);

		// render glass block
		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.GLASS), ModelTransformation.Mode.FIXED, light, overlay, matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers());

		// undo translation & scale
		matrices.scale(.75f, .75f, .75f);
		matrices.translate(0, -1.25, 0);
	}
}
