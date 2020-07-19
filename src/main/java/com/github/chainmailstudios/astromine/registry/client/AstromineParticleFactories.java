package com.github.chainmailstudios.astromine.registry.client;

import com.github.chainmailstudios.astromine.client.particle.RocketFlameParticle;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class AstromineParticleFactories {
	public static void initialize() {
		ParticleFactoryRegistry.getInstance().register(AstromineParticles.SPACE_SLIME, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new CrackParticle(world, x, y, z, new ItemStack(AstromineItems.SPACE_SLIME_BALL)));
		ParticleFactoryRegistry.getInstance().register(AstromineParticles.ROCKET_FLAME, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			RocketFlameParticle particle = new RocketFlameParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			return particle;
		});
	}
}
