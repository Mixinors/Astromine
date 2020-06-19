package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;

@Mixin (CrackParticle.class)
public interface CrackParticleAccessor {
	@Invoker
	static CrackParticle createCrackParticle(ClientWorld world, double x, double y, double z, ItemStack stack) {
		throw new UnsupportedOperationException();
	}
}
