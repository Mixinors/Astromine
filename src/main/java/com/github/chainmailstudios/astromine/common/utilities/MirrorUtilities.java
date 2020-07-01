package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.util.math.Direction;

public class MirrorUtilities {
	public static Direction rotate(Direction origin, Direction rotation) {
		if (rotation == Direction.NORTH) {
			return origin;
		} else if (rotation == Direction.SOUTH) {
			return origin.getOpposite();
		} else if (rotation == Direction.WEST) {
			return origin == Direction.NORTH ? Direction.WEST : origin == Direction.SOUTH ? Direction.EAST : origin == Direction.WEST ? Direction.NORTH : origin == Direction.EAST ? Direction.SOUTH : origin;
		} else if (rotation == Direction.EAST) {
			return origin == Direction.NORTH ? Direction.EAST : origin == Direction.SOUTH ? Direction.WEST : origin == Direction.EAST ? Direction.NORTH : origin == Direction.WEST ? Direction.SOUTH : origin;
		} else {
			return origin;
		}
	}
}
