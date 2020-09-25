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

package com.github.chainmailstudios.astromine.common.widget.blade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.client.utilities.Scissors;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;

public class HorizontalArrowWidget extends AbstractWidget {
	private static final Identifier BACKGROUND = AstromineCommon.identifier("textures/widget/horizontal_arrow_background.png");
	private static final Identifier FOREGROUND = AstromineCommon.identifier("textures/widget/horizontal_arrow_foreground.png");

	private IntSupplier progressSupplier = () -> 0;
	private IntSupplier limitSupplier = () -> 100;

	@NotNull
	@Override
	public List<Text> getTooltip() {
		return Collections.singletonList(new LiteralText(progressSupplier.getAsInt() + "/" + limitSupplier.getAsInt()));
	}

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

	public void setProgressSupplier(IntSupplier progressSupplier) {
		this.progressSupplier = progressSupplier;
	}

	public void setLimitSupplier(IntSupplier limitSupplier) {
		this.limitSupplier = limitSupplier;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(MatrixStack matrices, VertexConsumerProvider provider) {
		if (getHidden()) {
			return;
		}

		float x = getPosition().getX();
		float y = getPosition().getY();

		float sX = getSize().getWidth();
		float sY = getSize().getHeight();

		float rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		float scale = (float) MinecraftClient.getInstance().getWindow().getScaleFactor();

		float sBGX = (int) (((sX / getLimit()) * getProgress()));

		RenderLayer backgroundLayer = Layers.get(getBackgroundTexture());
		RenderLayer foregroundLayer = Layers.get(getForegroundTexture());

		Scissors area = new Scissors(provider, (int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, backgroundLayer, getPosition().getX(), getPosition().getY(), getSize().getWidth(), getSize().getHeight(), getBackgroundTexture());

		area.destroy(provider);

		area = new Scissors(provider, (int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sBGX * scale), (int) (sY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, foregroundLayer, getPosition().getX(), getPosition().getY(), getSize().getWidth(), getSize().getHeight(), getForegroundTexture());

		area.destroy(provider);
	}
}
