package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class OxygenVentBlockEntity extends AlphaBlockEntity implements Tickable, NetworkMember {
	public OxygenVentBlockEntity() {
		super(AstromineBlockEntityTypes.OXYGEN_VENT);
	}

	@Override
	public void tick() {
		BlockPos position = getPos();

		Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

		BlockPos output = position.offset(direction);

		if (world.getBlockState(output).getBlock() instanceof AirBlock) {
			AtmosphericManager.add(world, output, new FluidVolume(AstromineFluids.OXYGEN, Fraction.BUCKET));
		}
	}

	@Override
	public NetworkType getNetworkType() {
		return AstromineNetworkTypes.FLUID;
	}

	@Override
	public boolean accepts(Object... objects) {
		return true;
	}

	@Override
	public boolean isRequester() {
		return true;
	}
}
