package com.github.chainmailstudios.astromine.technologies.registry.client;

import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import com.github.chainmailstudios.astromine.registry.client.AstromineParticleFactories;
import com.github.chainmailstudios.astromine.technologies.client.particle.RocketFlameParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class AstromineTechnologiesParticleFactories extends AstromineParticleFactories {
	public static void initialize() {
		ParticleFactoryRegistry.getInstance().register(AstromineParticles.ROCKET_FLAME, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			RocketFlameParticle particle = new RocketFlameParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			return particle;
		});
	}
}
