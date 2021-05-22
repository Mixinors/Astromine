package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.client.particle.RocketFlameParticle;
import com.github.mixinors.astromine.mixin.common.CrackParticleAccessor;
import com.github.mixinors.astromine.registry.client.AMParticleFactories;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AMParticleFactories.class)
public class ParticleFactoriesMixin {
	@Overwrite
	@SuppressWarnings("all")
	public static void init() {
		ParticleFactoryRegistry.getInstance().register(AMParticles.SPACE_SLIME.get(), (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> CrackParticleAccessor.init(world, x, y, z, new ItemStack(AMItems.SPACE_SLIME_BALL.get())));
		
		ParticleFactoryRegistry.getInstance().register(AMParticles.ROCKET_FLAME.get(), provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			var particle = new RocketFlameParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			
			particle.setSprite(provider);
			
			return particle;
		});
	}
}
