package com.github.chainmailstudios.astromine.technologies.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class PumpPipeBlock extends Block {
    public static final VoxelShape PIPE_SHAPE = Block.createCuboidShape(5D, 0D, 5D, 11D, 16D, 11D);

    public PumpPipeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos position, ShapeContext entityContext) {
       return PIPE_SHAPE;
    }
}
