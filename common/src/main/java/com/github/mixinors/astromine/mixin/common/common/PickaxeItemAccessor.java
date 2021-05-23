package com.github.mixinors.astromine.mixin.common.common;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PickaxeItem.class)
public interface PickaxeItemAccessor {
	@Invoker("<init>")
	static PickaxeItem init(ToolMaterial toolMaterial, int i, float f, Item.Settings settings) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
