package com.github.mixinors.astromine.common.gravity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GravityManager {
	private static final double DEFAULT_GRAVITY = 0.08D;
	private static final Map<ResourceKey<Level>, Double> GRAVITY_BY_LEVEL = new ConcurrentHashMap<>();
	
	private GravityManager() {
	}
	
	public static double get(ResourceKey<Level> level) {
		return GRAVITY_BY_LEVEL.getOrDefault(level, DEFAULT_GRAVITY);
	}
	
	public static void set(ResourceKey<Level> level, double gravity) {
		GRAVITY_BY_LEVEL.put(level, gravity);
	}
	
	public static void reset(ResourceKey<Level> level) {
		GRAVITY_BY_LEVEL.remove(level);
	}
}
