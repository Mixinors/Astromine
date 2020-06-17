package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.utilities.LargeExplosionAlgorithm;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NuclearWeaponBlock extends Block {
	public NuclearWeaponBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		this.tryDetonate(world, pos);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		this.tryDetonate(world, pos);
	}

	private void tryDetonate(World world, BlockPos pos) {
		if(world.isReceivingRedstonePower(pos)) {
			LargeExplosionAlgorithm.explode(world, pos.getX(), pos.getY(), pos.getZ(), 128);
		}
	}
}
