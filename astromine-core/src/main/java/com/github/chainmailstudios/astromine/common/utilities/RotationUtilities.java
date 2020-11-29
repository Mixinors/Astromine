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

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotationUtilities {
	/** Returns the given {@link AABB} rotated towards the specified {@link Direction}. */
	public static AABB getRotatedBoundingBox(AABB box, Direction facing) {
		box.move(-0.5, -0.5, -0.5);
		switch (facing) {
			case SOUTH:
				box = new AABB(box.minZ, box.minY, (box.maxX * -1) + 1, box.maxZ, box.maxY, (box.minX * -1) + 1);
			case WEST:
				box = new AABB((box.maxX * -1) + 1, box.minY, (box.maxZ * -1) + 1, (box.minX * -1) + 1, box.maxY, (box.minZ * -1) + 1);
			case EAST:
				box = new AABB((box.maxZ * -1) + 1, box.minY, box.minX, (box.minZ * -1) + 1, box.maxY, box.maxX);
			default:

		}
		box.move(0.5, 0.5, 0.5);
		return box;
	}

	/** Returns the given {@link AABB} rotated towards the specified {@link Direction} as a {@link VoxelShape}. */
	public static VoxelShape getRotatedShape(AABB def, Direction facing) {
		return Shapes.create(getRotatedBoundingBox(def, facing));
	}
}
