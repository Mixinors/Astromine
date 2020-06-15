package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.mixin.CrackParticleAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.impl.client.particle.ParticleFactoryRegistryImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class AstromineParticles {

    public static final DefaultParticleType SPACE_SLIME = register("space_slime", false);

    /**
     * Registers a new {@link DefaultParticleType} instance under the given name.
     *
     * @param name       Name of {@link DefaultParticleType} to register
     * @param alwaysShow Whether or not the particle should always appear visible
     * @return Registered {@link DefaultParticleType}
     */
    public static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier("astromine", name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void initialize() {
        ParticleFactoryRegistryImpl.INSTANCE.register(
                SPACE_SLIME,
                (parameters, world, x, y, z, velocityX, velocityY, velocityZ) ->
                        CrackParticleAccessor.createCrackParticle(world, x, y, z, new ItemStack(AstromineItems.SPACE_SLIME_BALL)
                        ));
    }
}
