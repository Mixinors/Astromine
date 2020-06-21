package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.FluidTankBlockEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class FluidTankBlock extends Block implements BlockEntityProvider, NetworkMember {
	public FluidTankBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isBuffer() {
		return true;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new FluidTankBlockEntity();
	}
}
