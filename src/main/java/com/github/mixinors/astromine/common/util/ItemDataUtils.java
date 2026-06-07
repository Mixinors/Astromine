package com.github.mixinors.astromine.common.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.function.Consumer;

public final class ItemDataUtils {
	private ItemDataUtils() {
	}
	
	public static CompoundTag get(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
	}
	
	public static void update(ItemStack stack, Consumer<CompoundTag> updater) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, updater);
	}
}
