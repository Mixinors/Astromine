package com.github.mixinors.astromine.common.util.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Size {
	public static final Codec<Size> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("width").forGetter(Size::width),
			Codec.FLOAT.fieldOf("height").forGetter(Size::height),
			Codec.FLOAT.optionalFieldOf("length", 0.0F).forGetter(Size::length)
	).apply(instance, Size::new));
	
	public final float width;
	public final float height;
	public final float length;
	
	public Size(float width, float height) {
		this(width, height, 0.0F);
	}
	
	public Size(float width, float height, float length) {
		this.width = width;
		this.height = height;
		this.length = length;
	}
	
	public Size(Size size) {
		this(size.width, size.height, size.length);
	}
	
	public float width() {
		return width;
	}
	
	public float height() {
		return height;
	}
	
	public float length() {
		return length;
	}
}
