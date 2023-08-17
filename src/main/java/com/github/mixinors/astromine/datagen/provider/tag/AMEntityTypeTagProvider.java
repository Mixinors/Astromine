package com.github.mixinors.astromine.datagen.provider.tag;

import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AMEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public AMEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup lookup) {
		var fishTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.FISH);
		for (var entityType : AMDatagenLists.EntityTypeLists.FISH) {
			fishTag.add(entityType);
		}
		
		var squidsTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.SQUIDS);
		for (var entityType : AMDatagenLists.EntityTypeLists.SQUIDS) {
			squidsTag.add(entityType);
		}
		
		var guardiansTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.GUARDIANS);
		for (var entityType : AMDatagenLists.EntityTypeLists.GUARDIANS) {
			guardiansTag.add(entityType);
		}
		
		var skeletonsTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.SKELETONS);
		for (var entityType : AMDatagenLists.EntityTypeLists.SKELETONS) {
			skeletonsTag.add(entityType);
		}
		
		var zombiesTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.ZOMBIES);
		for (var entityType : AMDatagenLists.EntityTypeLists.ZOMBIES) {
			zombiesTag.add(entityType);
		}
		
		var spaceSlimesTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.SPACE_SLIMES);
		for (var entityType : AMDatagenLists.EntityTypeLists.SPACE_SLIMES) {
			spaceSlimesTag.add(entityType);
		}
		
		var ignoresDimensionalLayersTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.IGNORES_DIMENSIONAL_LAYERS);
		for (var entityType : AMDatagenLists.EntityTypeLists.IGNORES_DIMENSIONAL_LAYERS) {
			ignoresDimensionalLayersTag.add(entityType);
		}
		
		var doesNotBreatheTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.DOES_NOT_BREATHE);
		for (var entityType : AMDatagenLists.EntityTypeLists.DOES_NOT_BREATHE_ENTITY_TYPES) {
			doesNotBreatheTag.add(entityType);
		}
		for (var tag : AMDatagenLists.EntityTypeTagLists.DOES_NOT_BREATHE_TAGS) {
			doesNotBreatheTag.addTag(tag);
		}
		
		var canBreatheWaterTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.CAN_BREATHE_WATER);
		for (var entityType : AMDatagenLists.EntityTypeLists.CAN_BREATHE_WATER_ENTITY_TYPES) {
			canBreatheWaterTag.add(entityType);
		}
		for (var tag : AMDatagenLists.EntityTypeTagLists.CAN_BREATHE_WATER_TAGS) {
			canBreatheWaterTag.addTag(tag);
		}
		
		var canBreatheLavaTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.CAN_BREATHE_LAVA);
		for (var entityType : AMDatagenLists.EntityTypeLists.CAN_BREATHE_LAVA_ENTITY_TYPES) {
			canBreatheLavaTag.add(entityType);
		}
		
		var cannotBreatheOxygenTag = getOrCreateTagBuilder(AMTagKeys.EntityTypeTags.CANNOT_BREATHE_OXYGEN);
		for (var tag : AMDatagenLists.EntityTypeTagLists.CANNOT_BREATHE_OXYGEN_TAGS) {
			cannotBreatheOxygenTag.addTag(tag);
		}
	}
}