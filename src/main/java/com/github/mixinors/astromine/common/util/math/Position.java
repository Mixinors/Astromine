package com.github.mixinors.astromine.common.util.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Position {
	public static final Codec<Position> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("x").forGetter(Position::x),
			Codec.FLOAT.fieldOf("y").forGetter(Position::y),
			Codec.FLOAT.optionalFieldOf("z", 0.0F).forGetter(Position::z)
	).apply(instance, Position::new));
	
	public final float x;
	public final float y;
	public final float z;
	
	public Position(float x, float y) {
		this(x, y, 0.0F);
	}
	
	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Position(Position anchor, float x, float y) {
		this(anchor, x, y, 0.0F);
	}
	
	public Position(Position anchor, float x, float y, float z) {
		this(anchor.x + x, anchor.y + y, anchor.z + z);
	}
	
	public float x() {
		return x;
	}
	
	public float y() {
		return y;
	}
	
	public float z() {
		return z;
	}
	
	public Position plus(Position other) {
		return new Position(x + other.x, y + other.y, z + other.z);
	}
	
	public double distanceTo(Position other) {
		var xDistance = x - other.x;
		var yDistance = y - other.y;
		var zDistance = z - other.z;
		
		return Math.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
	}
}
