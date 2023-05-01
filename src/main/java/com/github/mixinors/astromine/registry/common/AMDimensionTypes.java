package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public class AMDimensionTypes {
	private static final DimensionType.MonsterSettings MONSTER_SETTINGS = new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 7);
	
	public static final DimensionType ROCKET_INTERIORS = new DimensionType(
			OptionalLong.of(15000),
			false,
			false,
			false,
			false,
			1.0,
			false,
			false,
			0,
			96,
			96,
			AMTagKeys.BlockTags.INFINIBURN_SPACE,
			AMCommon.id("space"),
			0.0F,
			MONSTER_SETTINGS
	);
	
	public static final DimensionType MOON = new DimensionType(
			OptionalLong.of(15000),
			true,
			false,
			false,
			false,
			1.0,
			false,
			false,
			-64,
			384,
			382,
			AMTagKeys.BlockTags.INFINIBURN_SPACE,
			AMCommon.id("space"),
			0.4F,
			MONSTER_SETTINGS
	);
	
	public static final DimensionType EARTH_ORBIT = new DimensionType(
			OptionalLong.of(15000),
			false,
			false,
			false,
			false,
			1.0,
			false,
			false,
			0,
			512,
			512,
			AMTagKeys.BlockTags.INFINIBURN_SPACE,
			AMCommon.id("space"),
			0.025F,
			MONSTER_SETTINGS
	);
}
