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

package com.github.mixinors.astromine.common.transfer;

/**
 * A {@link RedstoneType} which dictates in which Redstone power states a machine should run.
 */
public enum RedstoneType {
	/**
	 * The machine will run when powered by Redstone.
	 */
	WORK_WHEN_ON,
	/**
	 * The machine will run when <b>not</b> powered by Redstone.
	 */
	WORK_WHEN_OFF,
	/**
	 * The machine will run whether powered <b>or not</b> by Redstone.
	 */
	WORK_ALWAYS;
	
	/**
	 * Returns whether the given Redstone state allows for machines to run in this control.
	 *
	 * @param powered the state.
	 */
	public boolean shouldRun(boolean powered) {
		return switch (this) {
			case WORK_WHEN_ON -> powered;
			case WORK_WHEN_OFF -> !powered;
			case WORK_ALWAYS -> true;
		};
	}
	
	public RedstoneType next() {
		return switch (this) {
			case WORK_WHEN_ON -> WORK_WHEN_OFF;
			case WORK_WHEN_OFF -> WORK_ALWAYS;
			case WORK_ALWAYS -> WORK_WHEN_ON;
		};
	}
	
	public RedstoneType previous() {
		return switch (this) {
			case WORK_WHEN_ON -> WORK_ALWAYS;
			case WORK_ALWAYS -> WORK_WHEN_OFF;
			case WORK_WHEN_OFF -> WORK_WHEN_ON;
		};
	}
}
