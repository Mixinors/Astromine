package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.registry.common.AMBodies;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BodyManager {
	private static final double G = 6.6743 * Math.pow(10, -11.0D);
	
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
	
	public static void tick() {
		BODIES.remove(AMBodies.MOON);
		for (var i : BODIES) {
			var acceleration = Vec3d.ZERO;
			
			for (var j : BODIES) {
				if (i != j) {
					acceleration = acceleration.add((j.getPos().subtract(i.getPos())).multiply(G * (j.getMass()))).multiply(1.0D / Math.pow(i.getPos().distanceTo(j.getPos()), 3.0D));
				}
			}
			
			if (i != AMBodies.SUN) {
				acceleration = acceleration.multiply(1500000000000000000.0D);
				
				if (acceleration.getX() < -250) {
					acceleration = new Vec3d(-250, acceleration.getY(), acceleration.getZ());
				} else if (acceleration.getX() > 250) {
					acceleration = new Vec3d(250, acceleration.getY(), acceleration.getZ());
				}
				
				if (acceleration.getZ() < -250) {
					acceleration = new Vec3d(acceleration.getX(), acceleration.getY(), -250);
				} else if (acceleration.getZ() > 250) {
					acceleration = new Vec3d(acceleration.getX(), acceleration.getY(), 250);
				}
				
				i.setPos(i.getPos().add(i.getVelocity()));
				i.setVelocity(i.getVelocity().add(acceleration));
			}
		}
	}
}
