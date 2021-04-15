/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
        switch (this) {
            case WORK_WHEN_OFF: {
                return new TranslatableText("text.astromine.work_when_off").formatted(Formatting.RED);
            }

            case WORK_WHEN_ON: {
                return new TranslatableText("text.astromine.work_when_on").formatted(Formatting.GREEN);
            }

            case WORK_ALWAYS: {
                return new TranslatableText("text.astromine.work_always").formatted(Formatting.YELLOW);
            }

            default: {
                return null;
            }
        }
    }

    /** Returns this type as a number. */
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

    /** Returns the type corresponding to the given number. */
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

    /** Returns the next type on this enum. */
    public RedstoneType next() {
        switch (this) {
            case WORK_ALWAYS: {
                return WORK_WHEN_OFF;
            }

            case WORK_WHEN_OFF: {
                return WORK_WHEN_ON;
            }

			default: {
				return WORK_ALWAYS;
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
