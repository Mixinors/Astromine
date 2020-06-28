package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class AstromineParticles {
	public static final DefaultParticleType SPACE_SLIME = register("space_slime", false);
	public static final DefaultParticleType ROCKET_FLAME = register("rocket_flame", true);

	/**
	 * Registers a new {@link DefaultParticleType} instance under the given name.
	 *
	 * @param name       Name of {@link DefaultParticleType} to register
	 * @param alwaysShow Whether or not the particle should always appear visible
	 * @return Registered {@link DefaultParticleType}
	 */
	public static DefaultParticleType register(String name, boolean alwaysShow) {
		return Registry.register(Registry.PARTICLE_TYPE, AstromineCommon.identifier(name), FabricParticleTypes.simple(alwaysShow));
	}
}
