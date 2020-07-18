package com.github.chainmailstudios.astromine.common.utilities.type;

public enum BufferType {
	DIRT(1),
	WOOD(3),
	IRON(6),
	GOLD(9),
	DIAMOND(12),
	EMERALD(15);

	private final int height;

	BufferType(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
}
