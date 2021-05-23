package com.github.mixinors.astromine.mixin.common.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CrackParticle.class)
public interface CrackParticleAccessor {
	@Invoker("<init>")
	static CrackParticle init(ClientWorld clientWorld, double d, double e, double f, ItemStack itemStack) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
