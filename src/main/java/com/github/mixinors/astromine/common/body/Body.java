package com.github.mixinors.astromine.common.body;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Body {
	private final Vec3d startingPos;
	private final Vec3d startingVelocity;
	
	private Vec3d pos;
	private Vec3d velocity;
	
	private double mass;
	private final double temperature;
	
	@Nullable
	private final RegistryKey<World> worldKey;
	
	@Nullable
	private final RegistryKey<World> orbitWorldKey;
	
	public Body(Vec3d startingPos, Vec3d startingVelocity, double mass, double temperature, @Nullable RegistryKey<World> worldKey, @Nullable RegistryKey<World> orbitWorldKey) {
		this.startingPos = startingPos;
		this.startingVelocity = startingVelocity;
		
		this.pos = startingPos;
		this.velocity = startingVelocity;
		
		this.mass = mass;
		this.temperature = temperature;
		
		this.worldKey = worldKey;
		this.orbitWorldKey = orbitWorldKey;
	}
	
	public Vec3d getStartingPos() {
		return startingPos;
	}
	
	public Vec3d getStartingVelocity() {
		return startingVelocity;
	}
	
	public Vec3d getPos() {
		return pos;
	}
	
	public void setPos(Vec3d pos) {
		this.pos = pos;
	}
	
	public Vec3d getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vec3d velocity) {
		this.velocity = velocity;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public RegistryKey<World> getWorldKey() {
		return worldKey;
	}
	
	@Nullable
	public RegistryKey<World> getOrbitWorldKey() {
		return orbitWorldKey;
	}
}
