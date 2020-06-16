package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fluid.collection.IndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.fluid.collection.SidedVolumeCollection;
import com.github.chainmailstudios.astromine.common.fluid.collection.SimpleSidedVolumeCollection;
import com.github.chainmailstudios.astromine.common.fluid.logic.Volume;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.gas.GasManager;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;

public class OxygenVentBlockEntity extends BlockEntity implements Tickable, SidedVolumeCollection {
	private long counter = 0;
	private boolean locked = false;

	private SimpleSidedVolumeCollection sidedVolumeCollection = new SimpleSidedVolumeCollection();

	public OxygenVentBlockEntity() {
		super(AstromineBlockEntityTypes.ELECTROLYZER);
	}

	@Override
	public void tick() {
		if (System.currentTimeMillis() - counter >= 50 && !locked) {
			long start = System.currentTimeMillis();

			BlockPos position = getPos();

			Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

			BlockPos output = position.offset(direction);

			if (world.getBlockState(output).getBlock() instanceof AirBlock) {
				GasManager.add(world, output, new Volume(Fluids.WATER, Fraction.BUCKET));
			}

			counter = System.currentTimeMillis();

			long end = System.currentTimeMillis();

			if (start - end > 50) {
				locked = true;

				AstromineCommon.LOGGER.log(Level.WARN, "Electrolyzer tick took over 50ms; suspending its activity!");
			}
		}
	}

	@Override
	public Map<Direction, IndexedVolumeCollection> getCollections() {
		return sidedVolumeCollection.getCollections();
	}
}
