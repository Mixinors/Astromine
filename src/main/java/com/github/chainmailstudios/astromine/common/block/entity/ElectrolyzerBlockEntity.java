package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.fluid.logic.Volume;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.gas.GasManager;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ElectrolyzerBlockEntity extends BlockEntity implements Tickable {
	private long counter = 0;

	public ElectrolyzerBlockEntity() {
		super(AstromineBlockEntityTypes.ELECTROLYZER);
	}

	@Override
	public void tick() {
		if (System.currentTimeMillis() - counter >= 250) {
			long tA = System.currentTimeMillis();

			BlockPos position = getPos();

			Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

			BlockPos output = position.offset(direction);

			if (world.getBlockState(output).getBlock() instanceof AirBlock) {
				GasManager.add(world, output, new Volume(Fluids.WATER, Fraction.BOTTLE));

				GasManager.propagate(world, output);
			}

			counter = System.currentTimeMillis();

			System.out.println(System.currentTimeMillis() - tA);
		}
	}
}
