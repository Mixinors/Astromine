package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.manager.BodyManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AMBodies {
	public static final Body SUN = new Body(new Vec3d(192_000_000.0D, 0.0D, 192_000_000.0D), new Vec3d(0.0D, 0.0D, 0.0D), 1.989D * Math.pow(10.0D, 30.0D), 5778.0D, null, null);
	
	public static final Body EARTH = new Body(new Vec3d(192_000_000.0D, 0.0D, 192_000_000.0D + 152_100_000.0D), new Vec3d(24.0D, 0.0D, 0.0D), 5.972 * Math.pow(10.0D, 24.0D), 288.0D, World.OVERWORLD, AMWorlds.EARTH_ORBIT_WORLD);
	public static final Body MOON = new Body(new Vec3d(192_000_000.0D, 0.0D, 192_000_000.0D + 152_484_400.0D), new Vec3d(24.0D, 0.0D, 0.0D), 7.34767309 * Math.pow(10.0D, 22.0D), 400.0D, AMWorlds.MOON_WORLD, null);
	
	public static void init() {
		BodyManager.add(SUN);
		
		BodyManager.add(EARTH);
		BodyManager.add(MOON);
	}
}
