package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.EnergyWireConnectorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class EnergyWireConnectorBlock extends WireConnectorBlock {
	public EnergyWireConnectorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new EnergyWireConnectorBlockEntity();
	}
}
