package com.github.mixinors.astromine.datagen.provider.tag;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class AMEntityTypeTagProvider extends EntityTypeTagsProvider {
	public AMEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, AMCommon.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		var fishTag = tag(AMTagKeys.EntityTypeTags.FISH);
		for (var entityType : AMDatagenLists.EntityTypeLists.FISH) {
			fishTag.add(entityType);
		}
		
		var squidsTag = tag(AMTagKeys.EntityTypeTags.SQUIDS);
		for (var entityType : AMDatagenLists.EntityTypeLists.SQUIDS) {
			squidsTag.add(entityType);
		}
		
		var guardiansTag = tag(AMTagKeys.EntityTypeTags.GUARDIANS);
		for (var entityType : AMDatagenLists.EntityTypeLists.GUARDIANS) {
			guardiansTag.add(entityType);
		}
		
		var skeletonsTag = tag(AMTagKeys.EntityTypeTags.SKELETONS);
		for (var entityType : AMDatagenLists.EntityTypeLists.SKELETONS) {
			skeletonsTag.add(entityType);
		}
		
		var zombiesTag = tag(AMTagKeys.EntityTypeTags.ZOMBIES);
		for (var entityType : AMDatagenLists.EntityTypeLists.ZOMBIES) {
			zombiesTag.add(entityType);
		}
		
		var spaceSlimesTag = tag(AMTagKeys.EntityTypeTags.SPACE_SLIMES);
		for (var entityType : AMDatagenLists.EntityTypeLists.SPACE_SLIMES) {
			spaceSlimesTag.add(entityType);
		}
		
		var ignoresDimensionalLayersTag = tag(AMTagKeys.EntityTypeTags.IGNORES_DIMENSIONAL_LAYERS);
		for (var entityType : AMDatagenLists.EntityTypeLists.IGNORES_DIMENSIONAL_LAYERS) {
			ignoresDimensionalLayersTag.add(entityType);
		}
		
		var doesNotBreatheTag = tag(AMTagKeys.EntityTypeTags.DOES_NOT_BREATHE);
		for (var entityType : AMDatagenLists.EntityTypeLists.DOES_NOT_BREATHE_ENTITY_TYPES) {
			doesNotBreatheTag.add(entityType);
		}
		for (var tag : AMDatagenLists.EntityTypeTagLists.DOES_NOT_BREATHE_TAGS) {
			doesNotBreatheTag.addTag(tag);
		}
		
		var canBreatheWaterTag = tag(AMTagKeys.EntityTypeTags.CAN_BREATHE_WATER);
		for (var entityType : AMDatagenLists.EntityTypeLists.CAN_BREATHE_WATER_ENTITY_TYPES) {
			canBreatheWaterTag.add(entityType);
		}
		for (var tag : AMDatagenLists.EntityTypeTagLists.CAN_BREATHE_WATER_TAGS) {
			canBreatheWaterTag.addTag(tag);
		}
		
		var canBreatheLavaTag = tag(AMTagKeys.EntityTypeTags.CAN_BREATHE_LAVA);
		for (var entityType : AMDatagenLists.EntityTypeLists.CAN_BREATHE_LAVA_ENTITY_TYPES) {
			canBreatheLavaTag.add(entityType);
		}
		
		var canBreatheInSpaceTag = tag(AMTagKeys.EntityTypeTags.CAN_BREATHE_IN_SPACE);
		for (var entityType : AMDatagenLists.EntityTypeLists.SPACE_SLIMES) {
			canBreatheInSpaceTag.add(entityType);
		}

		var cannotBreatheOxygenTag = tag(AMTagKeys.EntityTypeTags.CANNOT_BREATHE_OXYGEN);
		for (var tag : AMDatagenLists.EntityTypeTagLists.CANNOT_BREATHE_OXYGEN_TAGS) {
			cannotBreatheOxygenTag.addTag(tag);
		}
	}
}
