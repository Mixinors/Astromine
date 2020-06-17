package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.bridge.HolographicBridgeManager;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class HolographicBridgeInvisibleBlock extends Block {
	public HolographicBridgeInvisibleBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	@Environment (EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos position, ShapeContext context) {
		return HolographicBridgeManager.getShape(world, position);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos position, ShapeContext context) {
		return HolographicBridgeManager.getShape(world, position);
	}
}
