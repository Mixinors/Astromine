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

import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import spinnery.client.render.BaseRenderer;
import spinnery.client.utility.ScissorArea;
import spinnery.widget.WAbstractBar;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class WFloatingPointVerticalBar extends WAbstractBar {
	private Supplier<Double> progressFraction = () -> 0.0;
	private Supplier<Double> limitFraction = () -> Double.MAX_VALUE;

	private Text unit;

	public Supplier<Double> getProgressFraction() {
		return progressFraction;
	}

	public <W extends WFloatingPointVerticalBar> W setProgressFraction(Supplier<Double> progress) {
		this.progressFraction = progress;
		return (W) this;
	}

	public Supplier<Double> getLimitFraction() {
		return limitFraction;
	}

	public <W extends WFloatingPointVerticalBar> W setLimitFraction(Supplier<Double> limitFraction) {
		this.limitFraction = limitFraction;
		return (W) this;
	}

	public Text getUnit() {
		return unit;
	}

	public <W extends WFloatingPointVerticalBar> W setUnit(Text unit) {
		this.unit = unit;
		return (W) this;
	}

	public List<Text> getTooltip() {
		return Lists.newArrayList(FluidUtilities.rawFraction(progressFraction.get(), limitFraction.get(), unit),
			new TranslatableText("text.astromine.tooltip.fractional_value", EnergyUtilities.toDecimalString(progressFraction.get()), EnergyUtilities.toDecimalString(limitFraction.get())));
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

		float sBGY = (((sY / limitFraction.get().intValue()) * progressFraction.get().intValue()));

		ScissorArea scissorArea = new ScissorArea(provider, (int) (x * scale), (int) (rawHeight - ((y + sY - sBGY) * scale)), (int) (sX * scale), (int) ((sY - sBGY) * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), z, getWidth(), getHeight(), getBackgroundTexture());

		scissorArea.destroy(provider);

		scissorArea = new ScissorArea(provider, (int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sBGY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), z, getWidth(), getHeight(), getForegroundTexture());

		scissorArea.destroy(provider);

		super.draw(matrices, provider);
	}
}
