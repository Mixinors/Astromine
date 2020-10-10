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

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.common.utilities.NumberUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.vini2003.blade.client.utilities.Instances;
import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.client.utilities.Scissors;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Supplier;

public class EnergyVerticalBarWidget extends AbstractWidget {
	private final Identifier ENERGY_BACKGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_background.png");
	private final Identifier ENERGY_FOREGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_foreground.png");

	private Supplier<EnergyVolume> volumeSupplier;

	@Environment(EnvType.CLIENT)
	@Override
	public @NotNull List<Text> getTooltip() {
		return Lists.newArrayList(new TranslatableText("text.astromine.energy"), new LiteralText(NumberUtilities.shorten(volumeSupplier.get().getAmount().longValue(), "") + "/" + NumberUtilities.shorten(volumeSupplier.get().getSize().longValue(), "")).formatted(Formatting.GRAY),
			new LiteralText("Astromine").formatted(Formatting.BLUE, Formatting.ITALIC));
	}

	public void setVolume(Supplier<EnergyVolume> volumeSupplier) {
		this.volumeSupplier = volumeSupplier;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider) {
		if (getHidden())
			return;

		float x = getPosition().getX();
		float y = getPosition().getY();

		float sX = getSize().getWidth();
		float sY = getSize().getHeight();

		float rawHeight = Instances.client().getWindow().getHeight();
		float scale = (float) Instances.client().getWindow().getScaleFactor();

		EnergyVolume volume = volumeSupplier.get();

		float sBGY = (float) (sY / volume.getSize() * volume.getAmount());

		Scissors area = new Scissors(provider, (int) (x * scale), (int) (rawHeight - (y + sY) * scale), (int) (sX * scale), (int) (sY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, Layers.get(ENERGY_BACKGROUND), x, y, sX, sY, ENERGY_BACKGROUND);

		area.destroy(provider);

		area = new Scissors(provider, (int) (x * scale), (int) (rawHeight - (y + sY) * scale), (int) (sX * scale), (int) (sBGY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, Layers.get(ENERGY_FOREGROUND), x, y, sX, sY, ENERGY_FOREGROUND);

		area.destroy(provider);
	}
}
