package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedHorizontalFacingBlockWithEntity;

public abstract class TieredHorizontalFacingMachineBlock extends DefaultedHorizontalFacingBlockWithEntity {
	public TieredHorizontalFacingMachineBlock(Settings settings) {
		super(settings);
	}
	
	public abstract double getMachineSpeed();
}
