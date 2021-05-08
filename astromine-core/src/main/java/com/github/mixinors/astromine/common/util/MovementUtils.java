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

package com.github.mixinors.astromine.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MovementUtils {
	/** Pushes the specified {@link Entity} per the given values. */
	public static void pushEntity(Entity entity, BlockPos pos, float speed, Direction facing) {
		pushEntity(entity, pos, speed, facing, true);
	}

	/** Pushes the specified {@link Entity} per the given values. */
	public static void pushEntity(Entity entity, BlockPos pos, float speed, Direction facing, boolean shouldCenter) {
		double motionX = entity.getVelocity().getX();
		double motionZ = entity.getVelocity().getZ();

		if (speed * facing.getOffsetX() > 0 && motionX < speed) {
			entity.addVelocity(speed / 2, 0, 0);
		} else if (speed * facing.getOffsetX() < 0 && motionX > -speed) {
			entity.addVelocity(-speed / 2, 0, 0);
		}

		if (speed * facing.getOffsetZ() > 0 && motionZ < speed) {
			entity.addVelocity(0, 0, speed / 2);
		} else if (speed * facing.getOffsetZ() < 0 && motionZ > -speed) {
			entity.addVelocity(0, 0, -speed / 2);
		}

		if (shouldCenter) {
			centerEntity(entity, pos, speed, facing);
		}
	}

	/** Centers the specified {@link Entity} on the X and Z axis per the given values. */
	private static void centerEntity(Entity entity, BlockPos pos, float speed, Direction facing) {
		if (speed * facing.getOffsetX() > 0 || speed * facing.getOffsetX() < 0) {
			centerZ(entity, pos);
		}

		if (speed * facing.getOffsetZ() > 0 || speed * facing.getOffsetZ() < 0) {
			centerX(entity, pos);
		}
	}

	/** Centers the specified {@link Entity} on the Z axis per the given values. */
	private static void centerZ(Entity entity, BlockPos pos) {
		if (entity.getZ() > pos.getZ() + .55) {
			entity.addVelocity(0, 0, -0.1F);
		} else if (entity.getZ() < pos.getZ() + .45) {
			entity.addVelocity(0, 0, 0.1F);
		} else {
			entity.setVelocity(entity.getVelocity().getX(), entity.getVelocity().getY(), 0);
		}
	}

	/** Centers the specified {@link Entity} on the X axis per the given values. */
	private static void centerX(Entity entity, BlockPos pos) {
		if (entity.getX() > pos.getX() + .55) {
			entity.addVelocity(-0.1F, 0, 0);
		} else if (entity.getX() < pos.getX() + .45) {
			entity.addVelocity(0.1F, 0, 0);
		} else {
			entity.setVelocity(0, entity.getVelocity().getY(), entity.getVelocity().getZ());
		}
	}
}
