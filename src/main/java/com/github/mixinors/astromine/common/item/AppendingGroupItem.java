package com.github.mixinors.astromine.common.item;

import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public interface AppendingGroupItem {
	void appendStacks(Consumer<ItemStack> stacks);
}
