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

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import net.minecraft.text.TranslatableText;

import java.util.function.Supplier;

public class WBaseVolumeFractionalVerticalBar extends WFractionalVerticalBar {
	private Supplier<BaseVolume> volume;

	public WBaseVolumeFractionalVerticalBar() {
		super();

		setUnit(new TranslatableText("text.astromine.base"));
	}

	public BaseVolume getFluidVolume() {
		return volume.get();
	}

	public <W extends WFractionalVerticalBar> W setBaseVolume(Supplier<BaseVolume> volume) {
		setProgressFraction(volume.get()::getFraction);
		setLimitFraction(volume.get()::getSize);

		this.volume = volume;
		return (W) this;
	}
}
