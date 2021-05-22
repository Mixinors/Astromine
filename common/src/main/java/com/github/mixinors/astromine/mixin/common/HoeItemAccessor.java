package com.github.mixinors.astromine.mixin.common;

import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HoeItem.class)
public interface HoeItemAccessor {
	@Invoker("<init>")
	static HoeItem init(ToolMaterial toolMaterial, int i, float f, Item.Settings settings) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
