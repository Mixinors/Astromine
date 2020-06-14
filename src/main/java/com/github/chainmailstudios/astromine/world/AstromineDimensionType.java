package com.github.chainmailstudios.astromine.world;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public class AstromineDimensionType extends DimensionType {
	public static final RegistryKey<DimensionOptions> OPTIONS = RegistryKey.of(Registry.DIMENSION_OPTIONS, AstromineCommon.identifier("astromine"));
	public static final RegistryKey<DimensionType> REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("astromine_type"));

	public static final DimensionType INSTANCE = new AstromineDimensionType();

	protected AstromineDimensionType() {
		super(OptionalLong.of(15000L), false, false, false, false, false, false, false, false, true, false, 256, VoronoiBiomeAccessType.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getId(), 0.025f);
	}
}
