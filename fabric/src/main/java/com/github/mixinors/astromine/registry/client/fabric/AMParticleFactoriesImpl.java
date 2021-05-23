package com.github.mixinors.astromine.registry.client.fabric;

import com.github.mixinors.astromine.client.model.EmptyUnbakedModel;
import com.github.mixinors.astromine.client.particle.RocketFlameParticle;
import com.github.mixinors.astromine.mixin.common.CrackParticleAccessor;
import com.github.mixinors.astromine.registry.client.AMClientModels;
import com.github.mixinors.astromine.registry.client.AMParticleFactories;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMParticles;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.item.ItemStack;

public class AMParticleFactoriesImpl {
	public static void postInit() {
		ParticleFactoryRegistry.getInstance().register(AMParticles.SPACE_SLIME.get(), (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> CrackParticleAccessor.init(world, x, y, z, new ItemStack(AMItems.SPACE_SLIME_BALL.get())));
		
		ParticleFactoryRegistry.getInstance().register(AMParticles.ROCKET_FLAME.get(), provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			var particle = new RocketFlameParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			
			particle.setSprite(provider);
			
			return particle;
		});
	}
}
