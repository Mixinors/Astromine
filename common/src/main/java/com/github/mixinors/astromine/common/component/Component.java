package com.github.mixinors.astromine.common.component;

import com.github.mixinors.astromine.common.block.redstone.RedstoneType;
import com.github.mixinors.astromine.common.component.block.entity.RedstoneComponent;
import net.minecraft.nbt.CompoundTag;

public interface Component {
	void toTag(CompoundTag tag);
	
	void fromTag(CompoundTag tag);
}
