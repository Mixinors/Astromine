package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.access.WorldChunkAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkSectionPos;

public class LargeExplosionAlgorithm {
    private static final int FLOOR_MASK = ~15;

    interface PointConsumer {
        void consume(int distance, int x, int y, int z);
    }

    public static void explode(RegionalWorldAccess access, int x, int y, int z, int power) {
        BlockState state = Blocks.AIR.getDefaultState();
        long start = System.currentTimeMillis();
        //sphere((d, bx, by, bz) -> access.setBlockState(state, bx, by, bz, 0), x, y, z, power);
        nukeChunks(access, x, y, z, power);
        long end = System.currentTimeMillis();
        System.out.printf("Explosion with power %d took %dms to explode!\n", power, end - start);
    }

    private static void sphere(PointConsumer consumer, int x, int y, int z, int radius) {
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

    private static void nukeChunks(RegionalWorldAccess access, int x, int y, int z, int radius) {
        // chunkwise radius
        int chunkRadius = radius >> 4;
        // C.hunk O.ffset X
        for (int cox = -chunkRadius; cox <= chunkRadius + 1; cox++) {
            // C.hunk O.ffset Z
            for (int coz = -chunkRadius; coz <= chunkRadius + 1; coz++) {
                // check if chunk is in area
                int box = cox * 16, boz = coz * 16;
                // if we can skip the entire chunk, do so
                if (touchesOrIsIn(box, 0, boz, box + 15, 255, boz + 15, radius))
                    forSubchunks(access, box, boz, x, y, z, radius);
                // todo send packet
            }
        }
    }

    private static void forSubchunks(RegionalWorldAccess access, int bx, int bz, int x, int y, int z, int radius) {
        int subChunkRadius = radius >> 4;
        int subchunk = y >> 4;
        for (int i = Math.max(-subChunkRadius + subchunk, 0); i <= Math.min(subchunk + subChunkRadius + 1, 15); i++) {
            int by = i * 16;
            if (encompassed(bx, by, bz, bx + 15, by + 15, bz + 15, radius)) {
                int rx = (bx + x) >> 4, rz = (bz + z) >> 4;
                System.out.printf("yeeted %d, %d, %d\n", (bx + x) >> 4, i, (bz + z) >> 4);
                ((WorldChunkAccess) access.getChunk((bx + x) >> 4, (bz + z) >> 4)).astromine_yeet(i);
                access.getWorld().getChunkManager().getLightingProvider().updateSectionStatus(ChunkSectionPos.from(rx, i, rz), true);
            }
        }
    }


    private static boolean touchesOrIsIn(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
        return in(x1, y1, z1, radius) || in(x1, y1, z2, radius) || in(x1, y2, z1, radius) || in(x1, y2, z2, radius) || in(x2, y1, z1, radius) || in(x2, y1, z2, radius) || in(x2, y2, z1, radius) || in(x2, y2, z2, radius);
    }

    private static boolean encompassed(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
        return in(x1, y1, z1, radius) && in(x1, y1, z2, radius) && in(x1, y2, z1, radius) && in(x1, y2, z2, radius) && in(x2, y1, z1, radius) && in(x2, y1, z2, radius) && in(x2, y2, z1, radius) && in(x2, y2, z2, radius);
    }

    private static boolean in(int ox, int oy, int oz, int radius) {
        return ox * ox + oy * oy + oz * oz <= radius * radius;
    }
}
