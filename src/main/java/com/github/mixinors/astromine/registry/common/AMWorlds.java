/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.world.generation.space.*;
import net.minecraft.block.Block;
import net.minecraft.structure.StructureSet;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class AMWorlds {
	public static final Map<RegistryKey<DimensionOptions>, Supplier<DimensionOptions>> DIMENSIONS = new HashMap<>();
	private static final Set<RegistryKey<World>> KEYS = new HashSet<>();
	private static final DimensionType.MonsterSettings MONSTER_SETTINGS = new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 7);
	
	public static final RegistryKey<World> ROCKET_INTERIORS = registerDimension(AMCommon.id("rocket_interiors"), (s, b) -> new RocketInteriorsChunkGenerator(s, new RocketInteriorsBiomeSource(b)),
			15000,
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
			0F,
			MONSTER_SETTINGS
	);
	
	public static final RegistryKey<World> MOON = registerDimension(AMCommon.id("moon"), (s, b) -> new MoonChunkGenerator(s, new MoonBiomeSource(b)),
			15000,
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
			0.4F,
			MONSTER_SETTINGS
	);
	
	public static final RegistryKey<World> EARTH_ORBIT = registerDimension(AMCommon.id("earth_orbit"), (s, b) -> new EarthOrbitChunkGenerator(s, new EarthOrbitBiomeSource(b)),
			15000,
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
			0.025F,
			MONSTER_SETTINGS
	);
	
	public static void init() {
	}
	
	public static boolean isVacuum(RegistryEntry<DimensionType> dimensionType) {
		return dimensionType.isIn(AMTagKeys.DimensionTypeTags.IS_VACUUM);
	}
	
	public static boolean isAstromine(RegistryKey<World> key) {
		return KEYS.contains(key);
	}
	
	public static RegistryKey<World> registerDimension(Identifier id, BiFunction<Registry<StructureSet>, Registry<Biome>, ChunkGenerator> chunkGenerator, long fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean bedWorks, boolean piglinAndRespawnAnchorSafe, int minY, int height, int logicalHeight, TagKey<Block> infiniburn, float ambientLight, DimensionType.MonsterSettings monsterSettings) {
		var dimWorldKey = createWorldKey(id);
		var dimTypeKey = createTypeKey(id);
		var dimOptionsKey = createOptionsKey(id);
		
		BuiltinRegistries.add(BuiltinRegistries.DIMENSION_TYPE, dimTypeKey, new DimensionType(
				OptionalLong.of(fixedTime),
				hasSkylight,
				hasCeiling,
				ultrawarm,
				natural,
				coordinateScale,
				bedWorks,
				piglinAndRespawnAnchorSafe,
				minY,
				height,
				logicalHeight,
				infiniburn,
				AMCommon.id("space"),
				ambientLight,
				monsterSettings
		));
		
		DIMENSIONS.put(dimOptionsKey, () -> new DimensionOptions(BuiltinRegistries.DIMENSION_TYPE.getOrCreateEntry(dimTypeKey), chunkGenerator.apply(BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.BIOME)));
		
		return dimWorldKey;
	}
	
	public static RegistryKey<DimensionOptions> createOptionsKey(Identifier id) {
		return RegistryKey.of(Registry.DIMENSION_KEY, id);
	}
	
	private static RegistryKey<DimensionType> createTypeKey(Identifier id) {
		return RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);
	}
	
	public static RegistryKey<World> createWorldKey(Identifier id) {
		var key = RegistryKey.of(Registry.WORLD_KEY, id);
		KEYS.add(key);
		return key;
	}
}
