package com.github.mixinors.astromine.mixin.common;

import net.minecraft.particle.DefaultParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DefaultParticleType.class)
public interface DefaultParticleTypeAccessor {
	@Invoker("<init>")
	static DefaultParticleType init(boolean bl) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
