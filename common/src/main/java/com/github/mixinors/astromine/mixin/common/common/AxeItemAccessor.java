package com.github.mixinors.astromine.mixin.common.common;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {
	@Invoker("<init>")
	static AxeItem init(ToolMaterial toolMaterial, float f, float g, Item.Settings settings) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
