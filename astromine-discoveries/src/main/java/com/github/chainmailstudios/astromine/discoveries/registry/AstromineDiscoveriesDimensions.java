package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class AstromineDiscoveriesDimensions extends AstromineDimensions {
	public static final Identifier EARTH_SPACE_ID = AstromineCommon.identifier("earth_space");
	public static final RegistryKey<DimensionOptions> EARTH_SPACE_OPTIONS = register(Registry.DIMENSION_OPTIONS, EARTH_SPACE_ID);
	public static final RegistryKey<DimensionType> EARTH_SPACE_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, EARTH_SPACE_ID);
	public static final RegistryKey<World> EARTH_SPACE_WORLD = register(Registry.DIMENSION, EARTH_SPACE_ID);

	public static final Identifier MOON_ID = AstromineCommon.identifier("moon");
	public static final RegistryKey<DimensionOptions> MOON_OPTIONS = register(Registry.DIMENSION_OPTIONS, MOON_ID);
	public static final RegistryKey<DimensionType> MOON_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, MOON_ID);
	public static final RegistryKey<World> MOON_WORLD = register(Registry.DIMENSION, MOON_ID);

	public static final Identifier MARS_ID = AstromineCommon.identifier("mars");
	public static final RegistryKey<DimensionOptions> MARS_OPTIONS = register(Registry.DIMENSION_OPTIONS, MARS_ID);
	public static final RegistryKey<DimensionType> MARS_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, MARS_ID);
	public static final RegistryKey<World> MARS_WORLD = register(Registry.DIMENSION, MARS_ID);

	public static final Identifier VULCAN_ID = AstromineCommon.identifier("vulcan");
	public static final RegistryKey<DimensionOptions> VULCAN_OPTIONS = register(Registry.DIMENSION_OPTIONS, VULCAN_ID);
	public static final RegistryKey<DimensionType> VULCAN_TYPE_KEY = register(Registry.DIMENSION_TYPE_KEY, VULCAN_ID);
	public static final RegistryKey<World> VULCAN_WORLD = register(Registry.DIMENSION, VULCAN_ID);

	public static void initialize() {

	}
}
