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

package com.github.chainmailstudios.astromine.common.block.redstone;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public enum RedstoneType {
	WORK_WHEN_ON,
	WORK_WHEN_OFF,
	WORK_ALWAYS;

	public static RedstoneType byNumber(int number) {
		switch (number) {
			case 1: {
				return WORK_WHEN_ON;
			}

			case 2: {
				return WORK_ALWAYS;
			}

			default: {
				return WORK_WHEN_OFF;
			}
		}
	}

	public int asNumber() {
		switch (this) {

			case WORK_WHEN_ON: {
				return 1;
			}

			case WORK_ALWAYS: {
				return 2;
			}

			default: {
				return 0;
			}
		}
	}

	public TranslatableText asText() {
		switch (this) {
			case WORK_WHEN_OFF: {
				return (TranslatableText) new TranslatableText("text.astromine.work_when_off").formatted(Formatting.RED);
			}

			case WORK_WHEN_ON: {
				return (TranslatableText) new TranslatableText("text.astromine.work_when_on").formatted(Formatting.GREEN);
			}

			case WORK_ALWAYS: {
				return (TranslatableText) new TranslatableText("text.astromine.work_always").formatted(Formatting.YELLOW);
			}

			default: {
				return null;
			}
		}
	}

	public boolean shouldWork(boolean powered) {
		switch(this) {
			case WORK_WHEN_OFF: {
				return !powered;
			}

			case WORK_WHEN_ON: {
				return powered;
			}

			case WORK_ALWAYS: {
				return true;
			}

			default: {
				return false;
			}
		}
	}
}
