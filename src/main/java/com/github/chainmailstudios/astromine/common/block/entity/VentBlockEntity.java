package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class VentBlockEntity extends AlphaBlockEntity implements Tickable, NetworkMember {
	public VentBlockEntity() {
		super(AstromineBlockEntityTypes.vent);
	}

	@Override
	public void tick() {
		if ((energyComponent.getVolume(0).getFraction().isBiggerThan(Fraction.BOTTLE) || energyComponent.getVolume(0).getFraction().equals(Fraction.BOTTLE)) && (fluidComponent.getVolume(0).getFraction().isBiggerThan(Fraction.BUCKET) || fluidComponent.getVolume(0).getFraction().equals(Fraction.BUCKET))){
			BlockPos position = getPos();

			Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

			BlockPos output = position.offset(direction);

			if (world.getBlockState(output).getBlock() instanceof AirBlock) {
				AtmosphericManager.add(world, output, fluidComponent.getVolume(0).take(Fraction.BUCKET));
			}

			energyComponent.getVolume(0).setFraction(Fraction.subtract(energyComponent.getVolume(0).getFraction(), Fraction.BOTTLE));
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
