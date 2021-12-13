package com.github.mixinors.astromine.common.block;

import java.util.Map;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FilledCauldronBlock extends AbstractCauldronBlock {
	public FilledCauldronBlock(AbstractBlock.Settings settings, Map<Item, CauldronBehavior> behavior) {
		super(settings, behavior);
	}

	@Override
	protected double getFluidHeight(BlockState state) {
		return 0.9375;
	}

	@Override
	public boolean isFull(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return 3;
	}
}
