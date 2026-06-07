package com.github.mixinors.astromine.datagen.provider.tag;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class AMDimensionTypeTagProvider extends TagsProvider<DimensionType> {
	public AMDimensionTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, Registries.DIMENSION_TYPE, lookupProvider, AMCommon.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(AMTagKeys.DimensionTypeTags.IS_VACUUM)
				.add(AMWorlds.EARTH_ORBIT_DIMENSION_TYPE_KEY)
				.add(AMWorlds.MOON_DIMENSION_TYPE_KEY)
				.add(AMWorlds.MOON_ORBIT_DIMENSION_TYPE_KEY)
				.add(AMWorlds.ROCKET_INTERIORS_DIMENSION_TYPE_KEY);
	}
}
