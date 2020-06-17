package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import org.apache.logging.log4j.Level;

public class OxygenVentBlockEntity extends BaseBiBlockEntity implements Tickable {
	private boolean locked = false;

	public OxygenVentBlockEntity() {
		super(AstromineBlockEntityTypes.ELECTROLYZER);
	}

	@Override
	public void tick() {
		if (this.locked) {
			return;
		}

		long start = System.currentTimeMillis();

		BlockPos position = this.getPos();

		Direction direction = this.world.getBlockState(position).get(FacingBlock.FACING);

		BlockPos output = position.offset(direction);

		if (this.world.getBlockState(output).getBlock() instanceof AirBlock) {
			AtmosphericManager.add(this.world, output, new FluidVolume(Fluids.WATER, Fraction.BUCKET));
		}

		long end = System.currentTimeMillis();

		if (end - start > 50) {
			this.locked = true;
			AstromineCommon.LOGGER.log(Level.WARN, "Oxygen Vent tick took over 50ms; suspending its activity!");
		}
	}
}
