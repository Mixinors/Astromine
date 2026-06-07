package com.github.mixinors.astromine.common.util;

public class Color {
	public static final Color WHITE = new Color(0xFFFFFFFFL);
	public static final Color GRAY = new Color(0xFF808080L);
	
	private final int r;
	private final int g;
	private final int b;
	private final int a;
	
	public Color(int rgb) {
		this((long) rgb & 0xFFFFFFFFL);
	}
	
	public Color(long rgba) {
		this.r = (int) ((rgba >> 24) & 0xFF);
		this.g = (int) ((rgba >> 16) & 0xFF);
		this.b = (int) ((rgba >> 8) & 0xFF);
		this.a = (int) (rgba & 0xFF);
	}
	
	public Color(float r, float g, float b, float a) {
		this.r = clamp(r * 255.0F);
		this.g = clamp(g * 255.0F);
		this.b = clamp(b * 255.0F);
		this.a = clamp(a * 255.0F);
	}
	
	public Color(float r, float g, float b, int a) {
		this.r = clamp(r * 255.0F);
		this.g = clamp(g * 255.0F);
		this.b = clamp(b * 255.0F);
		this.a = clamp(a);
	}
	
	public int getR() {
		return r;
	}
	
	public int getG() {
		return g;
	}
	
	public int getB() {
		return b;
	}
	
	public int getA() {
		return a;
	}
	
	public int toRgb() {
		return (r << 16) | (g << 8) | b;
	}
	
	public int toArgb() {
		return (a << 24) | toRgb();
	}
	
	private static int clamp(float value) {
		return clamp(Math.round(value));
	}
	
	private static int clamp(int value) {
		return Math.max(0, Math.min(255, value));
	}
}
