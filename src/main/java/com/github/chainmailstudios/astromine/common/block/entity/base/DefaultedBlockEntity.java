package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public abstract class DefaultedBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	public DefaultedBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
}
