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

package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.util.math.Direction;

public class MirrorUtilities {
	public static Direction rotate(Direction origin, Direction rotation) {
		if (rotation == Direction.NORTH) {
			switch (origin) {
				case NORTH:
					return Direction.NORTH;
				case SOUTH:
					return Direction.SOUTH;
				case WEST:
					return Direction.WEST;
				case EAST:
					return Direction.EAST;
				default:
					return origin;
			}
		} else if (rotation == Direction.SOUTH) {
			switch (origin) {
				case NORTH:
					return Direction.SOUTH;
				case SOUTH:
					return Direction.NORTH;
				case WEST:
					return Direction.EAST;
				case EAST:
					return Direction.WEST;
				default:
					return origin;
			}
		} else if (rotation == Direction.WEST) {
			switch (origin) {
				case NORTH:
					return Direction.EAST;
				case SOUTH:
					return Direction.WEST;
				case EAST:
					return Direction.SOUTH;
				case WEST:
					return Direction.NORTH;
				default:
					return origin;
			}
		} else if (rotation == Direction.EAST) {
			switch (origin) {
				case NORTH:
					return Direction.WEST;
				case SOUTH:
					return Direction.EAST;
				case EAST:
					return Direction.NORTH;
				case WEST:
					return Direction.SOUTH;
				default:
					return origin;
			}
		} else {
			/* TODO: {@link Direction#UP} and {@link Direction#DOWN}. */
			return origin;
		}
	}
}
