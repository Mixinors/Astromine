package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.registry.common.AMBodies;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BodyManager {
	private static final List<Body> BODIES = new ArrayList<>();
	
	public static Collection<Body> getBodies() {
		return BODIES;
	}
	
	public static void add(Body body) {
		BODIES.add(body);
	}
	
	public static void remove(Body body) {
		BODIES.remove(body);
	}
}
