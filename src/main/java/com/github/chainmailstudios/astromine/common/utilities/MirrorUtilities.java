package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.util.math.Direction;

import static net.minecraft.util.math.Direction.*;

public class MirrorUtilities {
	public static Direction rotate(Direction origin, Direction rotation) {
		if (rotation == NORTH) {
			switch (origin) {
				case NORTH: return NORTH;
				case SOUTH: return SOUTH;
				case WEST: return WEST;
				case EAST: return EAST;
				default: return origin;
			}
		} else if (rotation == SOUTH) {
			switch (origin) {
				case NORTH: return SOUTH;
				case SOUTH: return NORTH;
				case WEST: return EAST;
				case EAST: return WEST;
				default: return origin;
			}
		} else if (rotation == WEST) {
			switch (origin) {
				case NORTH: return WEST;
				case SOUTH: return EAST;
				case EAST: return SOUTH;
				case WEST: return NORTH;
				default: return origin;
			}
		} else if (rotation == EAST) {
			switch (origin) {
				case NORTH: return EAST;
				case SOUTH: return WEST;
				case EAST: return NORTH;
				case WEST: return SOUTH;
				default: return origin;
			}
		} else {
			return origin; // TODO - UP && DOWN
		}
	}
}
