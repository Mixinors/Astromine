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

package com.github.mixinors.astromine.techreborn.common.util;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class RotationUtils {
	/** Returns the given {@link Box} rotated towards the specified {@link Direction}. */
	public static Box getRotatedBoundingBox(Box box, Direction facing) {
		box.offset(-0.5, -0.5, -0.5);
		
		switch (facing) {
			case SOUTH:
				box = new Box(box.minZ, box.minY, (box.maxX * -1) + 1, box.maxZ, box.maxY, (box.minX * -1) + 1);
				break;
			case WEST:
				box = new Box((box.maxX * -1) + 1, box.minY, (box.maxZ * -1) + 1, (box.minX * -1) + 1, box.maxY, (box.minZ * -1) + 1);
				break;
			case EAST:
				box = new Box((box.maxZ * -1) + 1, box.minY, box.minX, (box.minZ * -1) + 1, box.maxY, box.maxX);
				break;
			default:
				break;
		}
		
		box.offset(0.5, 0.5, 0.5);
		
		return box;
	}

	/** Returns the given {@link Box} rotated towards the specified {@link Direction} as a {@link VoxelShape}. */
	public static VoxelShape getRotatedShape(Box def, Direction facing) {
		return VoxelShapes.cuboid(getRotatedBoundingBox(def, facing));
	}
}
