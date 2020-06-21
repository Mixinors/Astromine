package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class VentBlockEntity extends AlphaBlockEntity implements Tickable, NetworkMember {
	public VentBlockEntity() {
		super(AstromineBlockEntityTypes.vent);
	}

	@Override
	public void tick() {
		if (energyComponent.getVolume(0).hasStored(Fraction.BOTTLE) && (fluidComponent.getVolume(0).hasStored(Fraction.BUCKET))) {
			world.addParticle(ParticleTypes.FLAME, getPos().getX(), getPos().getY() + 1, getPos().getZ(), 0, 0.2f, 0);

			BlockPos position = getPos();

			Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

			BlockPos output = position.offset(direction);

			if (world.getBlockState(output).getBlock() instanceof AirBlock) {
				FluidVolume bucketVolume = fluidComponent.getVolume(0).extractVolume(Fraction.BUCKET);

				FluidVolume volume = AtmosphericManager.get(world, output);

				volume.pullVolume(bucketVolume, Fraction.BUCKET);

				AtmosphericManager.add(world, output, volume);

				energyComponent.getVolume(0).extractVolume(Fraction.BOTTLE);
			}
		}

		System.out.print("\n-" + energyComponent.getVolume(0).getFraction().longValue());
	}

	@Override
	public NetworkType getNetworkType() {
		return AstromineNetworkTypes.FLUID;
	}

	@Override
	public boolean isRequester() {
		return true;
	}
}
