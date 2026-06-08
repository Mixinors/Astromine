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

package com.github.mixinors.astromine.common.item.utility;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public final class DrillMiningArea {
	private DrillMiningArea() {
	}

	public static List<BlockPos> positions(BlockPos origin, Direction face, int diameter) {
		var radius = Math.max(0, diameter / 2);
		var positions = new ArrayList<BlockPos>((radius * 2 + 1) * (radius * 2 + 1));
		var axis = face.getAxis();

		for (var ring = 0; ring <= radius; ++ring) {
			for (var first = -ring; first <= ring; ++first) {
				for (var second = -ring; second <= ring; ++second) {
					if (Math.max(Math.abs(first), Math.abs(second)) != ring) {
						continue;
					}

					positions.add(offset(origin, axis, first, second));
				}
			}
		}

		return positions;
	}

	private static BlockPos offset(BlockPos origin, Direction.Axis axis, int first, int second) {
		return switch (axis) {
			case X -> origin.offset(0, first, second);
			case Y -> origin.offset(first, 0, second);
			case Z -> origin.offset(first, second, 0);
		};
	}
}
