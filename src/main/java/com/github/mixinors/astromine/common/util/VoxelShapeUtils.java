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

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Collection;

/**
 * Originally from {@see https://www.youtube.com/watch?v=I9Qn_oIx6Oo}, adapted to Fabric.
 */
public class VoxelShapeUtils {
	private static final double CENTER = 0.5;
	
	private static final double NINETY_DEGREES = Math.toRadians(90);
	private static final double ONE_HUNDRED_EIGHTY_DEGREES = Math.toRadians(180);
	private static final double TWO_HUNDRED_SEVENTY_DEGREES = Math.toRadians(270);
	
	public static VoxelShape union(VoxelShape... shapes) {
		return union(Lists.newArrayList(shapes));
	}
	
	public static VoxelShape union(Collection<VoxelShape> shapes) {
		var collision = VoxelShapes.empty();
		
		for (var shape : shapes) {
			collision = VoxelShapes.union(shape, collision);
		}
		
		return collision;
	}
	
	public static VoxelShape rotate(Direction.Axis axis, double radians, VoxelShape shape) {
		var collision = VoxelShapes.empty();
		
		for (var box : shape.getBoundingBoxes()) {
			var min = axis == Direction.Axis.X ? rotatePoint(box.minY, box.minZ, radians) : (axis == Direction.Axis.Z ? rotatePoint(box.minX, box.minY, radians) : rotatePoint(box.minX, box.minZ, radians));
			var max = axis == Direction.Axis.X ? rotatePoint(box.maxY, box.maxZ, radians) : (axis == Direction.Axis.Z ? rotatePoint(box.maxX, box.maxY, radians) : rotatePoint(box.maxX, box.maxZ, radians));
			
			collision = VoxelShapes.union(collision, axis == Direction.Axis.X ? VoxelShapes.cuboid(box.minX, min.getFirst(), min.getSecond(), box.maxX, max.getFirst(), max.getSecond()) : (axis == Direction.Axis.Z ? VoxelShapes.cuboid(min.getFirst(), min.getSecond(), box.minZ, max.getFirst(), max.getSecond(), box.maxZ) : VoxelShapes.cuboid(Math.min(min.getFirst(), max.getFirst()), Math.min(box.minY, box.maxY), Math.min(min.getSecond(), max.getSecond()), Math.max(min.getFirst(), max.getFirst()), Math.max(box.minY, box.maxY), Math.max(min.getSecond(), max.getSecond()))));
		}
		return collision;
	}
	
	public static VoxelShape rotate(Direction.Axis axis, double radians, Collection<VoxelShape> shapes) {
		var collision = VoxelShapes.empty();
		for (var shape : shapes) {
			collision = VoxelShapes.union(collision, rotate(axis, radians, shape));
		}
		return collision;
	}
	
	public static VoxelShape rotate(Direction.Axis axis, double radians, VoxelShape... shapes) {
		return rotate(axis, radians, Lists.newArrayList(shapes));
	}
	
	public static VoxelShape rotate(Direction direction, VoxelShape shape) {
		if (direction == Direction.EAST) {
			return rotateNinety(Direction.Axis.Y, shape);
		} else if (direction == Direction.SOUTH) {
			return rotateOneHundredAndEighty(Direction.Axis.Y, shape);
		} else if (direction == Direction.WEST) {
			return rotateTwoHundredAndSeventy(Direction.Axis.Y, shape);
		}
		
		return shape;
	}
	
	private static Pair<Double, Double> rotatePoint(double p1, double p2, double rotation) {
		return rotatePoint(p1, p2, rotation, CENTER);
	}
	
	private static Pair<Double, Double> rotatePoint(double p1, double p2, double rotation, double center) {
		return Pair.of(((p1 - center) * Math.cos(rotation) - ((p2 - center) * Math.sin(rotation))) + center, ((p1 - center) * Math.sin(rotation)) + ((p2 - center) * Math.cos(rotation)) + center);
	}
	
	public static VoxelShape rotateNinety(Direction.Axis axis, Collection<VoxelShape> shapes) {
		return rotate(axis, NINETY_DEGREES, shapes);
	}
	
	public static VoxelShape rotateNinety(Direction.Axis axis, VoxelShape... shapes) {
		return rotate(axis, NINETY_DEGREES, shapes);
	}
	
	public static VoxelShape rotateOneHundredAndEighty(Direction.Axis axis, Collection<VoxelShape> shapes) {
		return rotate(axis, ONE_HUNDRED_EIGHTY_DEGREES, shapes);
	}
	
	public static VoxelShape rotateOneHundredAndEighty(Direction.Axis axis, VoxelShape... shapes) {
		return rotate(axis, ONE_HUNDRED_EIGHTY_DEGREES, shapes);
	}
	
	public static VoxelShape rotateTwoHundredAndSeventy(Direction.Axis axis, Collection<VoxelShape> shapes) {
		return rotate(axis, TWO_HUNDRED_SEVENTY_DEGREES, shapes);
	}
	
	public static VoxelShape rotateTwoHundredAndSeventy(Direction.Axis axis, VoxelShape... shapes) {
		return rotate(axis, TWO_HUNDRED_SEVENTY_DEGREES, shapes);
	}
}
