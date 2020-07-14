package com.github.chainmailstudios.astromine.common.dimension;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.dimension.base.AtmosphericDimensionType;

import java.util.OptionalLong;

public class EarthSpaceDimensionType extends AtmosphericDimensionType {
	public static final RegistryKey<DimensionOptions> EARTH_SPACE_OPTIONS = RegistryKey.of(Registry.DIMENSION_OPTIONS, AstromineCommon.identifier("earth_space"));
	public static final RegistryKey<DimensionType> EARTH_SPACE_REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("earth_space"));

	public static final DimensionType INSTANCE = new EarthSpaceDimensionType();

	protected EarthSpaceDimensionType() {
		super(OptionalLong.of(15000L), false, false, false, false, false, false, false, false, true, false, 256, VoronoiBiomeAccessType.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getId(), 0.025f);
	}
}
