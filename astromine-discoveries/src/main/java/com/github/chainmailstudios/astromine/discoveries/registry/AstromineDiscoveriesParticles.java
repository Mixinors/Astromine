package com.github.chainmailstudios.astromine.discoveries.registry;

import net.minecraft.particle.DefaultParticleType;

import com.github.chainmailstudios.astromine.registry.AstromineParticles;

public class AstromineDiscoveriesParticles extends AstromineParticles {
	public static final DefaultParticleType SPACE_SLIME = register("space_slime", false);
	public static final DefaultParticleType ROCKET_FLAME = register("rocket_flame", true);
	public static final DefaultParticleType MARS_DUST = register("mars_dust", false);

	public static void initialize() {

	}
}
