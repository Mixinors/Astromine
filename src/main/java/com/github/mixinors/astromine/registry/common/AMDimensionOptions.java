package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.world.generation.space.*;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.dimension.DimensionOptions;

public class AMDimensionOptions {
	public static final DimensionOptions ROCKET_INTERIORS = new DimensionOptions(BuiltinRegistries.DIMENSION_TYPE.getOrCreateEntry(AMWorlds.ROCKET_INTERIORS_DIMENSION_TYPE_KEY), new RocketInteriorsChunkGenerator(BuiltinRegistries.STRUCTURE_SET, new RocketInteriorsBiomeSource(BuiltinRegistries.BIOME)));
	public static final DimensionOptions MOON = new DimensionOptions(BuiltinRegistries.DIMENSION_TYPE.getOrCreateEntry(AMWorlds.ROCKET_INTERIORS_DIMENSION_TYPE_KEY), new MoonChunkGenerator(BuiltinRegistries.STRUCTURE_SET, new MoonBiomeSource(BuiltinRegistries.BIOME)));
	public static final DimensionOptions EARTH_ORBIT = new DimensionOptions(BuiltinRegistries.DIMENSION_TYPE.getOrCreateEntry(AMWorlds.ROCKET_INTERIORS_DIMENSION_TYPE_KEY), new EarthOrbitChunkGenerator(BuiltinRegistries.STRUCTURE_SET, new EarthOrbitBiomeSource(BuiltinRegistries.BIOME)));
}
