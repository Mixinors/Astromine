package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class LargeExplosionAlgorithm {
	interface PointConsumer {
		void consume(int distance, int x, int y, int z);
	}

	public static void explode(RegionalWorldAccess access, int x, int y, int z, int power) {
		BlockState state = Blocks.AIR.getDefaultState();
		circle((d, bx, by, bz) -> {
			if (by < 0 || by > 255) return;
			if (d-6 > power) {
				// outer edge
				access.setBlockState(state, bx, by, bz);
			} else { // well inside blast zone, so we can setblockstate more effeciently
				access.setBlockState(state, bx, by, bz, 2);
			}
		}, x, y, z, power);
	}

	private static void circle(PointConsumer consumer, int x, int y, int z, int radius) {
		int sqrRad = radius * radius;
		for (int ox = -radius; ox <= radius; ox++) {
			for (int oy = -radius; oy <= radius; oy++) {
				for (int oz = -radius; oz <= radius; oz++) {
					int squared = ox * ox + oy * oy + oz * oz;
					if (squared <= sqrRad) {
						consumer.consume(sqrRad, x + ox, y + oy, z + oz);
					}
				}
			}
		}
	}
}
