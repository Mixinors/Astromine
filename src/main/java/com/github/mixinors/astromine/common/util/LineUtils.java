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

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;

public class LineUtils {
	public static Collection<Vector3f> getBezierSegments(Vector3f posA, Vector3f posB, Vector3f posC, float segments) {
		var positions = new ArrayList<Vector3f>();
		
		var x1 = posA.x();
		var y1 = posA.y();
		var z1 = posA.z();
		
		var x3 = posB.x();
		var y3 = posB.y();
		var z3 = posB.z();
		
		var x2 = posC.x();
		var y2 = posC.y();
		
		var dZ = (z3 - z1) / segments;
		var cZ = 0;
		
		for (var t = 0; t < 1; t += (1f / segments)) {
			var p0M = Math.pow(1 - t, 2);
			var p0X = x1 * p0M;
			var p0Y = y1 * p0M;
			
			var p1M = 2 * t * (1 - t);
			var p1X = p1M * x2;
			var p1Y = p1M * y2;
			
			var p2M = Math.pow(t, 2);
			var p2X = p2M * x3;
			var p2Y = p2M * y3;
			
			var pX = p0X + p1X + p2X;
			var pY = p0Y + p1Y + p2Y;
			
			if (t == 0) {
				positions.add(new Vector3f(x1, y1, z1));
			} else if (t + (1f / segments) >= 1) {
				positions.add(new Vector3f(x3, y3, z3));
			} else {
				positions.add(new Vector3f((float) pX, (float) pY, z1 + cZ));
			}
			
			cZ += dZ;
		}
		
		return positions;
	}
	
	public static Collection<Vector3f> getBresenhamSegments(Vector3f posA, Vector3f posB, float segments) {
		var x1 = posA.x();
		var y1 = posA.y();
		var z1 = posA.z();
		
		var x2 = posB.x();
		var y2 = posB.y();
		var z2 = posB.z();
		
		var points = new ArrayList<Vector3f>();
		
		points.add(posA);
		
		var dx = Math.abs(x2 - x1);
		var dy = Math.abs(y2 - y1);
		var dz = Math.abs(z2 - z1);
		
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
			var p1 = 2 * dy - dx;
			var p2 = 2 * dz - dx;
			
			while (posA.x() < posB.x() ? x1 < x2 : x1 > x2) {
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
				
				points.add(new Vector3f(x1, y1, z1));
			}
		} else if (dy >= dx && dy >= dz) {
			var p1 = 2 * dx - dy;
			var p2 = 2 * dz - dy;
			
			while (posA.y() < posB.y() ? y1 < y2 : y1 > y2) {
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
				
				points.add(new Vector3f(x1, y1, z1));
			}
		} else {
			var p1 = 2 * dy - dz;
			var p2 = 2 * dx - dz;
			
			while (posA.z() < posB.z() ? z1 < z2 : z1 > z2) {
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
				
				points.add(new Vector3f(x1, y1, z1));
			}
		}
		
		return points;
	}
}
