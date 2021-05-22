package com.github.mixinors.astromine.mixin.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidBlock.class)
public interface FluidBlockAccessor {
	@Invoker("<init>")
	static FluidBlock init(FlowableFluid flowableFluid, AbstractBlock.Settings settings) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
