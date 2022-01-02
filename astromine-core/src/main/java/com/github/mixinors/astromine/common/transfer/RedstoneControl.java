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

package com.github.mixinors.astromine.common.transfer;

public enum RedstoneControl {
	WORK_WHEN_ON,
	WORK_WHEN_OFF,
	WORK_ALWAYS;

	public boolean shouldRun(boolean powered) {
		return switch (this) {
			case WORK_WHEN_ON -> powered;
			case WORK_WHEN_OFF -> !powered;
			case WORK_ALWAYS -> true;
		};
	}

	public RedstoneControl next() {
		return switch (this) {
			case WORK_WHEN_ON -> WORK_WHEN_OFF;
			case WORK_WHEN_OFF -> WORK_ALWAYS;
			case WORK_ALWAYS -> WORK_WHEN_ON;
		};
	}
	
	public RedstoneControl previous() {
		return switch (this) {
			case WORK_WHEN_ON -> WORK_ALWAYS;
			case WORK_ALWAYS -> WORK_WHEN_OFF;
			case WORK_WHEN_OFF -> WORK_WHEN_ON;
		};
	}
}
