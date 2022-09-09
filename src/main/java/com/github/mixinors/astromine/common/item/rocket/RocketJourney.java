package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.registry.base.RegistryEntry;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class RocketJourney {
	public static final Codec<RocketJourney> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					RegistryEntry.createCodec(AMRegistries.BODY).fieldOf("from").forGetter(RocketJourney::getFrom),
					RegistryEntry.createCodec(AMRegistries.BODY).fieldOf("to").forGetter(RocketJourney::getTo),
					Codec.BOOL.fieldOf("started").forGetter(RocketJourney::hasStarted),
					Codec.BOOL.fieldOf("finished").forGetter(RocketJourney::hasFinished),
					Codec.LONG.fieldOf("travelledDistance").forGetter(RocketJourney::getTravelledDistance)
			).apply(instance, RocketJourney::new)
	);
	
	private final RegistryEntry<Body> from;
	private final RegistryEntry<Body> to;
	
	private boolean started = false;
	private boolean finished = false;
	
	// In KM.
	private long travelledDistance = 0L;
	
	public RocketJourney(RegistryEntry<Body> from, RegistryEntry<Body> to, boolean started, boolean finished, long travelledDistance) {
		this.from = from;
		this.to = to;
		
		this.started = started;
		this.finished = finished;
		
		this.travelledDistance = travelledDistance;
	}
	
	public RocketJourney(RegistryEntry<Body> from, RegistryEntry<Body> to) {
		this.from = from;
		this.to = to;
	}
	
	public long getDistance() {
		return (long) from.get().position().distanceTo(to.get().position());
	}
	
	public long getTravelledDistance() {
		return travelledDistance;
	}
	
	public void start() {
		this.started = true;
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public void finish() {
		this.finished = true;
	}
	
	public boolean hasFinished() {
		return finished;
	}
	
	public RegistryEntry<Body> getFrom() {
		return from;
	}
	
	public RegistryEntry<Body> getTo() {
		return to;
	}
	
	// 1KM is 1/81000 Buckets of fuel.
	// We consume twice as much oxygen as fuel.
	// Small Fuel Tanks hold 32 buckets of fuel.
	
	// The distance to the moon is our base metric.
	// 384000 KM is the default.
	// We want to get there in 5 minutes.
	// Means we must travel 1280KM/s.
	// Means we consume 4 droplets per KM.
	// Means we consume 5120 droplets per second.
	// Means we consume 1536000 droplets per Moon journey.
	// Means we consume 9 buckets of Fuel and 18 buckets of Oxygen per Moon journey.
	// Means we consume 1/3rd of a Small Fuel Tank to get to the Moon
	
	// TODO: Make Moon a bit closer so it only uses 16 buckets of Oxygen.
	public void tick(Rocket rocket) {
		if (!hasFinished()) {
			var oxygen = (double) rocket.getOxygen();
			var fuel = (double) rocket.getFuel();
			var thruster = rocket.getThruster().orElseThrow();
			
			oxygen -= 4 * thruster.getEfficiency().getFuelConsumptionMultiplier();
			fuel -= 2 * thruster.getEfficiency().getFuelConsumptionMultiplier();
			
			rocket.setOxygen((long) oxygen);
			rocket.setFuel((long) fuel);
			
			travelledDistance += 1280;
			
			if (travelledDistance >= getDistance()) {
				finish();
			}
		}
	}
}
