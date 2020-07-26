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
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.google.common.collect.Lists;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class WEnergyVolumeFractionalVerticalBar extends WFloatingPointVerticalBar {
	private Supplier<EnergyVolume> volume;

	private final Identifier ENERGY_BACKGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_background.png");
	private final Identifier ENERGY_FOREGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_foreground.png");

	public WEnergyVolumeFractionalVerticalBar() {
		setUnit(new TranslatableText("text.astromine.energy"));
	}

	@Override
	public Identifier getBackgroundTexture() {
		return ENERGY_BACKGROUND;
	}

	@Override
	public Identifier getForegroundTexture() {
		return ENERGY_FOREGROUND;
	}

	public EnergyVolume getEnergyVolume() {
		return volume.get();
	}

	@Override
	public List<Text> getTooltip() {
		return Lists.newArrayList(EnergyUtilities.compoundDisplayColored(getProgressFraction().get(), getLimitFraction().get()));
	}

	public <W extends WFloatingPointVerticalBar> W setEnergyVolume(Supplier<EnergyVolume> volume) {
		setProgressFraction(volume.get()::getAmount);
		setLimitFraction(volume.get()::getMaxAmount);

		this.volume = volume;
		return (W) this;
	}
}
