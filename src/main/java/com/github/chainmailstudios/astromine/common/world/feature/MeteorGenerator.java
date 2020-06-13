package com.github.chainmailstudios.astromine.common.world.feature;

import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceWithDimensions;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MeteorGenerator extends StructurePieceWithDimensions {

    public MeteorGenerator(Random random, int x, int z) {
        super(AstromineFeatures.METEOR, random, x, 64, z, 16, 16, 16);
    }

    public MeteorGenerator(StructureManager manager, CompoundTag tag) {
        super(AstromineFeatures.METEOR, tag);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos originPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
        emptySphere(
                world,
                originPos,
                30,
                state -> {
                    return Blocks.FIRE.getDefaultState();
                },
                state -> {
                    return Blocks.ANDESITE.getDefaultState();
                }
        );

        originPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
        buildSphere(world, originPos, 15, Blocks.OBSIDIAN.getDefaultState());

        return true;
    }

    private void emptySphere(ServerWorldAccess world, BlockPos originPos, int radius, GroundManipulator bottom, GroundManipulator underneath) {
        List<BlockPos> placedPositions = new ArrayList<>();

        for(int x = -radius; x <= radius; x++) {
            for(int z = -radius; z <= radius; z++) {
                for(int y = -radius; y <= radius; y++) {
                    double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));

                    // place blocks within spherical radius
                    if(distance <= radius) {
                        BlockPos offsetPos = originPos.add(x, y, z);

                        world.setBlockState(
                                offsetPos,
                                Blocks.AIR.getDefaultState(),
                                3
                        );

                        placedPositions.add(offsetPos);
                    }
                }
            }
        }

        List<BlockPos> bottomPositions = new ArrayList<>();
        List<BlockPos> underneathPositions = new ArrayList<>();

        for (BlockPos pos : placedPositions) {
            // store bottom block
            if(world.getBlockState(pos).isAir() && !world.getBlockState(pos.down()).isAir()) {
                bottomPositions.add(pos);
                underneathPositions.add(pos.down());
            }
        }

        for (BlockPos pos : bottomPositions) {
            world.setBlockState(pos, bottom.manipulate(world.getBlockState(pos)), 3);
        }

        for (BlockPos pos : underneathPositions) {
            world.setBlockState(pos, underneath.manipulate(world.getBlockState(pos)), 3);
        }
    }

    private void buildSphere(ServerWorldAccess world, BlockPos originPos, int radius, BlockState state) {
        for(int x = -radius; x <= radius; x++) {
            for(int z = -radius; z <= radius; z++) {
                for(int y = -radius; y <= radius; y++) {
                    double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));

                    // place blocks within spherical radius
                    if(distance <= radius) {
                        world.setBlockState(
                                originPos.add(x, y, z),
                                state,
                                3
                        );
                    }
                }
            }
        }
    }

    @FunctionalInterface
    public interface GroundManipulator {
        BlockState manipulate(BlockState state);
    }
}
