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

package com.github.mixinors.astromine.common.block.redstone;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
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
		    case WORK_WHEN_OFF -> {
			    return new TranslatableText("text.astromine.work_when_off").formatted(Formatting.RED);
		    }
		    case WORK_WHEN_ON -> {
			    return new TranslatableText("text.astromine.work_when_on").formatted(Formatting.GREEN);
		    }
		    case WORK_ALWAYS -> {
			    return new TranslatableText("text.astromine.work_always").formatted(Formatting.YELLOW);
		    }
		    default -> {
			    return null;
		    }
	    }
    }

    /** Returns the next type on this enum. */
    public RedstoneType getNext() {
	    switch (this) {
		    case WORK_ALWAYS -> {
			    return WORK_WHEN_OFF;
		    }
		    case WORK_WHEN_OFF -> {
			    return WORK_WHEN_ON;
		    }
		    default -> {
			    return WORK_ALWAYS;
		    }
	    }
	}
	
	public static RedstoneType fromString(String string) {
		return string.equals("WorkAlways") ? WORK_ALWAYS : string.equals("WorkWhenOn") ? WORK_WHEN_ON : string.equals("WorkWhenOff") ? WORK_WHEN_OFF : null;
	}
	
	@Override
	public String toString() {
		return this == WORK_ALWAYS ? "WorkAlways" : this == WORK_WHEN_ON ? "WorkWhenOn" : this == WORK_WHEN_OFF ? "WorkWhenOff" : null;
	}
}
