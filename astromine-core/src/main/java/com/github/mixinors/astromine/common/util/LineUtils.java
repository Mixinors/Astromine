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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.math.Vec3f;

public class LineUtils {
	/**
	 * Returns points of a Bezier curve between the three given points with the specified amount of segments.
	 */
	public static Collection<Vec3f> getBezierSegments(Vec3f posA, Vec3f posB, Vec3f posC, float segments) {
		ArrayList<Vec3f> positions = new ArrayList<>();

		double x1 = posA.getX();
		double y1 = posA.getY();
		double z1 = posA.getZ();

		double x3 = posB.getX();
		double y3 = posB.getY();
		double z3 = posB.getZ();

		double x2 = posC.getX();
		double y2 = posC.getY();

		double dZ = (z3 - z1) / segments;
		double cZ = 0;

		for (double t = 0; t < 1; t += (1f / segments)) {
			double p0M = Math.pow(1 - t, 2);
			double p0X = x1 * p0M;
			double p0Y = y1 * p0M;

			double p1M = 2 * t * (1 - t);
			double p1X = p1M * x2;
			double p1Y = p1M * y2;

			double p2M = Math.pow(t, 2);
			double p2X = p2M * x3;
			double p2Y = p2M * y3;

			double pX = p0X + p1X + p2X;
			double pY = p0Y + p1Y + p2Y;

			if (t == 0) {
				positions.add(new Vec3f((float) x1, (float) y1, (float) z1));
			} else if (t + (1f / segments) >= 1) {
				positions.add(new Vec3f((float) x3, (float) y3, (float) z3));
			} else {
				positions.add(new Vec3f((float) pX, (float) pY, (float) ((float) z1 + cZ)));
			}

			cZ += dZ;
		}

		return positions;
	}

	/**
	 * Returns points of a Bresenham line between the two given points with the specified amount of segments.
	 */
	public static Collection<Vec3f> getBresenhamSegments(Vec3f posA, Vec3f posB, float segments) {
		float x1 = posA.getX();
		float y1 = posA.getY();
		float z1 = posA.getZ();

		float x2 = posB.getX();
		float y2 = posB.getY();
		float z2 = posB.getZ();

		List<Vec3f> points = Lists.newArrayList();

		points.add(posA);

		float dx = Math.abs(x2 - x1);
		float dy = Math.abs(y2 - y1);
		float dz = Math.abs(z2 - z1);

		float xs;
		float ys;
		float zs;

		if (x2 > x1) {
			xs = 1 / segments;
		} else {
			xs = -1 / segments;
		}

		if (y2 > y1) {
			ys = 1 / segments;
		} else {
			ys = -1 / segments;
		}

		if (z2 > z1) {
			zs = 1 / segments;
		} else {
			zs = -1 / segments;
		}

		if (dx >= dy && dx >= dz) {
			float p1 = 2 * dy - dx;
			float p2 = 2 * dz - dx;

			while (posA.getX() < posB.getX() ? x1 < x2 : x1 > x2) {
				x1 += xs;

				if (p1 >= 0) {
					y1 += ys;
					p1 -= 2 * dx;
				}

				if (p2 >= 0) {
					z1 += zs;
					p2 -= 2 * dx;
				}

				p1 += 2 * dy;
				p2 += 2 * dz;

				points.add(new Vec3f(x1, y1, z1));
			}
		} else if (dy >= dx && dy >= dz) {
			float p1 = 2 * dx - dy;
			float p2 = 2 * dz - dy;

			while (posA.getY() < posB.getY() ? y1 < y2 : y1 > y2) {
				y1 += ys;

				if (p1 >= 0) {
					x1 += xs;
					p1 -= 2 * dy;
				}

				if (p2 >= 0) {
					z1 += zs;
					p2 -= 2 * dy;
				}

				p1 += 2 * dx;
				p2 += 2 * dz;

				points.add(new Vec3f(x1, y1, z1));
			}
		} else {
			float p1 = 2 * dy - dz;
			float p2 = 2 * dx - dz;

			while (posA.getZ() < posB.getZ() ? z1 < z2 : z1 > z2) {
				z1 += zs;

				if (p1 >= 0) {
					y1 += ys;
					p1 -= 2 * dz;
				}

				if (p2 >= 0) {
					p2 -= 2 * dz;
					x1 += xs;
				}

				p1 += 2 * dy;
				p2 += 2 * dx;

				points.add(new Vec3f(x1, y1, z1));
			}
		}

		return points;
	}
}
