package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.client.render.AstromineSkyProperties;
import com.github.chainmailstudios.astromine.registry.AstromineDimensionTypes;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.class)
public class SkyPropertiesMixin {

    @Shadow @Final private static Object2ObjectMap<RegistryKey<DimensionType>, SkyProperties> BY_DIMENSION_TYPE;

    static {
        BY_DIMENSION_TYPE.put(
                AstromineDimensionTypes.REGISTRY_KEY,
                new AstromineSkyProperties()
        );
    }
}
