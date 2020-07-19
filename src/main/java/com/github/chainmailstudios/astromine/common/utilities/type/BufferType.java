package com.github.chainmailstudios.astromine.common.utilities.type;

public enum BufferType {
	BASIC(6),
	ADVANCED(12),
	ELITE(24);

	private final int height;

	BufferType(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
}
