package com.github.chainmailstudios.astromine.common.component.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyStorage;

public class EnergyEmitter {
	public static void emit(DefaultedBlockEntity blockEntity, int slot) {
		for (Direction direction : Direction.values()) {
			BlockPos position = blockEntity.getPos().offset(direction);
			BlockEntity attached = blockEntity.getWorld().getBlockEntity(position);

			if (attached instanceof EnergyStorage) {
				Energy.of(blockEntity).side(direction).into(Energy.of(attached).side(direction.getOpposite())).move(1000);

				if (attached instanceof BlockEntityClientSerializable && !attached.getWorld().isClient) {
					((BlockEntityClientSerializable) attached).sync();
				}
			}
		}
	}
}
