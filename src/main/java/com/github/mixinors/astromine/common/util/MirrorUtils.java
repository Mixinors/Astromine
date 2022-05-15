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

package com.github.mixinors.astromine.common.util;

import net.minecraft.util.math.Direction;

import static net.minecraft.util.math.Direction.*;

public class MirrorUtils {
	public static Direction rotate(Direction origin, Direction rotation) {
		switch (rotation) {
			case NORTH -> {
				return switch (origin) {
					case NORTH -> NORTH;
					case SOUTH -> SOUTH;
					case WEST -> WEST;
					case EAST -> EAST;
					default -> origin;
				};
			}
			case SOUTH -> {
				return switch (origin) {
					case NORTH -> SOUTH;
					case SOUTH -> NORTH;
					case WEST -> EAST;
					case EAST -> WEST;
					default -> origin;
				};
			}
			case WEST -> {
				return switch (origin) {
					case NORTH -> EAST;
					case SOUTH -> WEST;
					case EAST -> SOUTH;
					case WEST -> NORTH;
					default -> origin;
				};
			}
			case EAST -> {
				return switch (origin) {
					case NORTH -> WEST;
					case SOUTH -> EAST;
					case EAST -> NORTH;
					case WEST -> SOUTH;
					default -> origin;
				};
			}
			default -> {
				return origin; /* TODO: {@link Direction#UP} and {@link Direction#DOWN}. */
			}
		}
	}
}
