package com.github.mixinors.astromine.mixin.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StairsBlock.class)
public interface StairsBlockAccessor {
	@Invoker("<init>")
	static StairsBlock init(BlockState blockState, AbstractBlock.Settings settings) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
