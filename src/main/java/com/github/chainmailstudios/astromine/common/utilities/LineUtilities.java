package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Collection;

public class LineUtilities {
	/**
	 * Builds a Bezier curve from two points.
	 * Current implementation needs to be reworked.
	 *
	 * @param posA     the first specified position.
	 * @param posB     the second specified position.
	 * @param segments the segments between both positions.
	 * @return the dots of the requested curve.
	 */
	public static Collection<Vector3f> getBezierSegments(Vec3i posA, Vec3i posB, float segments) {
		ArrayList<Vector3f> positions = new ArrayList<>();

		double x1 = posA.getX();
		double y1 = posA.getY();
		double z1 = posA.getZ();

		double x3 = posB.getX();
		double y3 = posB.getY();
		double z3 = posB.getZ();

		double x2 = ((x3 + x1)) / 2;
		double y2 = ((y1 + y3) / 2) - (Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2) + Math.pow(z3 - z1, 2)) * (1f / segments));

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
				positions.add(new Vector3f((float) x1, (float) y1, (float) z1));
			} else if (t + (1f / segments) >= 1) {
				positions.add(new Vector3f((float) x3, (float) y3, (float) z3));
			} else {
				positions.add(new Vector3f((float) pX, (float) pY, (float) ((float) z1 + cZ)));
			}

			cZ += dZ;
		}

		return positions;
	}
}
