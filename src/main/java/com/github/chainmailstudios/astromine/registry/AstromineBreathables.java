package com.github.chainmailstudios.astromine.registry;

import net.minecraft.entity.EntityType;

import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;

public class AstromineBreathables {
	public static void initialize() {
		BreathableRegistry.INSTANCE.register(EntityType.COD, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DOLPHIN, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DROWNED, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ELDER_GUARDIAN, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.GUARDIAN, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.HUSK, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.MAGMA_CUBE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PHANTOM, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PUFFERFISH, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SALMON, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON_HORSE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SQUID, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRAY, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRIDER, AstromineTags.NORMAL_BREATHABLE, AstromineTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TROPICAL_FISH, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TURTLE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITHER, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITHER_SKELETON, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOGLIN, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_HORSE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_VILLAGER, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIFIED_PIGLIN, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PLAYER, AstromineTags.NORMAL_BREATHABLE);
	}
}
