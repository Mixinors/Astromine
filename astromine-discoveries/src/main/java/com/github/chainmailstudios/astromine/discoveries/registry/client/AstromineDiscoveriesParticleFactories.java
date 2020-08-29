package com.github.chainmailstudios.astromine.discoveries.registry.client;

import com.github.chainmailstudios.astromine.discoveries.client.particle.MarsDustParticle;
import com.github.chainmailstudios.astromine.discoveries.client.particle.RocketFlameParticle;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import com.github.chainmailstudios.astromine.registry.client.AstromineParticleFactories;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.item.ItemStack;

public class AstromineDiscoveriesParticleFactories extends AstromineParticleFactories {
	public static void initialize() {
		ParticleFactoryRegistry.getInstance().register(AstromineDiscoveriesParticles.SPACE_SLIME, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new CrackParticle(world, x, y, z, new ItemStack(AstromineDiscoveriesItems.SPACE_SLIME_BALL)));

		ParticleFactoryRegistry.getInstance().register(AstromineDiscoveriesParticles.MARS_DUST, MarsDustParticle.Factory::new);

		ParticleFactoryRegistry.getInstance().register(AstromineDiscoveriesParticles.ROCKET_FLAME, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			RocketFlameParticle particle = new RocketFlameParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			return particle;
		});
	}
}
