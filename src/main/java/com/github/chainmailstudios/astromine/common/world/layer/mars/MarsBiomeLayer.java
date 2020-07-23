package com.github.chainmailstudios.astromine.common.world.layer.mars;

import com.github.chainmailstudios.astromine.registry.AstromineBiomes;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;

public enum MarsBiomeLayer implements ParentedLayer, IdentityCoordinateTransformer {
    INSTANCE;
    private static final int RIVER_ID = Registry.BIOME.getRawId(AstromineBiomes.MARS_RIVERBED);
    private static final int MARS_ID = Registry.BIOME.getRawId(AstromineBiomes.MARS);

    @Override
    public int sample(LayerSampleContext<?> context, LayerSampler parent, int x, int z) {
        if (parent.sample(x, z) == RIVER_ID) {
            return RIVER_ID;
        }

        return MARS_ID;
    }
}
