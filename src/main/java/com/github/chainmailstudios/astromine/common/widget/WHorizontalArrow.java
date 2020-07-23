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
package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.layer.SpinneryLayers;
import spinnery.client.utility.ScissorArea;
import spinnery.widget.WAbstractWidget;

import java.util.function.IntSupplier;

public class WHorizontalArrow extends WAbstractWidget {
	private static final Identifier BACKGROUND = AstromineCommon.identifier("textures/widget/horizontal_arrow_background.png");
	private static final Identifier FOREGROUND = AstromineCommon.identifier("textures/widget/horizontal_arrow_foreground.png");

	private IntSupplier progressSupplier = () -> 0;
	private IntSupplier limitSupplier = () -> 100;

	public Identifier getBackgroundTexture() {
		return BACKGROUND;
	}

	public Identifier getForegroundTexture() {
		return FOREGROUND;
	}

	public int getProgress() {
		return progressSupplier.getAsInt();
	}

	public int getLimit() {
		return limitSupplier.getAsInt();
	}

	public <W extends WHorizontalArrow> W setProgressSupplier(IntSupplier progressSupplier) {
		this.progressSupplier = progressSupplier;
		return (W) this;
	}

	public <W extends WHorizontalArrow> W setLimitSupplier(IntSupplier limitSupplier) {
		this.limitSupplier = limitSupplier;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		float rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		float scale = (float) MinecraftClient.getInstance().getWindow().getScaleFactor();

		float sBGX = (int) (((sX / getLimit()) * getProgress()));

		RenderLayer backgroundLayer = SpinneryLayers.get(getBackgroundTexture());
		RenderLayer foregroundLayer = SpinneryLayers.get(getForegroundTexture());

		ScissorArea area = new ScissorArea(provider, (int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, backgroundLayer, getX(), getY(), z, getWidth(), getHeight(), getBackgroundTexture());

		area.destroy(provider);

		area = new ScissorArea(provider, (int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sBGX * scale), (int) (sY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, foregroundLayer, getX(), getY(), z, getWidth(), getHeight(), getForegroundTexture());

		area.destroy(provider);

		super.draw(matrices, provider);
	}
}
