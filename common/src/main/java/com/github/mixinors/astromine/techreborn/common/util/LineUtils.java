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

import net.minecraft.client.util.math.Vector3f;

import java.util.ArrayList;
import java.util.Collection;

public class LineUtils {
	/** Returns points of a Bezier curve between the three given points with the specified amount of segments. */
	public static Collection<Vector3f> getBezierSegments(Vector3f posA, Vector3f posB, Vector3f posC, int segments) {
		var positions = new ArrayList<Vector3f>();

		var x1 = posA.getX();
		var y1 = posA.getY();
		var z1 = posA.getZ();

		var x3 = posB.getX();
		var y3 = posB.getY();
		var z3 = posB.getZ();

		var x2 = posC.getX();
		var y2 = posC.getY();

		var dZ = (z3 - z1) / segments;
		var cZ = 0.0F;

		for (var t = 0.0F; t < 1.0F; t += (1.0F / segments)) {
			var p0M = Math.pow(1.0F - t, 2.0F);
			var p0X = x1 * p0M;
			var p0Y = y1 * p0M;

			var p1M = 2.0F * t * (1.0F - t);
			var p1X = p1M * x2;
			var p1Y = p1M * y2;

			var p2M = Math.pow(t, 2.0F);
			var p2X = p2M * x3;
			var p2Y = p2M * y3;

			var pX = p0X + p1X + p2X;
			var pY = p0Y + p1Y + p2Y;

			if (t == 0) {
				positions.add(new Vector3f(x1, y1, z1));
			} else if (t + (1.0F / segments) >= 1.0F) {
				positions.add(new Vector3f(x3, y3, z3));
			} else {
				positions.add(new Vector3f((float) pX, (float) pY, z1 + cZ));
			}

			cZ += dZ;
		}

		return positions;
	}

	/** Returns points of a Bresenham line between the two given points with the specified amount of segments. */
	public static Collection<Vector3f> getBresenhamSegments(Vector3f posA, Vector3f posB, float segments) {
		var x1 = posA.getX();
		var y1 = posA.getY();
		var z1 = posA.getZ();

		var x2 = posB.getX();
		var y2 = posB.getY();
		var z2 = posB.getZ();

		var points = new ArrayList<Vector3f>();

		points.add(posA);

		var dx = Math.abs(x2 - x1);
		var dy = Math.abs(y2 - y1);
		var dz = Math.abs(z2 - z1);

		var xs = 0.0F;
		var ys = 0.0F;
		var zs = 0.0F;

		if (x2 > x1) {
			xs = 1.0F / segments;
		} else {
			xs = -1.0F / segments;
		}

		if (y2 > y1) {
			ys = 1.0F / segments;
		} else {
			ys = -1.0F / segments;
		}

		if (z2 > z1) {
			zs = 1.0F / segments;
		} else {
			zs = -1.0F / segments;
		}

		if (dx >= dy && dx >= dz) {
			var p1 = 2.0F * dy - dx;
			var p2 = 2.0F * dz - dx;

			while (posA.getX() < posB.getX() ? x1 < x2 : x1 > x2) {
				x1 += xs;

				if (p1 >= 0.0F) {
					y1 += ys;
					p1 -= 2.0F * dx;
				}

				if (p2 >= 0.0F) {
					z1 += zs;
					p2 -= 2.0F * dx;
				}

				p1 += 2.0F * dy;
				p2 += 2.0F * dz;

				points.add(new Vector3f(x1, y1, z1));
			}
		} else if (dy >= dx && dy >= dz) {
			var p1 = 2.0F * dx - dy;
			var p2 = 2.0F * dz - dy;

			while (posA.getY() < posB.getY() ? y1 < y2 : y1 > y2) {
				y1 += ys;

				if (p1 >= 0.0F) {
					x1 += xs;
					p1 -= 2.0F * dy;
				}

				if (p2 >= 0.0F) {
					z1 += zs;
					p2 -= 2.0F * dy;
				}

				p1 += 2.0F * dx;
				p2 += 2.0F * dz;

				points.add(new Vector3f(x1, y1, z1));
			}
		} else {
			var p1 = 2.0F * dy - dz;
			var p2 = 2.0F * dx - dz;

			while (posA.getZ() < posB.getZ() ? z1 < z2 : z1 > z2) {
				z1 += zs;

				if (p1 >= 0.0F) {
					y1 += ys;
					p1 -= 2.0F * dz;
				}

				if (p2 >= 0.0F) {
					p2 -= 2.0F * dz;
					x1 += xs;
				}

				p1 += 2.0F * dy;
				p2 += 2.0F * dx;

				points.add(new Vector3f(x1, y1, z1));
			}
		}

		return points;
	}
}
