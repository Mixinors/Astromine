package com.github.mixinors.astromine.common.component;

import com.github.mixinors.astromine.common.block.redstone.RedstoneType;
import com.github.mixinors.astromine.common.component.block.entity.RedstoneComponent;
import net.minecraft.nbt.CompoundTag;

public interface ProtoComponent {
	void writeToNbt(CompoundTag tag);
	
	void readFromNbt(CompoundTag tag);
}
