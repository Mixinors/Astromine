package com.github.chainmailstudios.astromine.common.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;

public interface TieredMachine {
	MachineTier getTier();
}
