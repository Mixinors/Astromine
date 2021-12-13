package com.github.mixinors.astromine.common.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

public class CauldronUtils {
	public static final Set<Map<Item, CauldronBehavior>> CAULDRON_BEHAVIOR_MAPS = new HashSet<>(Set.of(
			CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR,
			CauldronBehavior.WATER_CAULDRON_BEHAVIOR,
			CauldronBehavior.LAVA_CAULDRON_BEHAVIOR,
			CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR
	));
	private static final Set<CauldronBehaviorMapEntry> GLOBAL_BEHAVIORS = new HashSet<>();

	public static void addCauldronBehaviorMap(Map<Item, CauldronBehavior> behaviorMap) {
		CAULDRON_BEHAVIOR_MAPS.add(behaviorMap);
		registerGlobalBehaviors(behaviorMap);
	}

	public static Set<Map<Item, CauldronBehavior>> getCauldronBehaviorMaps() {
		return CAULDRON_BEHAVIOR_MAPS;
	}

	/**
	 * Adds all global behaviors to this behavior map
	 * @param behaviorMap
	 */
	private static void registerGlobalBehaviors(Map<Item, CauldronBehavior> behaviorMap) {
		CauldronBehavior.registerBucketBehavior(behaviorMap);
		GLOBAL_BEHAVIORS.forEach((behavior) -> {
			behaviorMap.put(behavior.item(), behavior.behavior());
		});
	}

	/**
	 * Adds a new cauldron behavior to be added to every behavior map
	 */
	public static void addGlobalBehavior(Item item, CauldronBehavior behavior) {
		getCauldronBehaviorMaps().forEach((map) -> map.put(item, behavior));
		GLOBAL_BEHAVIORS.add(new CauldronBehaviorMapEntry(item, behavior));
	}

	public static void addFillWithFluidBehavior(Item bucket, Block cauldron) {
		CauldronBehavior fillWithFluidBehavior = (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(world, pos, player, hand, stack, cauldron.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY);
		addGlobalBehavior(bucket, fillWithFluidBehavior);
	}

	public static CauldronBehavior createEmptyCauldronBehavior(Item bucket) {
		return (state2, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(state2, world, pos, player, hand, stack, new ItemStack(bucket), state -> true, SoundEvents.ITEM_BUCKET_FILL);
	}

	public static void addEmptyCauldronBehavior(Map<Item, CauldronBehavior> behaviorMap, Item bucket) {
		behaviorMap.put(Items.BUCKET, createEmptyCauldronBehavior(bucket));
	}

	private record CauldronBehaviorMapEntry(Item item, CauldronBehavior behavior){}
}
