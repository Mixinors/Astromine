package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashSet;
import java.util.Set;

public class AstromineDimensions {
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();

	public static final RegistryKey<DimensionOptions> EARTH_SPACE_OPTIONS = register(Registry.DIMENSION_OPTIONS, AstromineCommon.identifier("earth_space"));
	public static final RegistryKey<DimensionType> EARTH_SPACE_REGISTRY_KEY = register(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("earth_space"));

	public static final RegistryKey<DimensionOptions> MOON_OPTIONS = register(Registry.DIMENSION_OPTIONS, AstromineCommon.identifier("moon"));
	public static final RegistryKey<DimensionType> MOON_REGISTRY_KEY = register(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("moon"));

	public static final RegistryKey<DimensionType> MARS_REGISTRY_KEY = register(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("mars"));

	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		RegistryKey<T> key = RegistryKey.of(registry, identifier);
		KEYS.add(key);
		return key;
	}

	public static boolean isAstromine(RegistryKey<?> key) {
		return KEYS.contains(key);
	}

	public static void initialize() {
		// Unused.
	}
}
