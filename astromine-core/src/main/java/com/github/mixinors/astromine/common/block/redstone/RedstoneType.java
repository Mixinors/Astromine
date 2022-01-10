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

package com.github.mixinors.astromine.common.block.redstone;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

/**
 * An enum representing redstone behavior.
 */
public enum RedstoneType {
	WORK_WHEN_ON,
	WORK_WHEN_OFF,
	WORK_ALWAYS;

    /** Returns this type as a {@link Text}. */
    public Text asText() {
		return switch (this) {
			case WORK_WHEN_OFF -> new TranslatableText("text.astromine.work_when_off").formatted(Formatting.RED);
			case WORK_WHEN_ON -> new TranslatableText("text.astromine.work_when_on").formatted(Formatting.GREEN);
			case WORK_ALWAYS -> new TranslatableText("text.astromine.work_always").formatted(Formatting.YELLOW);
		};
    }

    /** Returns this type as a number. */
    public int asNumber() {
		return switch (this) {
			case WORK_WHEN_ON -> 1;
			case WORK_ALWAYS -> 2;
			default -> 0;
		};
	}

    /** Returns the type corresponding to the given number. */
    public static RedstoneType byNumber(int number) {
		return switch (number) {
			case 1 -> WORK_WHEN_ON;
			case 2 -> WORK_ALWAYS;
			default -> WORK_WHEN_OFF;
		};
    }

    /** Returns the next type on this enum. */
    public RedstoneType next() {
		return switch (this) {
			case WORK_ALWAYS -> WORK_WHEN_OFF;
			case WORK_WHEN_OFF -> WORK_WHEN_ON;
			default -> WORK_ALWAYS;
		};
	}

	public boolean shouldWork(boolean powered) {
		return switch(this) {
			case WORK_WHEN_OFF -> !powered;
			case WORK_WHEN_ON -> powered;
			case WORK_ALWAYS -> true;
		};
	}
}
