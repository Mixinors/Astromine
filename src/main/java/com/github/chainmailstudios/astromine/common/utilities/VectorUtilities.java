package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class VectorUtilities {
	public static Vector3f toVector3f(Vec3d vec3d) {
		return new Vector3f((float) vec3d.getX(), (float) vec3d.getY(), (float) vec3d.getZ());
	}

	public static Vector3f toVector3f(Vec3i vec3i) {
		return new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
	}
}
