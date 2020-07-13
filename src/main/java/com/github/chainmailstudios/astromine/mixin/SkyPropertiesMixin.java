package com.github.chainmailstudios.astromine.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.client.render.sky.SpaceSkyProperties;
import com.github.chainmailstudios.astromine.common.dimension.EarthSpaceDimensionType;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.class)
public class SkyPropertiesMixin {

	@Shadow
	@Final
	private static Object2ObjectMap<RegistryKey<DimensionType>, SkyProperties> BY_DIMENSION_TYPE;

	static {
		BY_DIMENSION_TYPE.put(EarthSpaceDimensionType.EARTH_SPACE_REGISTRY_KEY, new SpaceSkyProperties());
	}
}
